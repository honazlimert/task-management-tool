package com.atmosware.internship_project_tmt.dto.request;

import com.atmosware.internship_project_tmt.entity.enums.Priority;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateTaskRequest {
    private String title;
    private String description;
    private Priority priority;
    private Integer storyPoint;
    private Long projectId;
    private Long assigneeId;
    // task id'yi db oluşturacak
    // createdDate sistem oluşturacak
    // varsayılan task status todo olacak
}