package com.atmosware.internship_project_tmt.controller;

import com.atmosware.internship_project_tmt.entity.Task;
import com.atmosware.internship_project_tmt.entity.User;
import com.atmosware.internship_project_tmt.entity.enums.Status;
import com.atmosware.internship_project_tmt.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import com.atmosware.internship_project_tmt.dto.request.CreateTaskRequest;
import com.atmosware.internship_project_tmt.dto.response.TaskResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    // POST /api/tasks
    @PostMapping
    public ResponseEntity<TaskResponse> createTask(@Valid @RequestBody CreateTaskRequest request) {
        return new ResponseEntity<>(taskService.createTask(request), HttpStatus.CREATED);
        // Yeni görev oluştuğunda 201 CREATED
        // <TaskResponse> zaten belirtmistik, ici bu sebeple bos
    }

    // GET /api/tasks
    @GetMapping
    public ResponseEntity<List<TaskResponse>> getAllTasks() {
        return ResponseEntity.ok(taskService.getAllTasks());
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
    public Task updateTaskStatus(@PathVariable Long id, @RequestParam Status status) {
        return taskService.updateTaskStatus(id, status);
    }

    // PATCH /api/tasks/{id}/assignee
    @PatchMapping("/{id}/assignee")
    public Task updateTaskAssignee(@PathVariable Long id, @RequestBody User assignee) {
        return taskService.updateTaskAssignee(id, assignee);
    }
}