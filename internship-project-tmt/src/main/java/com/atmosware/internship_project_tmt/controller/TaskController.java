package com.atmosware.internship_project_tmt.controller;

import com.atmosware.internship_project_tmt.entity.Task;
import com.atmosware.internship_project_tmt.entity.User;
import com.atmosware.internship_project_tmt.entity.enums.Priority;
import com.atmosware.internship_project_tmt.entity.enums.Status;
import com.atmosware.internship_project_tmt.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.atmosware.internship_project_tmt.dto.request.CreateTaskRequest;
import com.atmosware.internship_project_tmt.dto.response.TaskResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import org.springframework.data.domain.Page;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    // POST /api/tasks
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    // Hem USER hem de ADMIN görev oluşturabilir
    @PostMapping
    public ResponseEntity<TaskResponse> createTask(@Valid @RequestBody CreateTaskRequest request) {
        return new ResponseEntity<>(taskService.createTask(request), HttpStatus.CREATED);
        // Yeni görev oluştuğunda 201 CREATED
    }

    // GET /api/tasks
    @GetMapping
    public ResponseEntity<Page<TaskResponse>> getAllTasks(
            @RequestParam(required = false) Status status,
            @RequestParam(required = false) Priority priority,
            @RequestParam(required = false) Long projectId,
            @RequestParam(required = false) Long assigneeId,
            // "required = false" zorunluluk olmaktan çıkarttık
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        return ResponseEntity.ok(taskService.getAllTasks(status, priority, projectId, assigneeId, page, size));
    }

    // GET /api/tasks/{id}
    @GetMapping("/{id}")
    public Task getTaskById(@PathVariable Long id) {
        return taskService.getTaskById(id);
    }

    // PUT /api/tasks/{id}
    @PutMapping("/{id}")
    public Task updateTask(@PathVariable Long id, @RequestBody Task task) {
        return taskService.updateTask(id, task);
    }

    // DELETE /api/tasks/{id}
    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
    }

    // PATCH /api/tasks/{id}/status
    @PatchMapping("/{id}/status")
    public TaskResponse updateTaskStatus(@PathVariable Long id, @RequestParam Status status) {
        return taskService.updateTaskStatus(id, status);
    }

    // PATCH /api/tasks/{id}/assignee
    @PatchMapping("/{id}/assignee")
    public Task updateTaskAssignee(@PathVariable Long id, @RequestBody User assignee) {
        return taskService.updateTaskAssignee(id, assignee);
    }
}