package com.atmosware.internship_project_tmt.service;

import com.atmosware.internship_project_tmt.dto.request.CreateTaskRequest;
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

        // Kullanıcının gönderdiği projectId ile veritabanından gerçek projeyi bul ve set et
        if (request.getProjectId() != null) {
            Project project = projectRepository.findById(request.getProjectId()).orElse(null);
            task.setProject(project);
        }

        // Kullanıcının gönderdiği assigneeId ile veritabanından gerçek kullanıcıyı bul ve set et
        if (!Objects.isNull(request.getAssigneeId()) ) {
            User assignee = userRepository.findById(request.getAssigneeId()).orElse(null);
            task.setAssignee(assignee);
        }

        Task savedTask = taskRepository.save(task);

        return taskMapper.mapToResponse(savedTask);
    }

    // filtre parametrelerini metodun imzasına ekliyoruz
    public Page<TaskResponse> getAllTasks(Status status, Priority priority, Long projectId, Long assigneeId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        // findAll yerine dinamik sorguyu çağırıyoruz
        Page<Task> taskPage = taskRepository.findByFilters(status, priority, projectId, assigneeId, pageable);

        return taskPage.map(taskMapper::mapToResponse);
    }

    public TaskResponse getTaskById(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Görev bulunamadı!"));
        return taskMapper.mapToResponse(task);
    }

    public TaskResponse updateTask(Long id, Task updatedTask) {
        Task existingTask = taskRepository.findById(id).orElse(null);
        if (existingTask != null) {
            existingTask.setTitle(updatedTask.getTitle());
            existingTask.setDescription(updatedTask.getDescription());
            existingTask.setPriority(updatedTask.getPriority());
            existingTask.setStoryPoint(updatedTask.getStoryPoint());
            Task savedTask = taskRepository.save(existingTask);
            return taskMapper.mapToResponse(savedTask);
        }
        return null;
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

        return taskMapper.mapToResponse(savedTask);
    }

    public TaskResponse updateTaskAssignee(Long id, User newAssignee) {
        Task existingTask = taskRepository.findById(id).orElse(null);
        if (existingTask != null) {
            existingTask.setAssignee(newAssignee);
            Task savedTask = taskRepository.save(existingTask);
            // entity to dto
            return taskMapper.mapToResponse(savedTask);
        }
        return null;
    }
}