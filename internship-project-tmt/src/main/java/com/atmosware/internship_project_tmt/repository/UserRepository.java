package com.atmosware.internship_project_tmt.repository;

import com.atmosware.internship_project_tmt.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}