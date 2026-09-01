package com.atmosware.internship_project_tmt.config;

import com.atmosware.internship_project_tmt.entity.User;
import com.atmosware.internship_project_tmt.repository.UserRepository;
import com.atmosware.internship_project_tmt.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // request header'dan bearer token'i al
        final String authHeader = request.getHeader("Authorization");

        // token'i dogrula
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // bearer kelimesini kesip sadece asıl token'i al
        final String jwt = authHeader.substring(7);

        // tokendeki e-mail'i çöz
        final String userEmail = jwtService.extractEmail(jwt);

        // e-mail geçerliyse ve sistemde o an kimse açık değilse kullanıcıyı içeri al
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // kullanıcıyı db'den bul
            User user = userRepository.findByEmail(userEmail).orElse(null);

            if (user != null) {
                // kullanıcı rolünü uygun formata dönüştür
                // SimpleGrantedAuthority: kullanımı zorunlu Spring Security özel güvenlik nesnesi
                SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + user.getRole().name());

                // kullanıcıya rolünü ata
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userEmail, null, Collections.singletonList(authority)
                );

                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}