package com.atmosware.internship_project_tmt.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // şifreleri hash yapıya getirecek araç
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // güvenlik kuralları (filter chain)
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // API yazdığımız için CSRF korumasını şimdilik kapatıyoruz
                .authorizeHttpRequests(auth -> auth
                        // Sadece /api/auth/ altındaki adreslere HERKES girebilir (Login/Register için)
                        .requestMatchers("/api/auth/**").permitAll()
                        // Geri kalan TÜM adresler (tasks, projects) için kimlik doğrulaması zorunludur
                        .anyRequest().authenticated()
                );

        return http.build();
    }
}

