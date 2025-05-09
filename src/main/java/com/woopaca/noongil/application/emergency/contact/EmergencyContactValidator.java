package com.woopaca.noongil.application.emergency.contact;

import com.woopaca.noongil.domain.emergencycontact.EmergencyContact;
import com.woopaca.noongil.domain.emergencycontact.EmergencyContactRepository;
import com.woopaca.noongil.domain.user.User;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Objects;

@Service
public class EmergencyContactValidator {

    private static final int MAX_REGISTRABLE_CONTACTS = 2;

    private final EmergencyContactRepository emergencyContactRepository;

    public EmergencyContactValidator(EmergencyContactRepository emergencyContactRepository) {
        this.emergencyContactRepository = emergencyContactRepository;
    }

    public void validateRegistration(User user, String name, String contact) {
        Collection<EmergencyContact> emergencyContacts = emergencyContactRepository.findByUserId(user.getId());
        if (emergencyContacts.size() >= MAX_REGISTRABLE_CONTACTS) {
            throw new IllegalArgumentException(String.format("비상연락망은 최대 %d개까지 등록할 수 있습니다.", MAX_REGISTRABLE_CONTACTS));
        }

        if (emergencyContacts.stream()
                .anyMatch(emergencyContact -> emergencyContact.getContact().equals(contact))) {
            throw new IllegalArgumentException("이미 등록된 비상연락망입니다.");
        }

        if (user.getContact().equals(contact)) {
            throw new IllegalArgumentException("자신의 연락처는 비상연락망으로 등록할 수 없습니다.");
        }
    }

    public void validateChangeNotification(EmergencyContact emergencyContact, User authenticatedUser) {
        if (!Objects.equals(emergencyContact.getUserId(), authenticatedUser.getId())) {
            throw new IllegalArgumentException("비상연락망 등록자가 아닙니다.");
        }

        if (emergencyContact.isPending()) {
            throw new IllegalArgumentException("비상연락망 등록이 승인되지 않았습니다.");
        }
    }

    public void validateDeleteEmergencyContact(EmergencyContact emergencyContact, User authenticatedUser) {
        if (!Objects.equals(emergencyContact.getUserId(), authenticatedUser.getId())) {
            throw new IllegalArgumentException("비상연락망 등록자가 아닙니다.");
        }
    }
}
