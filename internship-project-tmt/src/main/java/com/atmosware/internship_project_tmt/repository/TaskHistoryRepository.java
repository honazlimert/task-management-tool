package com.atmosware.internship_project_tmt.repository;

import com.atmosware.internship_project_tmt.entity.TaskHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface TaskHistoryRepository extends JpaRepository<TaskHistory, Long> {

    // her task'ın son status değişim tarihini bulup, 7 günden eski olanların id'lerini döner
    @Query("SELECT th.taskId FROM TaskHistory th " +
            "WHERE th.changedDate = (SELECT MAX(th2.changedDate) FROM TaskHistory th2 WHERE th2.taskId = th.taskId) " +
            "AND th.changedDate < :cutoffDate")

    List<Long> findTaskIdsWithNoStatusChangeSince(LocalDateTime cutoffDate);
}