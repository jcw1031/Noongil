package com.woopaca.noongil.domain.mlmodel;

import com.woopaca.noongil.domain.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;

@Getter
@Entity
@Table(name = "health_model", indexes = {
        @Index(name = "uidx_user_id", columnList = "user_id", unique = true),
})
public class HealthModel extends BaseEntity {

    private String modelName;

    private Long userId;

    protected HealthModel() {
    }

    @Builder
    public HealthModel(String modelName, Long userId) {
        this.modelName = modelName;
        this.userId = userId;
    }

    public void changeModelName(String modelName) {
        if (modelName != null) {
            this.modelName = modelName;
        }
    }
}
