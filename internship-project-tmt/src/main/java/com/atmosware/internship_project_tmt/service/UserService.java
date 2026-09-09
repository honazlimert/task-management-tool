package com.atmosware.internship_project_tmt.service;

import com.atmosware.internship_project_tmt.dto.request.CreateUserRequest;
import com.atmosware.internship_project_tmt.dto.response.UserResponse;
import com.atmosware.internship_project_tmt.entity.User;
import com.atmosware.internship_project_tmt.exception.UserNotFoundException;
import com.atmosware.internship_project_tmt.mapper.UserMapper;
import com.atmosware.internship_project_tmt.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;


    @Transactional
    public UserResponse createUser(CreateUserRequest request) {
        User user = userMapper.mapToEntity(request);
        // Şifreyi encode edip entity'ye atıyoruz
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        User savedUser = userRepository.save(user);
        return userMapper.mapToResponse(savedUser);
    }

    @Transactional(readOnly = true)
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Kullanıcı bulunamadı!"));
        return userMapper.mapToResponse(user);
    }

    @Transactional
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException("Silinecek kullanıcı bulunamadı: " + id);
        }
        userRepository.deleteById(id);
    }
}