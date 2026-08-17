package com.atmosware.internship_project_tmt.service;

import com.atmosware.internship_project_tmt.entity.User;
import com.atmosware.internship_project_tmt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor // (Lombok)
// Dependency Injection
// "Bu sınıftaki final olarak işaretlenmiş her şey bir bağımlılıktır
//  Proje başlarken bunları bul ve bu sınıfın içine enjekte et."
public class UserService {

    private final UserRepository userRepository;


    public User createUser(User user) {
        return userRepository.save(user);
    }


    public List<User> getAllUsers() {
        return userRepository.findAll();
    }


    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }


    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}