package com.woopaca.noongil.domain.notification;

import com.woopaca.noongil.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.Getter;

@Getter
@Entity
@Table(name = "emergency_notification", indexes = {
        @Index(name = "idx_updated_at", columnList = "updated_at")
})
public class EmergencyNotification extends BaseEntity {

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private NotificationStatus status;

    @Column(nullable = false)
    private Long senderId;

    @Column(nullable = false)
    private Long receiverId;
}
