package com.woopaca.noongil.infrastructure.sqs;

import com.woopaca.noongil.domain.activity.Activity;
import lombok.Builder;

import java.time.LocalDate;
import java.util.List;

public record TrainHealthModelMessage(Long userId, List<ActivityMessage> activities)
        implements SqsMessageBody {

    @Builder
    public record ActivityMessage(LocalDate activityDate, int stepsCount, int dayOfWeek) {

        public static ActivityMessage from(Activity activity) {
            return ActivityMessage.builder()
                    .activityDate(activity.getActivityDate())
                    .stepsCount(activity.getStepsCount())
                    .dayOfWeek((activity.getDayOfWeek().ordinal() + 1) % 7)
                    .build();
        }
    }
}
