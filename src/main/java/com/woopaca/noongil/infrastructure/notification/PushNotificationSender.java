package com.woopaca.noongil.infrastructure.notification;

public interface PushNotificationSender {

    void send(String pushToken, String title, String body);
}
