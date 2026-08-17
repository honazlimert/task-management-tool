package com.atmosware.internship_project_tmt.repository;

import com.atmosware.internship_project_tmt.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long> {
}

