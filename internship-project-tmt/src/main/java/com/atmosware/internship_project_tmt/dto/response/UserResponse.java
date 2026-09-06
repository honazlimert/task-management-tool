package com.atmosware.internship_project_tmt.dto.response;


import com.atmosware.internship_project_tmt.entity.enums.Role;

import lombok.*;

import java.time.LocalDateTime;


@Getter
@Setter
public class UserResponse {
    private Long id;
    private String name;
    private String surname;
    private String email;
    private Role role;
    private LocalDateTime createdDate;
}
