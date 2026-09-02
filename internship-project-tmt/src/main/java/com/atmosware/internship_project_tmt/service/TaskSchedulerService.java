package com.atmosware.internship_project_tmt.service;

import com.atmosware.internship_project_tmt.entity.Task;
import com.atmosware.internship_project_tmt.entity.enums.Status;
import com.atmosware.internship_project_tmt.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j  // loglanması amacıyla (lombok)
@Service
@RequiredArgsConstructor
public class TaskSchedulerService {

    private final TaskRepository taskRepository;

    // her gece 00:00'da çalışır
    @Scheduled(cron = "0 0 0 * * *")
    public void checkStaleTasks() {

        // 7 gün öncesini hesapla
        LocalDateTime sevenDaysAgo = LocalDateTime.now().minusDays(7);

        // 7 günden eski ve hala IN_PROGRESS olan görevleri getir
        List<Task> staleTasks = taskRepository.findByStatusAndUpdatedDateBefore(Status.IN_PROGRESS, sevenDaysAgo);

        // businnes rules (loglama)
        if (!staleTasks.isEmpty()) {
            log.warn("DİKKAT! 7 gündür ilerlemeyen {} adet görev bulundu!", staleTasks.size());
            for (Task task : staleTasks) {
                log.info("Geciken Görev - ID: {}, Başlık: {}", task.getId(), task.getTitle());
            }
        }
    }
}