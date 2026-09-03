package com.atmosware.internship_project_tmt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling // zamanlanmış görevleri aktif eder
@SpringBootApplication
public class InternshipProjectTmtApplication {

	static void main(String[] args) {
		SpringApplication.run(InternshipProjectTmtApplication.class, args);
	}

}
