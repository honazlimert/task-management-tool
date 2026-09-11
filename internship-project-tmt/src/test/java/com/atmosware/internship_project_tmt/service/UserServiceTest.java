package com.atmosware.internship_project_tmt.service;

import com.atmosware.internship_project_tmt.dto.request.CreateUserRequest;
import com.atmosware.internship_project_tmt.dto.response.UserResponse;
import com.atmosware.internship_project_tmt.entity.User;
import com.atmosware.internship_project_tmt.entity.enums.Role;
import com.atmosware.internship_project_tmt.exception.UserNotFoundException;
import com.atmosware.internship_project_tmt.mapper.UserMapper;
import com.atmosware.internship_project_tmt.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    void createUser_Success() {
        CreateUserRequest request = new CreateUserRequest();
        request.setName("Ali");
        request.setSurname("Yılmaz");
        request.setEmail("ali@test.com");
        request.setPassword("123456");
        request.setRole(Role.USER);

        User user = new User();
        user.setName("Ali");
        user.setSurname("Yılmaz");
        user.setEmail("ali@test.com");
        user.setRole(Role.USER);

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

        when(userMapper.mapToEntity(request)).thenReturn(user);
        when(passwordEncoder.encode("123456")).thenReturn("encodedPassword");
        when(userRepository.save(user)).thenReturn(savedUser);
        when(userMapper.mapToResponse(savedUser)).thenReturn(response);

        UserResponse result = userService.createUser(request);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Ali", result.getName());
        assertEquals(Role.USER, result.getRole());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void getAllUsers_Success() {
        User user1 = new User();
        user1.setId(1L);
        user1.setName("Ali");
        user1.setRole(Role.USER);

        User user2 = new User();
        user2.setId(2L);
        user2.setName("Ayşe");
        user2.setRole(Role.ADMIN);

        UserResponse response1 = new UserResponse();
        response1.setId(1L);
        response1.setName("Ali");
        response1.setRole(Role.USER);

        UserResponse response2 = new UserResponse();
        response2.setId(2L);
        response2.setName("Ayşe");
        response2.setRole(Role.ADMIN);

        when(userRepository.findAll()).thenReturn(List.of(user1, user2));
        when(userMapper.mapToResponse(user1)).thenReturn(response1);
        when(userMapper.mapToResponse(user2)).thenReturn(response2);

        List<UserResponse> result = userService.getAllUsers();

        assertEquals(2, result.size());
        assertEquals("Ali", result.get(0).getName());
        assertEquals("Ayşe", result.get(1).getName());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void getUserById_Success() {
        Long userId = 1L;

        User user = new User();
        user.setId(userId);
        user.setName("Ali");
        user.setEmail("ali@test.com");
        user.setRole(Role.USER);

        UserResponse response = new UserResponse();
        response.setId(userId);
        response.setName("Ali");
        response.setEmail("ali@test.com");
        response.setRole(Role.USER);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userMapper.mapToResponse(user)).thenReturn(response);

        UserResponse result = userService.getUserById(userId);

        assertNotNull(result);
        assertEquals(userId, result.getId());
        assertEquals("Ali", result.getName());
    }

    @Test
    void getUserById_NotFound_ThrowsException() {
        Long userId = 99L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getUserById(userId));
        verify(userMapper, never()).mapToResponse(any(User.class));
    }

    @Test
    void deleteUser_Success() {
        Long userId = 1L;

        when(userRepository.existsById(userId)).thenReturn(true);

        userService.deleteUser(userId);

        verify(userRepository, times(1)).existsById(userId);
        verify(userRepository, times(1)).deleteById(userId);
    }

    @Test
    void deleteUser_NotFound_ThrowsException() {
        Long userId = 99L;

        when(userRepository.existsById(userId)).thenReturn(false);

        assertThrows(UserNotFoundException.class, () -> userService.deleteUser(userId));
        verify(userRepository, never()).deleteById(anyLong());
    }
}
