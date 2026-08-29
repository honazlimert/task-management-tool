package com.atmosware.internship_project_tmt.config;

import com.atmosware.internship_project_tmt.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

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

            // geçici giriş yetkisini ver
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    userEmail, null, new ArrayList<>()
            );

            SecurityContextHolder.getContext().setAuthentication(authToken);
        }

        filterChain.doFilter(request, response);
    }
}