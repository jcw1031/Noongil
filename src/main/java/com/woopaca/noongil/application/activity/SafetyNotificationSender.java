package com.woopaca.noongil.application.activity;

import com.woopaca.noongil.domain.user.User;
import com.woopaca.noongil.infrastructure.notification.PushNotificationSender;
import org.springframework.stereotype.Component;

@Component
public class SafetyNotificationSender {

    private static final String SAFETY_NOTIFICATION_TITLE = "⚠️ 눈길 경고";
    private static final String SAFETY_NOTIFICATION_BODY = "요즘 활동량이 줄었어요. 괜찮으신가요?";
    private static final String CONTACT_NOTIFICATION_TITLE = "🚨 눈길 주의";
    private static final String CONTACT_NOTIFICATION_BODY = "%s님의 상태가 주의 상태입니다. 연락 부탁드려요!";

    private final PushNotificationSender pushNotificationSender;

    public SafetyNotificationSender(PushNotificationSender pushNotificationSender) {
        this.pushNotificationSender = pushNotificationSender;
    }

    public void sendSafetyNotification(User user) {
        if (user.isPushNotification()) {
            pushNotificationSender.send(user.getPushToken(), SAFETY_NOTIFICATION_TITLE, SAFETY_NOTIFICATION_BODY);
        }
    }

    public void sendContactNotification(User contactUser, User user) {
        if (contactUser.isPushNotification()) {
            pushNotificationSender.send(contactUser.getPushToken(),
                    CONTACT_NOTIFICATION_TITLE, String.format(CONTACT_NOTIFICATION_BODY, user.getName()));
        }
    }
}
