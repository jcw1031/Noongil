package com.woopaca.noongil.infrastructure.notification;

import com.eatthepath.pushy.apns.ApnsClient;
import com.eatthepath.pushy.apns.util.SimpleApnsPushNotification;
import com.woopaca.noongil.infrastructure.apple.AppleProperties;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile({"production", "develop", "local-develop"})
public class ApnsPushNotificationSender implements PushNotificationSender {

    private final ApnsClient apnsClient;
    private final AppleProperties appleProperties;

    public ApnsPushNotificationSender(ApnsClient apnsClient, AppleProperties appleProperties) {
        this.apnsClient = apnsClient;
        this.appleProperties = appleProperties;
    }

    public void test() {
        new SimpleApnsPushNotification("pushToken", appleProperties.getClientId(), null);
    }
}
