package com.woopaca.noongil.scheduler;

import com.woopaca.noongil.application.activity.SafetyService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class EmergencyNotificationScheduler {

    private final SafetyService safetyService;

    public EmergencyNotificationScheduler(SafetyService safetyService) {
        this.safetyService = safetyService;
    }

    @Scheduled(cron = "30 */3 * * * ?")
    public void sendEmergencyNotification() {
        safetyService.automaticChangeSafetyStatus();
    }
}
