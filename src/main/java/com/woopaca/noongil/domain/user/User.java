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

@Getter
@Entity
@Table(name = "user", indexes = {
        @Index(name = "uidx_email", columnList = "email", unique = true),
        @Index(name = "uidx_contact", columnList = "contact", unique = true)
})
public class User extends BaseEntity {

    @Column(nullable = false, length = 128)
    private String email;

    @Column(nullable = false, length = 12)
    private String name;

    @Column(nullable = false, length = 12)
    private String contact;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AccountStatus status;

    @Column(length = 1024)
    private String pushToken;

    protected User() {
    }

    @Builder
    public User(String email, String name, String contact, AccountStatus status, String pushToken) {
        this.email = email;
        this.name = name;
        this.contact = contact;
        this.status = status;
        this.pushToken = pushToken;
    }
}
