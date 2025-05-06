package com.woopaca.noongil.infrastructure.notification;

import com.eatthepath.pushy.apns.ApnsClient;
import com.eatthepath.pushy.apns.util.SimpleApnsPayloadBuilder;
import com.eatthepath.pushy.apns.util.SimpleApnsPushNotification;
import com.woopaca.noongil.infrastructure.apple.AppleProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Profile({"production", "develop", "local-develop"})
public class ApnsPushNotificationSender implements PushNotificationSender {

    private final ApnsClient apnsClient;
    private final AppleProperties appleProperties;

    public ApnsPushNotificationSender(ApnsClient apnsClient, AppleProperties appleProperties) {
        this.apnsClient = apnsClient;
        this.appleProperties = appleProperties;
    }

    @Override
    public void send(String pushToken, String title, String body) {
        String payload = new SimpleApnsPayloadBuilder()
                .setAlertTitle(title)
                .setAlertBody(body)
                .setSound(SimpleApnsPayloadBuilder.DEFAULT_SOUND_FILENAME)
                .build();
        SimpleApnsPushNotification pushNotification = new SimpleApnsPushNotification(pushToken, appleProperties.getBundleId(), payload);
        apnsClient.sendNotification(pushNotification)
                .whenComplete((response, throwable) -> {
                    log.info("APNs 푸시 전송 response: {}", response, throwable);
                });
    }

    public void sendBackgroundUpdatesPush(String pushToken) {
        String payload = new SimpleApnsPayloadBuilder()
                .setContentAvailable(true)
                .build();
        SimpleApnsPushNotification pushNotification = new SimpleApnsPushNotification(pushToken, appleProperties.getBundleId(), payload);
        apnsClient.sendNotification(pushNotification)
                .whenComplete((response, throwable) -> {
                    log.info("APNs 백그라운드 업데이트 푸시 전송 response: {}", response, throwable);
                });
    }
}
