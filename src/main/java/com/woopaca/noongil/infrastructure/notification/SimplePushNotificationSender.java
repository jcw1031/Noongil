package com.woopaca.noongil.infrastructure.notification;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("local")
@Component
public class SimplePushNotificationSender implements PushNotificationSender {

    @Override
    public void send(String pushToken, String title, String body) {
    }
}
