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

    @Test
    void send() throws InterruptedException {
        String pushToken = "599324fc91d17a8315f06cabce9e8fa8e717001c427cb20cc951c7429b73ed2e";
        String payload = new SimpleApnsPayloadBuilder()
                .setAlertTitle("화이팅입니다 정훈님")
                .setAlertBody("")
                .setSound(SimpleApnsPayloadBuilder.DEFAULT_SOUND_FILENAME)
                .addCustomProperty("navigateTo", "emergency")
                .build();
        log.info("payload: {}", payload);

        SimpleApnsPushNotification pushNotification = new SimpleApnsPushNotification(pushToken, appleProperties.getBundleId(), payload);
        apnsClient.sendNotification(pushNotification)
                .whenComplete((result, error) -> {
                    if (error != null) {
                        log.error("Error sending push notification: {}", error.getMessage());
                    } else {
                        log.info("Push notification sent successfully: {}", result);
                    }
                });

        Thread.sleep(2_000L);
    }
}
