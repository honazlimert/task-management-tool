package com.atmosware.internship_project_tmt.service;

import com.atmosware.internship_project_tmt.entity.Task;
import com.atmosware.internship_project_tmt.entity.enums.Status;
import com.atmosware.internship_project_tmt.repository.TaskHistoryRepository;
import com.atmosware.internship_project_tmt.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

@Slf4j  // loglanması amacıyla (lombok)
@Service
@RequiredArgsConstructor
public class TaskSchedulerService {

    private final TaskRepository taskRepository;
    private final TaskHistoryRepository taskHistoryRepository;
    private static final Set<Status> STALE_CHECK_STATUSES = EnumSet.of(Status.TODO, Status.IN_PROGRESS);

    @Scheduled(cron = "0 0 0 * * *")
    public void checkStaleTasks() {

        LocalDateTime sevenDaysAgo = LocalDateTime.now().minusDays(7);

        // son status değişikliği 7 günden eski olan task id'leri (status ne olursa olsun)
        List<Long> staleTaskIds = taskHistoryRepository.findTaskIdsWithNoStatusChangeSince(sevenDaysAgo);

        // sadece TODO veya IN_PROGRESS olanları bırak; DONE olanlar hariç tutulur
        List<Task> staleTasks = taskRepository.findAllById(staleTaskIds).stream()
                .filter(t -> STALE_CHECK_STATUSES.contains(t.getStatus()))
                .toList();

        if (!staleTasks.isEmpty()) {
            log.warn("DİKKAT! 7 gündür ilerlemeyen {} adet görev bulundu!", staleTasks.size());
            for (Task task : staleTasks) {
                // hangi durumda takıldığını da logluyoruz (TODO mu IN_PROGRESS mi)
                log.info("Geciken Görev - ID: {}, Başlık: {}, Durum: {}",
                        task.getId(), task.getTitle(), task.getStatus());
            }
        }
    }
}