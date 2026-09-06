package com.atmosware.internship_project_tmt.config;

import jakarta.annotation.Nonnull;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component("securityAuditorAware")
public class SecurityAuditorAware implements AuditorAware<String> {

    @Override
    public @Nonnull Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Kullanıcı giriş yapmamışsa veya kimliği doğrulanmamışsa boş dön
        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
            return Optional.empty();
        }

        // JwtAuthenticationFilter içinde principal olarak e-posta adresini kaydetmiştik
        return Optional.of(authentication.getName());
    }
}