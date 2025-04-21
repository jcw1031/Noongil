package com.woopaca.noongil.domain.emergencycontact;

import com.woopaca.noongil.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.Builder;
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

    protected EmergencyContact() {
    }

    @Builder
    public EmergencyContact(String name, String contact, boolean notification, EmergencyContactStatus status, Long userId, Long contactUserId) {
        this.name = name;
        this.contact = contact;
        this.notification = notification;
        this.status = status;
        this.userId = userId;
        this.contactUserId = contactUserId;
    }

    public static EmergencyContact accepted(String name, String contact, Long userId, Long contactUserId) {
        return EmergencyContact.builder()
                .name(name)
                .contact(contact)
                .notification(true)
                .status(EmergencyContactStatus.ACCEPTED)
                .userId(userId)
                .contactUserId(contactUserId)
                .build();
    }

    public static EmergencyContact pending(String name, String contact, Long userId) {
        return EmergencyContact.builder()
                .name(name)
                .contact(contact)
                .notification(false)
                .status(EmergencyContactStatus.PENDING)
                .userId(userId)
                .build();
    }

    public void toAccepted(String contact, Long contactUserId) {
        if (this.contact.equals(contact)) {
            this.contactUserId = contactUserId;
            this.status = EmergencyContactStatus.ACCEPTED;
            this.notification = true;
        }
    }

    public boolean isPending() {
        return this.status == EmergencyContactStatus.PENDING;
    }

    public void changeNotification(boolean notification) {
        this.notification = notification;
    }
}
