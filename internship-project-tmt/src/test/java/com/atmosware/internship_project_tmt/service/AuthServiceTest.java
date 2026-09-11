package com.atmosware.internship_project_tmt.service;

import com.atmosware.internship_project_tmt.dto.request.LoginRequest;
import com.atmosware.internship_project_tmt.dto.request.RegisterRequest;
import com.atmosware.internship_project_tmt.dto.response.LoginResponse;
import com.atmosware.internship_project_tmt.dto.response.UserResponse;
import com.atmosware.internship_project_tmt.entity.User;
import com.atmosware.internship_project_tmt.entity.enums.Role;
import com.atmosware.internship_project_tmt.exception.BusinessException;
import com.atmosware.internship_project_tmt.mapper.UserMapper;
import com.atmosware.internship_project_tmt.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private AuthService authService;

    @Test
    void register_Success() {
        RegisterRequest request = new RegisterRequest();
        request.setName("Ali");
        request.setSurname("Yılmaz");
        request.setEmail("ali@test.com");
        request.setPassword("123456");

        User user = new User();
        user.setName("Ali");
        user.setSurname("Yılmaz");
        user.setEmail("ali@test.com");

        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setName("Ali");
        savedUser.setSurname("Yılmaz");
        savedUser.setEmail("ali@test.com");
        savedUser.setRole(Role.USER);

        UserResponse response = new UserResponse();
        response.setId(1L);
        response.setName("Ali");
        response.setSurname("Yılmaz");
        response.setEmail("ali@test.com");
        response.setRole(Role.USER);

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());
        when(userMapper.mapToEntity(request)).thenReturn(user);
        when(passwordEncoder.encode("123456")).thenReturn("encodedPassword");
        when(userRepository.save(user)).thenReturn(savedUser);
        when(userMapper.mapToResponse(savedUser)).thenReturn(response);

        UserResponse result = authService.register(request);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("ali@test.com", result.getEmail());
        assertEquals(Role.USER, result.getRole());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void register_WhenEmailAlreadyExists_ThrowsException() {
        RegisterRequest request = new RegisterRequest();
        request.setEmail("ali@test.com");
        request.setPassword("123456");

        when(userRepository.findByEmail("ali@test.com")).thenReturn(Optional.of(new User()));

        BusinessException exception = assertThrows(BusinessException.class,
                () -> authService.register(request));

        assertEquals("Bu e-posta adresi zaten kullanılıyor!", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void login_Success() {
        LoginRequest request = new LoginRequest();
        request.setEmail("ali@test.com");
        request.setPassword("123456");

        User user = new User();
        user.setEmail("ali@test.com");
        user.setPassword("encodedPassword");

        when(userRepository.findByEmail("ali@test.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("123456", "encodedPassword")).thenReturn(true);
        when(jwtService.generateToken("ali@test.com")).thenReturn("jwt-token");

        LoginResponse result = authService.login(request);

        assertNotNull(result);
        assertEquals("jwt-token", result.getToken());
        verify(jwtService, times(1)).generateToken("ali@test.com");
    }

    @Test
    void login_WhenUserNotFound_ThrowsException() {
        LoginRequest request = new LoginRequest();
        request.setEmail("missing@test.com");
        request.setPassword("123456");

        when(userRepository.findByEmail("missing@test.com")).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class,
                () -> authService.login(request));

        assertEquals("Kullanıcı bulunamadı!", exception.getMessage());
        verify(jwtService, never()).generateToken(anyString());
    }

    @Test
    void login_WhenPasswordIsWrong_ThrowsException() {
        LoginRequest request = new LoginRequest();
        request.setEmail("ali@test.com");
        request.setPassword("wrongPassword");

        User user = new User();
        user.setEmail("ali@test.com");
        user.setPassword("encodedPassword");

        when(userRepository.findByEmail("ali@test.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrongPassword", "encodedPassword")).thenReturn(false);

        BusinessException exception = assertThrows(BusinessException.class,
                () -> authService.login(request));

        assertEquals("Hatalı şifre!", exception.getMessage());
        verify(jwtService, never()).generateToken(anyString());
    }
}
