package com.atmosware.internship_project_tmt.service;

import com.atmosware.internship_project_tmt.dto.request.RegisterRequest;
import com.atmosware.internship_project_tmt.dto.request.LoginRequest;
import com.atmosware.internship_project_tmt.dto.response.UserResponse;
import com.atmosware.internship_project_tmt.entity.User;
import com.atmosware.internship_project_tmt.entity.enums.Role;
import com.atmosware.internship_project_tmt.exception.BusinessException;
import com.atmosware.internship_project_tmt.mapper.UserMapper;
import com.atmosware.internship_project_tmt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UserMapper userMapper;

    // register
    public UserResponse register(RegisterRequest request) {

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new BusinessException("Bu e-posta adresi zaten kullanılıyor!");
        }

        User user = userMapper.mapToEntity(request);
        user.setPassword(passwordEncoder.encode(request.getPassword())); // hashing
        user.setRole(Role.USER); // varsayılan rol "user"

        User savedUser = userRepository.save(user);
        return userMapper.mapToResponse(savedUser);
    }

    // login
    public String login(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BusinessException("Kullanıcı bulunamadı!"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BusinessException("Hatalı şifre!");
        }

        // sifre dogruysa jwt uret ve teslim et
        return jwtService.generateToken(user.getEmail());
    }
}