package com.atmosware.internship_project_tmt.dto.request;

import com.atmosware.internship_project_tmt.entity.enums.Priority;
import com.atmosware.internship_project_tmt.validation.ValidStoryPoint;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateTaskRequest {

    @NotBlank(message = "Task title boş bırakılamaz.")
    private String title;

    private String description;

    private Priority priority;

    @ValidStoryPoint
    private Integer storyPoint;
}