package com.woopaca.noongil.application.activity;

import com.woopaca.noongil.adapter.holiday.PublicHolidayDeterminer;
import com.woopaca.noongil.application.auth.AuthenticatedUserHolder;
import com.woopaca.noongil.domain.activity.ActivityUpdater;
import com.woopaca.noongil.domain.user.User;
import com.woopaca.noongil.web.dto.RegisterActivitiesRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ActivityService {

    private final ActivityUpdater activityUpdater;
    private final PublicHolidayDeterminer publicHolidayDeterminer;

    public ActivityService(ActivityUpdater activityUpdater, PublicHolidayDeterminer publicHolidayDeterminer) {
        this.activityUpdater = activityUpdater;
        this.publicHolidayDeterminer = publicHolidayDeterminer;
    }

    @Transactional
    public void registerActivities(List<RegisterActivitiesRequest> requests) {
        User authenticatedUser = AuthenticatedUserHolder.getAuthenticatedUser();
        Long userId = authenticatedUser.getId();
        requests.stream()
                .map(request -> {
                    boolean holiday = publicHolidayDeterminer.isPublicHoliday(request.activityDate());
                    return request.toEntity(userId, holiday);
                })
                .forEach(activityUpdater::updateActivity);
    }
}
