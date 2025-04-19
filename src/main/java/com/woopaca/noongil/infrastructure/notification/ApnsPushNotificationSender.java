package com.woopaca.noongil.infrastructure.notification;

import com.eatthepath.pushy.apns.ApnsClient;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("production")
public class ApnsPushNotificationSender implements PushNotificationSender {

    private final ApnsClient apnsClient;

    public ApnsPushNotificationSender(ApnsClient apnsClient) {
        this.apnsClient = apnsClient;
    }
}
