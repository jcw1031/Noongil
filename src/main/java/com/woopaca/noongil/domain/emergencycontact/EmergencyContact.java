package com.woopaca.noongil.domain.emergencycontact;

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
@Table(name = "emergency_contact", indexes = {
        @Index(name = "idx_contact", columnList = "contact"),
        @Index(name = "idx_user_id", columnList = "user_id"),
        @Index(name = "idx_contact_user_id", columnList = "contact_user_id")
})
public class EmergencyContact extends BaseEntity {

    @Column(nullable = false, length = 12)
    private String name;

    @Column(nullable = false, length = 12)
    private String contact;

    @Column(nullable = false)
    private boolean notification;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EmergencyContactStatus status;

    @Column(nullable = false)
    private Long userId;

    private Long contactUserId;
}
