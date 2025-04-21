package com.woopaca.noongil.infrastructure.notification;

import com.eatthepath.pushy.apns.ApnsClient;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile({"production", "develop", "local-develop"})
public class ApnsPushNotificationSender implements PushNotificationSender {

    private final ApnsClient apnsClient;

    public ApnsPushNotificationSender(ApnsClient apnsClient) {
        this.apnsClient = apnsClient;
    }
}
