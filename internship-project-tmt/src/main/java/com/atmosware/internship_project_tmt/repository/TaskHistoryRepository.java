package com.atmosware.internship_project_tmt.repository;

import com.atmosware.internship_project_tmt.entity.TaskHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskHistoryRepository extends JpaRepository<TaskHistory, Long> {
}