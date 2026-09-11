package com.atmosware.internship_project_tmt.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
        ReflectionTestUtils.setField(jwtService, "secretKey",
                "MDEyMzQ1Njc4OWFiY2RlZjAxMjM0NTY3ODlhYmNkZWY=");
    }

    @Test
    void generateToken_ShouldReturnTokenAndExtractEmail() {
        String email = "ali@test.com";

        String token = jwtService.generateToken(email);

        assertNotNull(token);
        assertFalse(token.isBlank());
        assertEquals(email, jwtService.extractEmail(token));
    }

    @Test
    void extractEmail_ShouldReturnCorrectEmailFromToken() {
        String email = "ayse@test.com";
        String token = jwtService.generateToken(email);

        String extractedEmail = jwtService.extractEmail(token);

        assertEquals(email, extractedEmail);
    }
}
