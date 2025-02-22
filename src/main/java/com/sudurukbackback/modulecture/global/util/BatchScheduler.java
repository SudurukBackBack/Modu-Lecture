package com.sudurukbackback.modulecture.global.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class BatchScheduler {

    private final JobLauncher jobLauncher;
    private final ApplicationContext applicationContext;

    @Scheduled(cron = "0 0 0 * * *") // 매일 자정 실행
    public void runDeletePendingUsersJob() {
        try {
            // Job을 동적으로 가져옴 (Bean 로딩 문제 방지)
            Job deletePendingUsersJob = applicationContext.getBean("deletePendingUsersJob", Job.class);

            JobParameters jobParameters = new JobParametersBuilder()
                    .addLong("time", System.currentTimeMillis())
                    .toJobParameters();

            jobLauncher.run(deletePendingUsersJob, jobParameters);
        } catch (Exception e) {
            log.error("Error running batch job", e);
        }
    }
}
