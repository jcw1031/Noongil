package com.woopaca.noongil.domain.emergencycontact;

import lombok.Getter;

@Getter
public enum EmergencyContactStatus {

    PENDING("대기"),
    ACCEPTED("수락");

    private final String expression;

    EmergencyContactStatus(String expression) {
        this.expression = expression;
    }
}
