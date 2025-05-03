package com.woopaca.noongil.scheduler;

import com.woopaca.noongil.application.activity.SafetyService;
import com.woopaca.noongil.domain.safety.SafetyRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class EmergencyNotificationScheduler {

    private final SafetyService safetyService;
    private final SafetyRepository safetyRepository;

    public EmergencyNotificationScheduler(SafetyService safetyService, SafetyRepository safetyRepository) {
        this.safetyService = safetyService;
        this.safetyRepository = safetyRepository;
    }

    @Scheduled(cron = "30 */3 * * * ?")
    public void sendEmergencyNotification() {
        safetyService.automaticChangeSafetyStatus();
    }
}
