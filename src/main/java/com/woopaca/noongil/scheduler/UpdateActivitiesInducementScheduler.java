package com.woopaca.noongil.scheduler;

import com.woopaca.noongil.domain.user.AccountStatus;
import com.woopaca.noongil.domain.user.User;
import com.woopaca.noongil.domain.user.UserRepository;
import com.woopaca.noongil.infrastructure.notification.ApnsPushNotificationSender;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Profile({"production", "develop", "local-develop"})
@Component
public class UpdateActivitiesInducementScheduler {

    private final ApnsPushNotificationSender apnsPushNotificationSender;
    private final UserRepository userRepository;

    public UpdateActivitiesInducementScheduler(ApnsPushNotificationSender apnsPushNotificationSender, UserRepository userRepository) {
        this.apnsPushNotificationSender = apnsPushNotificationSender;
        this.userRepository = userRepository;
    }

    @Scheduled(cron = "0 0 8 * * *")
    public void induceBackgroundUpdateActivities() {
        userRepository.findPushTokensByStatus(AccountStatus.ACTIVE)
                .parallelStream()
                .filter(Objects::nonNull)
                .forEach(apnsPushNotificationSender::sendBackgroundUpdatesPush);
    }

    @Scheduled(cron = "0 0 9 * * *")
    public void induceForegroundUpdateActivities() {
        userRepository.findByStatus(AccountStatus.ACTIVE)
                .parallelStream()
                .filter(User::isPushNotification)
                .map(User::getPushToken)
                .forEach(pushToken -> apnsPushNotificationSender
                        .send(pushToken, "오늘도 힘찬 하루를 시작해볼까요?", "눈길이 항상 당신과 함께해요 😊"));
    }
}
