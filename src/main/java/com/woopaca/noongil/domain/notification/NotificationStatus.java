package com.woopaca.noongil.domain.notification;

public enum NotificationStatus {

    CREATED("생성"), // 사용자에게 알림이 발송된 상태
    WAITING("대기"), // 지인에게 알림이 발송된 상태
    COMPLETED("완료"); // 알림이 완료된 상태

    private final String description;

    NotificationStatus(String description) {
        this.description = description;
    }
}
