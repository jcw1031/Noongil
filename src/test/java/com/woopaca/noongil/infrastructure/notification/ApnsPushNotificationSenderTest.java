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
        String pushToken = "d502176d22391aa129e05dcf1a9dfacf4586336270010a19203f97221f993098";
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
