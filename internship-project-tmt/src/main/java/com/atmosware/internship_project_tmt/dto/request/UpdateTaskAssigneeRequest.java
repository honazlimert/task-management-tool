package com.atmosware.internship_project_tmt.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateTaskAssigneeRequest {
    @NotNull(message = "Atanacak kişinin ID'si boş olamaz!")
    private Long assigneeId;
}