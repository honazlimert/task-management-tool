package com.atmosware.internship_project_tmt.mapper;

import com.atmosware.internship_project_tmt.dto.request.CreateTaskRequest;
import com.atmosware.internship_project_tmt.dto.response.TaskResponse;
import com.atmosware.internship_project_tmt.entity.Task;
import org.springframework.stereotype.Component;

@Component
// singleton pattern
// spring uygulama ömrü boyunca nesneden sadece bir adet üretir ve bellekte saklar
// yapıcı metod public olmak zorundadır
public class TaskMapper {

    // dto to entity
    public Task mapToEntity(CreateTaskRequest request) {
        Task task = new Task();
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setPriority(request.getPriority());
        task.setStoryPoint(request.getStoryPoint());
        // projectId ve assigneeId'yi kullanarak, project ve user nesnelerini service katmanında db'den alıp ekleyeceğiz.
        return task;
    }

    // entity to dto
    public TaskResponse mapToResponse(Task task) {
        TaskResponse response = new TaskResponse();
        response.setId(task.getId());
        response.setTitle(task.getTitle());
        response.setDescription(task.getDescription());
        response.setStatus(task.getStatus());
        response.setPriority(task.getPriority());
        response.setStoryPoint(task.getStoryPoint());
        response.setCreatedDate(task.getCreatedDate());

        if (task.getProject() != null) {
            response.setProjectName(task.getProject().getName());
        }

        if (task.getAssignee() != null) {
            response.setAssignee(task.getAssignee().getName() + " " + task.getAssignee().getSurname());
        }

        return response;
    }
}