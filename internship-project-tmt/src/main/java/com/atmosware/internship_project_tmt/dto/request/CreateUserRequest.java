package com.atmosware.internship_project_tmt.dto.request;

import com.atmosware.internship_project_tmt.entity.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserRequest {

    @NotBlank(message = "Ad alanı boş bırakılamaz!")
    private String name;

    @NotBlank(message = "Soyad alanı boş bırakılamaz!")
    private String surname;

    @NotBlank(message = "E-posta alanı boş bırakılamaz!")
    @Email(message = "Geçerli bir e-posta adresi giriniz!")
    private String email;

    @NotBlank(message = "Şifre alanı boş bırakılamaz!")
    private String password;

    @NotNull(message = "Rol alanı boş bırakılamaz!")
    private Role role; // ADMIN veya USER
}
