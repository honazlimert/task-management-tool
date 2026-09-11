package com.atmosware.internship_project_tmt.dto.request;

import com.atmosware.internship_project_tmt.entity.enums.Priority;
import com.atmosware.internship_project_tmt.validation.ValidStoryPoint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateTaskRequest {

    @NotBlank(message = "Task title boş bırakılamaz.")
    private String title;

    private String description;

    @NotNull(message = "Task priority boş bırakılamaz.")
    private Priority priority;

    @NotNull(message = "Task story point boş bırakılamaz.")
    @ValidStoryPoint
    private Integer storyPoint;
}