package com.woopaca.noongil.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UserEventPublisher {

    private final ApplicationEventPublisher eventPublisher;

    public UserEventPublisher(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    public void publishRegisterContactEvent(Long userId, String contact) {
        log.info("사용자 연락처 등록 이벤트 발행");
        RegisterContactEvent event = new RegisterContactEvent(userId, contact);
        eventPublisher.publishEvent(event);
    }
}
