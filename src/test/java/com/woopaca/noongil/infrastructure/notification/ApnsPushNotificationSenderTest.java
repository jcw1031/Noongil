package com.woopaca.noongil.infrastructure.notification;

import com.eatthepath.pushy.apns.ApnsClient;
import com.eatthepath.pushy.apns.util.SimpleApnsPayloadBuilder;
import com.eatthepath.pushy.apns.util.SimpleApnsPushNotification;
import com.woopaca.noongil.infrastructure.apple.AppleProperties;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@Slf4j
@Disabled
@ActiveProfiles("local-develop")
@SpringBootTest
class ApnsPushNotificationSenderTest {

    @Autowired
    private AppleProperties appleProperties;

    @Autowired
    private ApnsClient apnsClient;

    @Autowired
    private ApnsClient developApnsClient;

    @Test
    void send() throws InterruptedException {
        String pushToken = "843dd0319a534d6f627962b6fae6636255cded50c7504e74d98082d79e924e1a";
        String payload = new SimpleApnsPayloadBuilder()
                .setAlertTitle("비상연락망 등록 알림")
                .setAlertBody("지찬우님이 당신을 비상연락망으로 등록했어요.")
                .setSound(SimpleApnsPayloadBuilder.DEFAULT_SOUND_FILENAME)
                .addCustomProperty("category", "emergency")
                .build();
        log.info("payload: {}", payload);

        SimpleApnsPushNotification pushNotification = new SimpleApnsPushNotification(pushToken, appleProperties.getBundleId(), payload);
        apnsClient.sendNotification(pushNotification)
                .whenComplete((response, throwable) -> {
                    log.info("response: {}", response, throwable);
                    if (!response.isAccepted()) {
                        developApnsClient.sendNotification(pushNotification)
                                .whenComplete((retryResponse, retryThrowable) ->
                                        log.info("APNs 푸시 재전송: {}", retryResponse, retryThrowable));
                    }
                });
        Thread.sleep(2_000L);
    }
}
