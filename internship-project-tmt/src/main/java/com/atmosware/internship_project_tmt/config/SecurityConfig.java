package com.atmosware.internship_project_tmt.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity      // Web güvenliğini ve URL tabanlı yetkilendirmeyi etkinleştirir.
// HTTP requestlerin hangi rollerle erişilebileceğini belirleyen SecurityFilterChain için kullanılır.
@EnableMethodSecurity   // Metot düzeyinde (servis veya controller fonksiyonlarında) güvenliği etkinleştirir.
// @PreAuthorize, @PostAuthorize, @Secured gibi anotasyonlar ile kimin hangi metodu çağırabileceğini kontrol etmeyi sağlar.
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;

    // şifreleri hash yapıya getirecek araç
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // güvenlik kuralları (filter chain) (controller'a gitmeden önce)
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        // swagger UI ve API Docs yollarına herkesin erişmesine izin ver
                        .requestMatchers(
                                "/v3/api-docs",
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/swagger-resources/**",
                                "/webjars/**"
                        ).permitAll()
                        // auth uç noktalarına (login, register) izin ver
                        .requestMatchers("/api/auth/**").permitAll()
                        // kalan tüm istekler için kimlik doğrulaması bekle
                        .anyRequest().authenticated()
                )
                // JWT filter
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}

