package com.woopaca.noongil.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class NotificationEventPublisher {

    private final ApplicationEventPublisher eventPublisher;

    public NotificationEventPublisher(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    public void publishRegisterEmergencyContactEvent(String name, String contact) {
        log.info("비상연락망 등록 이벤트 발행");
        RegisterEmergencyContactEvent event = new RegisterEmergencyContactEvent(name, contact);
        eventPublisher.publishEvent(event);
    }
}
