package com.atmosware.internship_project_tmt.entity;

import com.atmosware.internship_project_tmt.entity.enums.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "task_histories")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long taskId;

    @Enumerated(EnumType.STRING)
    private Status oldStatus;

    @Enumerated(EnumType.STRING)
    private Status newStatus;

    private String changedBy;

    private LocalDateTime changedDate;
}