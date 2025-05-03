package com.woopaca.noongil.domain.safety;

import com.woopaca.noongil.domain.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;

@Getter
@Entity
@Table(name = "safety", indexes = {
        @Index(name = "idx_user_id", columnList = "user_id"),
        @Index(name = "idx_updated_at", columnList = "updated_at"),
})
public class Safety extends BaseEntity {

    @Enumerated(EnumType.STRING)
    private SafetyStatus status;

    private Long userId;

    protected Safety() {
    }

    @Builder
    public Safety(SafetyStatus status, Long userId) {
        this.status = status;
        this.userId = userId;
    }

    public static Safety caution(Long userId) {
        return Safety.builder()
                .status(SafetyStatus.CAUTION)
                .userId(userId)
                .build();
    }

    public void toWarning() {
        if (status.ordinal() < SafetyStatus.WARNING.ordinal()) {
            this.status = SafetyStatus.WARNING;
        }
    }

    public void toDanger() {
        if (status.ordinal() < SafetyStatus.DANGER.ordinal()) {
            this.status = SafetyStatus.DANGER;
        }
    }

    public void toComplete() {
        if (status.ordinal() < SafetyStatus.COMPLETE.ordinal()) {
            this.status = SafetyStatus.COMPLETE;
        }
    }
}
