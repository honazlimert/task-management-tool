package com.atmosware.internship_project_tmt.repository;

import com.atmosware.internship_project_tmt.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
}