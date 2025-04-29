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
        String pushToken = "e3da402bd00d5f9189783d2dcbc66f832fae3785d58961852e54c62b62968e3b";
        String payload = new SimpleApnsPayloadBuilder()
                .setAlertTitle("테스트")
                .setAlertBody("ㅎㅇ")
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
