package com.atmosware.internship_project_tmt.service;

import com.atmosware.internship_project_tmt.dto.request.RegisterRequest;
import com.atmosware.internship_project_tmt.dto.request.LoginRequest;
import com.atmosware.internship_project_tmt.entity.User;
import com.atmosware.internship_project_tmt.entity.enums.Role;
import com.atmosware.internship_project_tmt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder; // SecurityConfig'de oluşturduğumuz şifreleyici

    // register
    public String register(RegisterRequest request) {

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Bu e-posta adresi zaten kullanılıyor!");
        }

        User user = new User();
        user.setName(request.getName());
        user.setSurname(request.getSurname());
        user.setEmail(request.getEmail());

        user.setPassword(passwordEncoder.encode(request.getPassword())); // hashing
        user.setRole(Role.USER); // varsayılan rol "user"

        userRepository.save(user);

        return "Kullanıcı başarıyla kaydedildi!";
    }

    // login
    public String login(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı!"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Hatalı şifre!");
        }

        // buraya JWT Token eklenecek
        return "Giriş başarılı! Hoş geldiniz.";
    }
}