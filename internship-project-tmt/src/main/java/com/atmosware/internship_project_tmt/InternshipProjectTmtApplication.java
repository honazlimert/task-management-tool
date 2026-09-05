package com.atmosware.internship_project_tmt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling // zamanlanmış görevleri aktif eder (@Scheduled)
@EnableJpaAuditing // db denetimini otomatikleştirir (@CreatedBy)
@SpringBootApplication
public class InternshipProjectTmtApplication {

	static void main(String[] args) {
		SpringApplication.run(InternshipProjectTmtApplication.class, args);
	}

}
