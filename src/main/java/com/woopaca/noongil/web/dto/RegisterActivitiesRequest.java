package com.woopaca.noongil.web.dto;

import com.woopaca.noongil.domain.activity.Activity;

import java.time.LocalDate;

public record RegisterActivitiesRequest(LocalDate activityDate, int stepsCount) {

    public Activity toEntity(Long userId, boolean holiday) {
        return Activity.builder()
                .stepsCount(stepsCount)
                .activityDate(activityDate)
                .holiday(holiday)
                .dayOfWeek(activityDate.getDayOfWeek())
                .userId(userId)
                .build();
    }
}
