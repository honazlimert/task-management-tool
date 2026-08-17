package com.atmosware.internship_project_tmt.repository;

import com.atmosware.internship_project_tmt.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}