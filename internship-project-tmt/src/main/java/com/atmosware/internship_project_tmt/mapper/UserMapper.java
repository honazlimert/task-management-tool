package com.atmosware.internship_project_tmt.mapper;

import com.atmosware.internship_project_tmt.dto.request.CreateUserRequest;
import com.atmosware.internship_project_tmt.dto.request.RegisterRequest;
import com.atmosware.internship_project_tmt.dto.response.UserResponse;
import com.atmosware.internship_project_tmt.entity.User;
import org.springframework.stereotype.Component;


@Component
public class UserMapper {

    // dto to entity
    public User mapToEntity(RegisterRequest request) {
        User user = new User();
        user.setName(request.getName());
        user.setSurname(request.getSurname());
        user.setEmail(request.getEmail());
        return user;
    }

    // dto to entity
    public User mapToEntity(CreateUserRequest request) {
        User user = new User();
        user.setName(request.getName());
        user.setSurname(request.getSurname());
        user.setEmail(request.getEmail());
        user.setRole(request.getRole());
        return user;
    }

    // entity to dto
    public UserResponse mapToResponse(User user) {
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setName(user.getName());
        response.setSurname(user.getSurname());
        response.setEmail(user.getEmail());
        response.setRole(user.getRole());
        response.setCreatedDate(user.getCreatedDate());
        return response;
    }
}
