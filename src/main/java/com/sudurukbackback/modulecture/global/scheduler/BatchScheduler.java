package com.sudurukbackback.modulecture.global.scheduler;

import com.sudurukbackback.modulecture.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class BatchScheduler {

    private final UserService userService;

    @Scheduled(cron = "0 0 * * * *")
    public void batchDeactivateAccounts() {

        log.info("계정 탈퇴 Batch scheduler 작동 시작");
        userService.batchDeactivateAccounts();
        log.info("계정 탈퇴 Batch scheduler 작동 완료");
    }
}
