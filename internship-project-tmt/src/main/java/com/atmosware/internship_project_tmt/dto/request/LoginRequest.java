package com.atmosware.internship_project_tmt.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {

    @NotBlank(message = "E-posta boş bırakılamaz!")
    private String email;

    @NotBlank(message = "Şifre boş bırakılamaz!")
    private String password;
}