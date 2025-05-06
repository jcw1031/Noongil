package com.woopaca.noongil.application.activity;

import com.woopaca.noongil.domain.emergencycontact.EmergencyContact;
import com.woopaca.noongil.domain.emergencycontact.EmergencyContactRepository;
import com.woopaca.noongil.domain.safety.Safety;
import com.woopaca.noongil.domain.safety.SafetyRepository;
import com.woopaca.noongil.domain.user.User;
import com.woopaca.noongil.domain.user.UserRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Collection;

@Component
public class SafetyStatusChanger {

    private final SafetyRepository safetyRepository;
    private final UserRepository userRepository;
    private final EmergencyContactRepository emergencyContactRepository;
    private final SafetyNotificationSender safetyNotificationSender;

    public SafetyStatusChanger(SafetyRepository safetyRepository, UserRepository userRepository, EmergencyContactRepository emergencyContactRepository, SafetyNotificationSender safetyNotificationSender) {
        this.safetyRepository = safetyRepository;
        this.userRepository = userRepository;
        this.emergencyContactRepository = emergencyContactRepository;
        this.safetyNotificationSender = safetyNotificationSender;
    }

    @Transactional
    public void changeToWarning(Safety safety) {
        safety.toWarning();
        safetyRepository.save(safety);
        Collection<EmergencyContact> emergencyContacts = emergencyContactRepository.findByUserId(safety.getUserId());
        if (CollectionUtils.isEmpty(emergencyContacts)) {
            changeToDanger(safety);
        }

        User user = userRepository.findById(safety.getUserId())
                .orElseThrow();
        emergencyContacts.stream()
                .filter(EmergencyContact::isNotification)
                .map(EmergencyContact::getContactUserId)
                .map(userRepository::findById)
                .forEach(contactUserId -> contactUserId.ifPresent(contactUser ->
                        safetyNotificationSender.sendContactNotification(contactUser, user)));
    }

    @Transactional
    public void changeToDanger(Safety safety) {
        safety.toDanger();
        safetyRepository.save(safety);
        // TODO: 눈길 CS 센터 연락
    }

    @Transactional
    public void changeToComplete(Safety safety) {
        safety.toComplete();
        safetyRepository.save(safety);
    }
}
