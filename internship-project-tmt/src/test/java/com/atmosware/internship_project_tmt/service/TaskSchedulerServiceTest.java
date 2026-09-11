package com.atmosware.internship_project_tmt.service;

import com.atmosware.internship_project_tmt.entity.Task;
import com.atmosware.internship_project_tmt.entity.enums.Status;
import com.atmosware.internship_project_tmt.repository.TaskHistoryRepository;
import com.atmosware.internship_project_tmt.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskSchedulerServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private TaskHistoryRepository taskHistoryRepository;

    @InjectMocks
    private TaskSchedulerService taskSchedulerService;

    @Test
    void checkStaleTasks_ShouldProcessTodoAndInProgressTasksOnly() {
        Task todoTask = new Task();
        todoTask.setId(1L);
        todoTask.setTitle("TODO task");
        todoTask.setStatus(Status.TODO);

        Task inProgressTask = new Task();
        inProgressTask.setId(2L);
        inProgressTask.setTitle("In Progress task");
        inProgressTask.setStatus(Status.IN_PROGRESS);

        Task doneTask = new Task();
        doneTask.setId(3L);
        doneTask.setTitle("Done task");
        doneTask.setStatus(Status.DONE);

        when(taskHistoryRepository.findTaskIdsWithNoStatusChangeSince(any(LocalDateTime.class)))
                .thenReturn(List.of(1L, 2L, 3L));
        when(taskRepository.findAllById(List.of(1L, 2L, 3L)))
                .thenReturn(List.of(todoTask, inProgressTask, doneTask));

        taskSchedulerService.checkStaleTasks();

        verify(taskHistoryRepository, times(1)).findTaskIdsWithNoStatusChangeSince(any(LocalDateTime.class));
        verify(taskRepository, times(1)).findAllById(List.of(1L, 2L, 3L));
    }

    @Test
    void checkStaleTasks_WhenNoStaleTasks_ShouldStillCallRepositoryWithEmptyList() {
        when(taskHistoryRepository.findTaskIdsWithNoStatusChangeSince(any(LocalDateTime.class)))
                .thenReturn(List.of());

        taskSchedulerService.checkStaleTasks();

        verify(taskHistoryRepository, times(1)).findTaskIdsWithNoStatusChangeSince(any(LocalDateTime.class));
        verify(taskRepository, times(1)).findAllById(List.of());
    }
}
