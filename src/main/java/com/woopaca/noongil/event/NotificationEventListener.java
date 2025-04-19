package com.woopaca.noongil.event;

import com.woopaca.noongil.infrastructure.notification.PushNotificationSender;
import com.woopaca.noongil.infrastructure.notification.SmsSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
public class NotificationEventListener {

    private final SmsSender smsSender;
    private final PushNotificationSender pushNotificationSender;

    public NotificationEventListener(SmsSender smsSender, PushNotificationSender pushNotificationSender) {
        this.smsSender = smsSender;
        this.pushNotificationSender = pushNotificationSender;
    }

    @Async
    @TransactionalEventListener
    public void handleRegisterEmergencyContactEvent(RegisterEmergencyContactEvent event) {
        log.info("비상연락망 등록 이벤트 처리");
        smsSender.sendSms();
    }
}
