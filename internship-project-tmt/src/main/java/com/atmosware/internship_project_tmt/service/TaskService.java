package com.atmosware.internship_project_tmt.service;

import com.atmosware.internship_project_tmt.dto.request.CreateTaskRequest;
import com.atmosware.internship_project_tmt.dto.response.TaskResponse;
import com.atmosware.internship_project_tmt.entity.Project;
import com.atmosware.internship_project_tmt.entity.Task;
import com.atmosware.internship_project_tmt.entity.User;
import com.atmosware.internship_project_tmt.entity.enums.Priority;
import com.atmosware.internship_project_tmt.entity.enums.Status;
import com.atmosware.internship_project_tmt.exception.InvalidTaskStatusException;
import com.atmosware.internship_project_tmt.exception.ProjectNotFoundException;
import com.atmosware.internship_project_tmt.exception.TaskNotFoundException;
import com.atmosware.internship_project_tmt.exception.UserNotFoundException;
import com.atmosware.internship_project_tmt.mapper.TaskMapper;
import com.atmosware.internship_project_tmt.repository.ProjectRepository;
import com.atmosware.internship_project_tmt.repository.TaskRepository;
import com.atmosware.internship_project_tmt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository; // Proje bulmak için ekledik
    private final UserRepository userRepository;       // Kullanıcı bulmak için ekledik
    private final TaskMapper taskMapper;             // Dönüştürücümüzü ekledik

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

    public Task getTaskById(Long id) {
        return taskRepository.findById(id).orElse(null);
    }

    public Task updateTask(Long id, Task updatedTask) {
        Task existingTask = taskRepository.findById(id).orElse(null);
        if (existingTask != null) {
            existingTask.setTitle(updatedTask.getTitle());
            existingTask.setDescription(updatedTask.getDescription());
            existingTask.setPriority(updatedTask.getPriority());
            existingTask.setStoryPoint(updatedTask.getStoryPoint());
            return taskRepository.save(existingTask);
        }
        return null;
    }

    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }

    public TaskResponse updateTaskStatus(Long id, Status newStatus) {

        // task db'de yoksa hata fırlat
        Task existingTask = taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException("Görev bulunamadı: " + id));

        // done status todo yapılamaz
        if (existingTask.getStatus() == Status.DONE && newStatus == Status.TODO) {
            throw new InvalidTaskStatusException("DONE olan bir görev tekrar TODO durumuna alınamaz.");
        }

        existingTask.setStatus(newStatus);
        Task savedTask = taskRepository.save(existingTask);

        return taskMapper.mapToResponse(savedTask);
    }

    public Task updateTaskAssignee(Long id, User newAssignee) {
        Task existingTask = taskRepository.findById(id).orElse(null);
        if (existingTask != null) {
            existingTask.setAssignee(newAssignee);
            return taskRepository.save(existingTask);
        }
        return null;
    }
}