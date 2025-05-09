package com.woopaca.noongil.event;

import com.woopaca.noongil.domain.emergencycontact.EmergencyContact;
import com.woopaca.noongil.domain.emergencycontact.EmergencyContactRepository;
import com.woopaca.noongil.domain.user.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
public class UserRegistrationEventHandler {

    private final EmergencyContactRepository emergencyContactRepository;

    public UserRegistrationEventHandler(EmergencyContactRepository emergencyContactRepository) {
        this.emergencyContactRepository = emergencyContactRepository;
    }

    @Async
    @TransactionalEventListener
    public void handleUserRegistrationEvent(UserRegistrationEvent event) {
        log.info("사용자 등록 이벤트 처리: {}", event.user().getContact());
        User registeredUser = event.user();
        emergencyContactRepository.findByContact(registeredUser.getContact())
                .stream()
                .filter(EmergencyContact::isPending)
                .forEach(emergencyContact -> {
                    log.info("비상연락망 상태 변경: {}", emergencyContact.getId());
                    emergencyContact.toAccepted(registeredUser.getContact(), registeredUser.getId());
                    emergencyContactRepository.save(emergencyContact);
                });
    }
}
