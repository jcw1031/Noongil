package com.woopaca.noongil.event;

import com.woopaca.noongil.infrastructure.notification.PushNotificationSender;
import com.woopaca.noongil.infrastructure.sms.SmsSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
public class NotificationEventHandler {

    private static final String REGISTER_EMERGENCY_CONTACT_MESSAGE = """
            🔔 눈길 알림
            {name}님이 당신을 비상연락망으로 등록했어요.
            '눈길'과 함께 {name}님의 하루를 지켜주세요.
            
            👉 앱으로 바로 확인하기: {url}
            """;

    private final SmsSender smsSender;
    private final PushNotificationSender pushNotificationSender;

    public NotificationEventHandler(SmsSender smsSender, PushNotificationSender pushNotificationSender) {
        this.smsSender = smsSender;
        this.pushNotificationSender = pushNotificationSender;
    }

    @Async
    @TransactionalEventListener
    public void handleRegisterEmergencyContactEvent(RegisterEmergencyContactEvent event) {
        log.info("비상연락망 등록 이벤트 처리");
        String message = REGISTER_EMERGENCY_CONTACT_MESSAGE
                .replace("{name}", event.name())
                .replace("{url}", "https://naver.com");
        smsSender.send(event.contact(), message);
    }
}
