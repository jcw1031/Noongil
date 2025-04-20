package com.woopaca.noongil.event;

import com.woopaca.noongil.domain.emergencycontact.EmergencyContact;
import com.woopaca.noongil.domain.emergencycontact.EmergencyContactRepository;
import com.woopaca.noongil.domain.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;

@Slf4j
@Component
public class UserEventListener {

    private final UserRepository userRepository;
    private final EmergencyContactRepository emergencyContactRepository;

    public UserEventListener(UserRepository userRepository, EmergencyContactRepository emergencyContactRepository) {
        this.userRepository = userRepository;
        this.emergencyContactRepository = emergencyContactRepository;
    }

    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener
    public void handleRegisterEmergencyContactEvent(RegisterContactEvent event) {
        log.info("사용자 연락처 등록 이벤트 처리");
        Long contactUserId = event.userId();
        String contact = event.contact();
        emergencyContactRepository.findByContact(contact)
                .stream()
                .filter(EmergencyContact::isPending)
                .forEach(emergencyContact -> {
                    userRepository.findById(emergencyContact.getUserId())
                            .ifPresent(user -> {
                                emergencyContact.toAccepted(contact, contactUserId);
                                EmergencyContact otherEmergencyContact = EmergencyContact
                                        .accepted(user.getName(), user.getContact(), contactUserId, user.getId());
                                emergencyContactRepository.saveAll(List.of(emergencyContact, otherEmergencyContact));
                            });
                });
    }
}
