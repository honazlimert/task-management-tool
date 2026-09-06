package com.atmosware.internship_project_tmt.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
public class ProjectResponse {
    private Long id;
    private String name;
    private String description;
    private LocalDateTime createdDate;
}
