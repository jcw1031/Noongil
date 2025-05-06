package com.woopaca.noongil.scheduler;

import com.woopaca.noongil.domain.activity.ActivityRepository;
import com.woopaca.noongil.infrastructure.sqs.SqsMessageSender;
import com.woopaca.noongil.infrastructure.sqs.TrainHealthModelMessage;
import com.woopaca.noongil.infrastructure.sqs.TrainHealthModelMessage.ActivityMessage;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Profile({"production", "develop", "local-develop"})
@Component
public class HealthModelTrainingScheduler {

    private final ActivityRepository activityRepository;
    private final SqsMessageSender sqsMessageSender;

    public HealthModelTrainingScheduler(ActivityRepository activityRepository, SqsMessageSender sqsMessageSender) {
        this.activityRepository = activityRepository;
        this.sqsMessageSender = sqsMessageSender;
    }

    @Scheduled(cron = "0 0 1 * * SUN")
    public void trainHealthModel() {
        LocalDate startDate = LocalDate.now()
                .minusDays(30);
        activityRepository.findRecentActivitiesUserIds(startDate)
                .parallelStream()
                .map(userId -> {
                    List<ActivityMessage> activities = activityRepository.findByUserIdAndActivityDateIsGreaterThanEqual(userId, startDate)
                            .stream()
                            .map(ActivityMessage::from)
                            .toList();
                    return new TrainHealthModelMessage(userId, activities);
                })
                .forEach(sqsMessageSender::send);
    }
}
