package com.woopaca.noongil.web.dto;

import com.woopaca.noongil.domain.activity.Activity;
import lombok.Builder;

import java.time.DayOfWeek;
import java.time.LocalDate;

@Builder
public record ActivityListResponse(LocalDate activityDate, int stepsCount, DayOfWeek dayOfWeek) {

    public static ActivityListResponse from(Activity activity) {
        return ActivityListResponse.builder()
                .activityDate(activity.getActivityDate())
                .stepsCount(activity.getStepsCount())
                .dayOfWeek(activity.getDayOfWeek())
                .build();
    }
}
