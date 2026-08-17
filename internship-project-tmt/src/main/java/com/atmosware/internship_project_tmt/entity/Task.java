package com.atmosware.internship_project_tmt.entity;

import com.atmosware.internship_project_tmt.entity.enums.Priority;
import com.atmosware.internship_project_tmt.entity.enums.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "tasks")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Enumerated(EnumType.STRING)
    private Priority priority;

    private Integer storyPoint;

    // Görevin bağlı olduğu proje (N - 1 ilişkisi)
    @ManyToOne
    @JoinColumn(name = "project_id")  // (Foreign Key)

    private Project project;

    // Görevin atandığı kullanıcı (N - 1 ilişkisi)
    @ManyToOne
    @JoinColumn(name = "assignee_id")
    private User assignee;

    private String createdBy;

    private LocalDateTime createdDate;

    private LocalDateTime updatedDate;
}