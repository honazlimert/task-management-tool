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
import com.atmosware.internship_project_tmt.exception.UserNotFoundException;
import com.atmosware.internship_project_tmt.mapper.TaskMapper;
import com.atmosware.internship_project_tmt.repository.ProjectRepository;
import com.atmosware.internship_project_tmt.repository.TaskHistoryRepository;
import com.atmosware.internship_project_tmt.repository.TaskRepository;
import com.atmosware.internship_project_tmt.repository.UserRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ProjectRepository projectRepository;
    @Mock
    private TaskHistoryRepository taskHistoryRepository;
    @Mock
    private TaskMapper taskMapper;

    @InjectMocks
    private TaskService taskService;

    @Test
    void createTask_Success() {
        // Task başarıyla oluşturuluyor mu?
        CreateTaskRequest request = new CreateTaskRequest();
        request.setTitle("Yeni Task");
        request.setProjectId(1L);
        request.setAssigneeId(2L);
        request.setPriority(Priority.HIGH);

        Project project = new Project();
        project.setId(1L);

        User user = new User();
        user.setId(2L);

        Task task = new Task();
        Task savedTask = new Task();
        savedTask.setId(100L);
        savedTask.setStatus(Status.TODO);

        TaskResponse response = new TaskResponse();
        response.setId(100L);

        when(taskMapper.mapToEntity(request)).thenReturn(task);
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        when(userRepository.findById(2L)).thenReturn(Optional.of(user));
        when(taskRepository.save(any(Task.class))).thenReturn(savedTask);
        when(taskMapper.mapToResponse(savedTask)).thenReturn(response);

        TaskResponse result = taskService.createTask(request);

        assertNotNull(result);
        assertEquals(100L, result.getId());
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    void createTask_ProjectNotFound_ThrowsException() {
        // Olmayan project ile task oluşturulursa hata dönüyor mu?
        CreateTaskRequest request = new CreateTaskRequest();
        request.setProjectId(99L);

        Task mockTask = new Task();
        when(taskMapper.mapToEntity(request)).thenReturn(mockTask);

        when(projectRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ProjectNotFoundException.class, () -> taskService.createTask(request));
        verify(taskRepository, never()).save(any(Task.class));
    }

    @Test
    void createTask_UserNotFound_ThrowsException() {
        // Olmayan user assignee yapılırsa hata dönüyor mu?
        CreateTaskRequest request = new CreateTaskRequest();
        request.setProjectId(1L);
        request.setAssigneeId(99L);

        Project project = new Project();
        project.setId(1L);

        Task mockTask = new Task();
        when(taskMapper.mapToEntity(request)).thenReturn(mockTask);

        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> taskService.createTask(request));
        verify(taskRepository, never()).save(any(Task.class));
    }

    @Test
    void updateTaskStatus_Success_And_CreatesHistory() {
        // Task status değiştirilebiliyor mu? & Task status değiştiğinde history oluşturuluyor mu?
        Long taskId = 1L;
        Task existingTask = new Task();
        existingTask.setId(taskId);
        existingTask.setStatus(Status.TODO); // eski durum

        Task savedTask = new Task();
        savedTask.setId(taskId);
        savedTask.setStatus(Status.IN_PROGRESS); // yeni durum

        TaskResponse response = new TaskResponse();
        response.setId(taskId);
        response.setStatus(Status.IN_PROGRESS);

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(existingTask));
        when(taskRepository.save(any(Task.class))).thenReturn(savedTask);
        when(taskMapper.mapToResponse(savedTask)).thenReturn(response);

        TaskResponse result = taskService.updateTaskStatus(taskId, Status.IN_PROGRESS);

        assertEquals(Status.IN_PROGRESS, result.getStatus());
        verify(taskRepository, times(1)).save(existingTask);
        verify(taskHistoryRepository, times(1)).save(any(TaskHistory.class));
    }

    @Test
    void updateTaskStatus_FromDoneToTodo_ThrowsException() {
        // DONE task TODO yapılmaya çalışıldığında hata dönüyor mu?
        Long taskId = 1L;
        Task existingTask = new Task();
        existingTask.setId(taskId);
        existingTask.setStatus(Status.DONE);

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(existingTask));

        assertThrows(InvalidTaskStatusException.class, () -> taskService.updateTaskStatus(taskId, Status.TODO));
        verify(taskRepository, never()).save(any(Task.class));
        verify(taskHistoryRepository, never()).save(any(TaskHistory.class));
    }
}