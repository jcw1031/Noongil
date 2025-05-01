package com.woopaca.noongil.domain.activity;

import org.springframework.stereotype.Component;

@Component
public class ActivityUpdater {

    private final ActivityRepository activityRepository;

    public ActivityUpdater(ActivityRepository activityRepository) {
        this.activityRepository = activityRepository;
    }

    public void updateActivity(Activity activity) {
        activityRepository.findByUserIdAndActivityDate(activity.getUserId(), activity.getActivityDate())
                .ifPresentOrElse(existingActivity -> {
                    existingActivity.updateStepsCount(activity.getStepsCount());
                    activityRepository.save(existingActivity);
                }, () -> {
                    activityRepository.save(activity);
                });
    }
}
