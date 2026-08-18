package com.atmosware.internship_project_tmt.service;

import com.atmosware.internship_project_tmt.dto.request.CreateTaskRequest;
import com.atmosware.internship_project_tmt.dto.response.TaskResponse;
import com.atmosware.internship_project_tmt.entity.Project;
import com.atmosware.internship_project_tmt.entity.Task;
import com.atmosware.internship_project_tmt.entity.User;
import com.atmosware.internship_project_tmt.entity.enums.Status;
import com.atmosware.internship_project_tmt.mapper.TaskMapper;
import com.atmosware.internship_project_tmt.repository.ProjectRepository;
import com.atmosware.internship_project_tmt.repository.TaskRepository;
import com.atmosware.internship_project_tmt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository; // Proje bulmak için ekledik
    private final UserRepository userRepository;       // Kullanıcı bulmak için ekledik
    private final TaskMapper taskMapper;             // Dönüştürücümüzü ekledik

    public TaskResponse createTask(CreateTaskRequest request) {
        // Mapper ile gelen DTO'yu Entity'ye çevir (İçinde henüz proje ve kullanıcı yok)
        Task task = taskMapper.mapToEntity(request);

        // Kullanıcının gönderdiği projectId ile veritabanından gerçek projeyi bul ve set et
        if (request.getProjectId() != null) {
            Project project = projectRepository.findById(request.getProjectId()).orElse(null);
            task.setProject(project);
        }

        // Kullanıcının gönderdiği assigneeId ile veritabanından gerçek kullanıcıyı bul ve set et
        if (request.getAssigneeId() != null) {
            User assignee = userRepository.findById(request.getAssigneeId()).orElse(null);
            task.setAssignee(assignee);
        }

        Task savedTask = taskRepository.save(task);

        return taskMapper.mapToResponse(savedTask);
    }

    public List<TaskResponse> getAllTasks() {
        List<Task> tasks = taskRepository.findAll();

        return tasks.stream()
                .map(taskMapper::mapToResponse)
                .collect(Collectors.toList());
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

    public Task updateTaskStatus(Long id, Status newStatus) {
        Task existingTask = taskRepository.findById(id).orElse(null);
        if (existingTask != null) {
            existingTask.setStatus(newStatus);
            return taskRepository.save(existingTask);
        }
        return null;
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