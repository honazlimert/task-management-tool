package com.atmosware.internship_project_tmt.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "projects")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    private LocalDateTime createdDate;

    @CreatedDate
    @Column(updatable = false)
    private String createdBy;

    // Project 1 - N Task ilişkisi
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)  // relational databases
    // ana varlık (project) üzerinde yapılan db işlemlerini, alt varlıklara (task) otomatik olarak yansıtır
    private List<Task> tasks;
}