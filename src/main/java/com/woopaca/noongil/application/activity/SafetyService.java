package com.woopaca.noongil.application.activity;

import com.woopaca.noongil.application.auth.AuthenticatedUserHolder;
import com.woopaca.noongil.domain.safety.Safety;
import com.woopaca.noongil.domain.safety.SafetyRepository;
import com.woopaca.noongil.domain.safety.SafetyStatus;
import com.woopaca.noongil.domain.user.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class SafetyService {

    private static final double THRESHOLD = 0.7;
    private static final int SAFETY_RESPONSE_LIMIT_DURATION = 6;

    private final SafetyRepository safetyRepository;
    private final SafetyNotificationSender safetyNotificationSender;
    private final SafetyStatusChanger safetyStatusChanger;

    public SafetyService(SafetyRepository safetyRepository, SafetyNotificationSender safetyNotificationSender, SafetyStatusChanger safetyStatusChanger) {
        this.safetyRepository = safetyRepository;
        this.safetyNotificationSender = safetyNotificationSender;
        this.safetyStatusChanger = safetyStatusChanger;
    }

    @Transactional
    public void checkSafety(double inferenceResult) {
        User authenticatedUser = AuthenticatedUserHolder.getAuthenticatedUser();
        if (inferenceResult < THRESHOLD) {
            Safety cautionSafety = Safety.caution(authenticatedUser.getId());
            safetyRepository.save(cautionSafety);
            safetyNotificationSender.sendSafetyNotification(authenticatedUser);
        }
    }

    public void automaticChangeSafetyStatus() {
        LocalDateTime currentDateTime = LocalDateTime.now();
        LocalDateTime startOfToday = LocalDate.now()
                .atStartOfDay();
        safetyRepository.findByUpdatedAtIsGreaterThanEqual(startOfToday)
                .parallelStream()
                .forEach(safety -> {
                    SafetyStatus status = safety.getStatus();
                    LocalDateTime updatedAt = safety.getUpdatedAt();
                    Duration duration = Duration.between(currentDateTime, updatedAt);
                    if (duration.toHours() >= SAFETY_RESPONSE_LIMIT_DURATION) {
                        switch (status) {
                            case CAUTION -> safetyStatusChanger.changeToWarning(safety);
                            case WARNING -> safetyStatusChanger.changeToDanger(safety);
                        }
                    }
                });
    }
}
