package com.atmosware.internship_project_tmt.dto.response;


import com.atmosware.internship_project_tmt.entity.enums.Priority;
import com.atmosware.internship_project_tmt.entity.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskResponse {
    private Long id;
    private String title;
    private String description;
    private Status status;
    private Priority priority;
    private Integer storyPoint;
    private String projectName; // project nesnesi yerine sadece ismi dönüyoruz
    private String assignee; // user nesnesi yerine sadece isim dönüyoruz
    private LocalDateTime createdDate;
    // dönüşümler mapper'da yapılacak
}