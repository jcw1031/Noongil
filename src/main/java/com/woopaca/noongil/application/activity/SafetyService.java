package com.woopaca.noongil.application.activity;

import com.woopaca.noongil.application.auth.AuthenticatedUserHolder;
import com.woopaca.noongil.domain.emergencycontact.EmergencyContactRepository;
import com.woopaca.noongil.domain.safety.Safety;
import com.woopaca.noongil.domain.safety.SafetyRepository;
import com.woopaca.noongil.domain.safety.SafetyStatus;
import com.woopaca.noongil.domain.user.User;
import com.woopaca.noongil.domain.user.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class SafetyService {

    private static final double PREDICTED_SAFETY_CAUTION = 1;
    private static final int SAFETY_RESPONSE_LIMIT_DURATION_HOURS = 6;

    private final SafetyRepository safetyRepository;
    private final SafetyNotificationSender safetyNotificationSender;
    private final SafetyStatusChanger safetyStatusChanger;
    private final UserRepository userRepository;
    private final EmergencyContactRepository emergencyContactRepository;

    public SafetyService(SafetyRepository safetyRepository, SafetyNotificationSender safetyNotificationSender, SafetyStatusChanger safetyStatusChanger, UserRepository userRepository, EmergencyContactRepository emergencyContactRepository) {
        this.safetyRepository = safetyRepository;
        this.safetyNotificationSender = safetyNotificationSender;
        this.safetyStatusChanger = safetyStatusChanger;
        this.userRepository = userRepository;
        this.emergencyContactRepository = emergencyContactRepository;
    }

    public SafetyStatus getUserSafetyStatus() {
        User authenticatedUser = AuthenticatedUserHolder.getAuthenticatedUser();
        SafetyStatus lastStatus = safetyRepository.findTopByUserIdOrderByCreatedAtDesc(authenticatedUser.getId())
                .map(Safety::getStatus)
                .orElse(SafetyStatus.SAFE);
        switch (lastStatus) {
            case CAUTION -> {
                return SafetyStatus.CAUTION;
            }
            case WARNING, DANGER -> {
                return SafetyStatus.DANGER;
            }
            default -> {
                return SafetyStatus.SAFE;
            }
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
                    if (isSafetyResponseTimeout(currentDateTime, updatedAt)) {
                        switch (status) {
                            case CAUTION -> safetyStatusChanger.changeToWarning(safety);
                            case WARNING -> safetyStatusChanger.changeToDanger(safety);
                        }
                    }
                });
    }

    private boolean isSafetyResponseTimeout(LocalDateTime currentDateTime, LocalDateTime updatedAt) {
        return Duration.between(updatedAt, currentDateTime).toHours() >= SAFETY_RESPONSE_LIMIT_DURATION_HOURS;
    }

    @Transactional
    public void checkSafety(double inferenceResult) {
        User authenticatedUser = AuthenticatedUserHolder.getAuthenticatedUser();
        LocalDateTime startOfDay = LocalDate.now()
                .atStartOfDay();
        Optional<Safety> safety = safetyRepository
                .findByUpdatedAtIsGreaterThanEqualAndUserId(startOfDay, authenticatedUser.getId());
        if (inferenceResult == PREDICTED_SAFETY_CAUTION && safety.isEmpty()) {
            Safety cautionSafety = Safety.caution(authenticatedUser.getId());
            safetyRepository.save(cautionSafety);
            safetyNotificationSender.sendSafetyNotification(authenticatedUser);
        }
    }

    public void responseSafety(String response) {
        User authenticatedUser = AuthenticatedUserHolder.getAuthenticatedUser();
        LocalDateTime startOfToday = LocalDate.now()
                .atStartOfDay();
        Safety safety = safetyRepository
                .findByUpdatedAtIsGreaterThanEqualAndUserId(startOfToday, authenticatedUser.getId())
                .orElseThrow(() -> new IllegalArgumentException("주의 상태가 아닙니다."));

        if (safety.getStatus() == SafetyStatus.CAUTION) {
            switch (response) {
                case "OK" -> safetyStatusChanger.changeToComplete(safety);
                case "HELP" -> safetyStatusChanger.changeToWarning(safety);
            }
        }
    }

    public void contactSafety(String contact) {
        User authenticatedUser = AuthenticatedUserHolder.getAuthenticatedUser();
        User contactedUser = userRepository.findByContact(contact)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 연락처입니다."));

        validateContactSafety(contactedUser, authenticatedUser);
        LocalDateTime startOfToday = LocalDate.now().atStartOfDay();
        Safety safety = safetyRepository
                .findByUpdatedAtIsGreaterThanEqualAndUserId(startOfToday, contactedUser.getId())
                .orElseThrow(() -> new IllegalArgumentException("경고 상태가 아닙니다."));

        if (safety.getStatus() == SafetyStatus.WARNING) {
            safetyStatusChanger.changeToComplete(safety);
        }
    }

    private void validateContactSafety(User contactedUser, User authenticatedUser) {
        boolean hasEmergencyContact = emergencyContactRepository.findByUserId(contactedUser.getId())
                .stream()
                .anyMatch(emergencyContact ->
                        emergencyContact.getContactUserId().equals(authenticatedUser.getId()));
        if (!hasEmergencyContact) {
            throw new IllegalArgumentException("비상연락망에 등록된 사용자가 아닙니다. \"연락했어요\" 응답이 유효하지 않습니다.");
        }
    }

    public Optional<Safety> findUserLastSafety() {
        User authenticatedUser = AuthenticatedUserHolder.getAuthenticatedUser();
        return safetyRepository.findTopByUserIdOrderByCreatedAtDesc(authenticatedUser.getId());
    }
}
