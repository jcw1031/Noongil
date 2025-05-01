package com.woopaca.noongil.domain.activity;

import com.woopaca.noongil.domain.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;

import java.time.DayOfWeek;
import java.time.LocalDate;

@Getter
@Entity
@Table(name = "activity", indexes = {
        @Index(name = "idx_user_id", columnList = "user_id"),
        @Index(name = "idx_activity_date", columnList = "activity_date"),
})
public class Activity extends BaseEntity {

    private int stepsCount;

    private LocalDate activityDate;

    private boolean holiday;

    private DayOfWeek dayOfWeek;

    private Long userId;

    protected Activity() {
    }

    @Builder
    public Activity(int stepsCount, LocalDate activityDate, boolean holiday, DayOfWeek dayOfWeek, Long userId) {
        this.stepsCount = stepsCount;
        this.activityDate = activityDate;
        this.holiday = holiday;
        this.dayOfWeek = dayOfWeek;
        this.userId = userId;
    }

    public void updateStepsCount(int stepsCount) {
        if (stepsCount != 0) {
            this.stepsCount = stepsCount;
        }
    }
}
