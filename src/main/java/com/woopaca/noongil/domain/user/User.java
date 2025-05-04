package com.woopaca.noongil.domain.user;

import com.woopaca.noongil.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import org.springframework.util.StringUtils;

@Getter
@Entity
@Table(name = "user", indexes = {
        @Index(name = "uidx_email", columnList = "email", unique = true),
        @Index(name = "uidx_identifier", columnList = "identifier", unique = true),
        @Index(name = "uidx_contact", columnList = "contact", unique = true)
})
public class User extends BaseEntity {

    @Column(nullable = false, length = 128)
    private String email;

    @Column(nullable = false, length = 12)
    private String name;

    @Column(nullable = false, length = 48, unique = true)
    private String identifier;

    @Column(nullable = false, length = 12)
    private String contact;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AccountStatus status;

    @Column(length = 1024)
    private String pushToken;

    private boolean pushNotification;

    private boolean smsNotification;

    protected User() {
    }

    @Builder
    public User(String email, String name, String identifier, String contact, AccountStatus status, String pushToken, boolean pushNotification) {
        this.email = email;
        this.name = name;
        this.identifier = identifier;
        this.contact = contact;
        this.status = status;
        this.pushToken = pushToken;
        this.pushNotification = pushNotification;
    }

    public static User signUp(String identifier, String name, String email) {
        if (!StringUtils.hasText(name) || !StringUtils.hasText(email)) {
            throw new IllegalArgumentException("name과 email 필수입니다. name: " + name + ", email: " + email);
        }
        return User.builder()
                .name(name)
                .email(email)
                .identifier(identifier)
                .status(AccountStatus.PENDING)
                .build();
    }

    public void updateContact(String contact) {
        if (StringUtils.hasText(this.contact)) {
            throw new IllegalStateException("이미 연락처가 등록되어 있습니다.");
        }

        this.contact = contact;
        this.status = AccountStatus.ACTIVE;
        this.smsNotification = true;
    }

    public void updatePushToken(String pushToken) {
        if (StringUtils.hasText(this.pushToken)) {
            throw new IllegalStateException("푸시 토큰이 이미 등록되어 있습니다.");
        }

        this.pushToken = pushToken;
        this.pushNotification = true;
    }

    public void updatePushConsent(Boolean push) {
        if (push != null) {
            this.pushNotification = push;
        }
    }

    public void updateSmsConsent(Boolean sms) {
        if (sms != null) {
            this.smsNotification = sms;
        }
    }
}
