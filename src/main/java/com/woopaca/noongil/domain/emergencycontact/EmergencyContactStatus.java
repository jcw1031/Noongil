package com.woopaca.noongil.domain.emergencycontact;

public enum EmergencyContactStatus {

    PENDING("대기"),
    ACCEPTED("수락");

    private final String description;

    EmergencyContactStatus(String description) {
        this.description = description;
    }
}
