package com.woopaca.noongil.scheduler;

import com.woopaca.noongil.application.program.ProgramUpdateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ProgramScheduler {

    private final ProgramUpdateService programUpdateService;

    public ProgramScheduler(ProgramUpdateService programUpdateService) {
        this.programUpdateService = programUpdateService;
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void updatePrograms() {
        log.info("1인가구 프로그램 업데이트 시작");
        programUpdateService.updateNewPrograms();
    }
}
