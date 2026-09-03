package com.atmosware.internship_project_tmt.service;

import com.atmosware.internship_project_tmt.dto.request.CreateTaskRequest;
import com.atmosware.internship_project_tmt.dto.request.UpdateTaskRequest;
import com.atmosware.internship_project_tmt.dto.response.TaskResponse;
import com.atmosware.internship_project_tmt.entity.Project;
import com.atmosware.internship_project_tmt.entity.Task;
import com.atmosware.internship_project_tmt.entity.TaskHistory;
import com.atmosware.internship_project_tmt.entity.User;
import com.atmosware.internship_project_tmt.entity.enums.Priority;
import com.atmosware.internship_project_tmt.entity.enums.Status;
import com.atmosware.internship_project_tmt.exception.InvalidTaskStatusException;
import com.atmosware.internship_project_tmt.exception.ProjectNotFoundException;
import com.atmosware.internship_project_tmt.exception.TaskNotFoundException;
import com.atmosware.internship_project_tmt.exception.UserNotFoundException;
import com.atmosware.internship_project_tmt.mapper.TaskMapper;
import com.atmosware.internship_project_tmt.repository.ProjectRepository;
import com.atmosware.internship_project_tmt.repository.TaskHistoryRepository;
import com.atmosware.internship_project_tmt.repository.TaskRepository;
import com.atmosware.internship_project_tmt.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskHistoryRepository taskHistoryRepository;
    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final TaskMapper taskMapper;

    public TaskResponse createTask(CreateTaskRequest request) {
        // dto to entity
        Task task = taskMapper.mapToEntity(request);

        // varsayılan status "todo"
        task.setStatus(Status.TODO);

        // project yoksa hata fırlat
        if (request.getProjectId() != null) {
            Project project = projectRepository.findById(request.getProjectId())
                    .orElseThrow(() -> new ProjectNotFoundException("Proje bulunamadı: " + request.getProjectId()));
            task.setProject(project);
        }

        // asignee yoksa hata fırlat
        if (request.getAssigneeId() != null) {
            User assignee = userRepository.findById(request.getAssigneeId())
                    .orElseThrow(() -> new UserNotFoundException("Kullanıcı bulunamadı: " + request.getAssigneeId()));
            task.setAssignee(assignee);
        }

        // projectId ile veritabanından projeyi bul ve set et
        if (request.getProjectId() != null) {
            Project project = projectRepository.findById(request.getProjectId()).orElse(null);
            task.setProject(project);
        }

        // assigneeId ile veritabanından kullanıcıyı bul ve set et
        if (!Objects.isNull(request.getAssigneeId()) ) {
            User assignee = userRepository.findById(request.getAssigneeId()).orElse(null);
            task.setAssignee(assignee);
        }

        // kaydet ve response dto'ya çevir
        Task savedTask = taskRepository.save(task);
        return taskMapper.mapToResponse(savedTask);
    }

    // filtre parametrelerini metodun imzasına ekliyoruz
    public Page<TaskResponse> getAllTasks(Status status, Priority priority, Long projectId, Long assigneeId, int page, int size) {
        // pageable
        Pageable pageable = PageRequest.of(page, size);

        // findAll yerine dinamik sorguyu çağırıyoruz
        Page<Task> taskPage = taskRepository.findByFilters(status, priority, projectId, assigneeId, pageable);

        // response dto'ya çevir
        return taskPage.map(taskMapper::mapToResponse);
    }

    public TaskResponse getTaskById(Long id) {
        // task db'de yoksa hata fırlat
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Görev bulunamadı!"));

        // response dto'ya çevir
        return taskMapper.mapToResponse(task);
    }

    public TaskResponse updateTask(Long id, UpdateTaskRequest request) {
        // task db'de yoksa hata fırlat
        Task existingTask = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Güncellenecek görev bulunamadı: " + id));

        // dto'dan gelen yeni değerleri mevcut göreve set et
        existingTask.setTitle(request.getTitle());
        existingTask.setDescription(request.getDescription());
        existingTask.setPriority(request.getPriority());
        existingTask.setStoryPoint(request.getStoryPoint());

        // kaydet ve response dto'ya çevir
        Task savedTask = taskRepository.save(existingTask);
        return taskMapper.mapToResponse(savedTask);
    }

    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }

    @Transactional
    public TaskResponse updateTaskStatus(Long id, Status newStatus) {
        // task db'de yoksa hata fırlat
        Task existingTask = taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException("Görev bulunamadı: " + id));

        // eski status'ı not alıyoruz
        Status oldStatus = existingTask.getStatus();

        // done status todo yapılamaz
        if (existingTask.getStatus() == Status.DONE && newStatus == Status.TODO) {
            throw new InvalidTaskStatusException("DONE olan bir görev tekrar TODO durumuna alınamaz.");
        }

        // kaydet
        existingTask.setStatus(newStatus);
        Task savedTask = taskRepository.save(existingTask);

        // log kaydını oluştur ve TaskHistory tablosuna kaydet
        TaskHistory history = new TaskHistory();
        history.setTaskId(savedTask.getId());
        history.setOldStatus(oldStatus);
        history.setNewStatus(newStatus);
        history.setChangedBy("Sistem Kullanıcısı"); // security eklenene kadar geçici değer
        history.setChangedDate(LocalDateTime.now());
        taskHistoryRepository.save(history);

        // response dto'ya çevir
        return taskMapper.mapToResponse(savedTask);
    }

    public TaskResponse updateTaskAssignee(Long id, Long assigneeId) {
        // task db'de yoksa hata fırlat
        Task existingTask = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Görev bulunamadı: " + id));

        // asignee db'de yoksa hata fırlat
        User newAssignee = userRepository.findById(assigneeId)
                .orElseThrow(() -> new UserNotFoundException("Atanacak kullanıcı bulunamadı: " + assigneeId));

        // kaydet ve response dto'ya çevir
        existingTask.setAssignee(newAssignee);
        Task savedTask = taskRepository.save(existingTask);
        return taskMapper.mapToResponse(savedTask);
    }
}