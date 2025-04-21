package com.woopaca.noongil.web.dto;

import com.woopaca.noongil.domain.emergencycontact.EmergencyContact;
import lombok.Builder;

@Builder
public record EmergencyContactListResponse(Long id, String name, String contact, String status, boolean notification) {

    public static EmergencyContactListResponse from(EmergencyContact emergencyContact) {
        return EmergencyContactListResponse.builder()
                .id(emergencyContact.getId())
                .name(emergencyContact.getName())
                .contact(emergencyContact.getContact())
                .status(emergencyContact.getStatus().name())
                .notification(emergencyContact.isNotification())
                .build();
    }
}
