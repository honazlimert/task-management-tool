package com.atmosware.internship_project_tmt.dto.request;

import com.atmosware.internship_project_tmt.entity.enums.Priority;
import com.atmosware.internship_project_tmt.validation.ValidStoryPoint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
public class CreateTaskRequest {

    @NotBlank(message = "Task title boş bırakılamaz.")
    private String title;

    private String description;

    @NotNull(message = "Task priority boş bırakılamaz.")
    private Priority priority;

    @NotNull(message = "Task story point boş bırakılamaz.")
    @ValidStoryPoint
    private Integer storyPoint;

    @NotNull(message = "Task project boş bırakılamaz.")
    private Long projectId;

    private Long assigneeId;

    // task id'yi db oluşturacak
    // createdDate sistem oluşturacak
    // varsayılan task status todo olacak
}