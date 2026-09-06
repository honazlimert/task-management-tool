package com.atmosware.internship_project_tmt.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
public class CreateProjectRequest {

    @NotBlank(message = "Proje adı boş bırakılamaz!")
    private String name;

    private String description;
}
