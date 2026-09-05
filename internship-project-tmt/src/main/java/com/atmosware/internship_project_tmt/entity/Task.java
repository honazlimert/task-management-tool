package com.atmosware.internship_project_tmt.entity;

import com.atmosware.internship_project_tmt.entity.enums.Priority;
import com.atmosware.internship_project_tmt.entity.enums.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "tasks")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
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
    @JoinColumn(name = "project_id")  // yeni bir sütun ve foreign key'ler oluşturur

    private Project project;

    // Görevin atandığı kullanıcı (N - 1 ilişkisi)
    @ManyToOne
    @JoinColumn(name = "assignee_id")
    private User assignee;

    private String createdBy;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate
    private LocalDateTime updatedDate;

    // optimistic locking
    @Version
    private Long version;

}