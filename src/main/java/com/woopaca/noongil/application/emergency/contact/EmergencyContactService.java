package com.woopaca.noongil.application.emergency.contact;

import com.woopaca.noongil.application.auth.AuthenticatedUserHolder;
import com.woopaca.noongil.application.user.UserValidator;
import com.woopaca.noongil.domain.emergencycontact.EmergencyContact;
import com.woopaca.noongil.domain.emergencycontact.EmergencyContactRepository;
import com.woopaca.noongil.domain.user.User;
import com.woopaca.noongil.domain.user.UserRepository;
import com.woopaca.noongil.event.NotificationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EmergencyContactService {

    private final UserRepository userRepository;
    private final EmergencyContactRepository emergencyContactRepository;
    private final EmergencyContactValidator emergencyContactValidator;
    private final NotificationEventPublisher notificationEventPublisher;
    private final UserValidator userValidator;

    public EmergencyContactService(UserRepository userRepository, EmergencyContactRepository emergencyContactRepository, EmergencyContactValidator emergencyContactValidator, NotificationEventPublisher notificationEventPublisher, UserValidator userValidator) {
        this.userRepository = userRepository;
        this.emergencyContactRepository = emergencyContactRepository;
        this.emergencyContactValidator = emergencyContactValidator;
        this.notificationEventPublisher = notificationEventPublisher;
        this.userValidator = userValidator;
    }

    @Transactional
    public void registerEmergencyContact(String name, String contact) {
        User authenticatedUser = AuthenticatedUserHolder.getAuthenticatedUser();
        userRepository.acquireExclusiveLock(authenticatedUser.getId());
        userValidator.validateActiveUser(authenticatedUser);
        emergencyContactValidator.validateRegistration(authenticatedUser, name, contact);

        userRepository.findByContact(contact)
                .ifPresentOrElse(
                        registeredUser -> handleRegisteredUser(name, contact, authenticatedUser, registeredUser),
                        () -> handleUnregisteredUser(name, contact, authenticatedUser)
                );
    }

    private void handleRegisteredUser(String name, String contact, User authenticatedUser, User otherUser) {
        EmergencyContact emergencyContact = EmergencyContact
                .accepted(name, contact, authenticatedUser.getId(), otherUser.getId());
        emergencyContactRepository.save(emergencyContact);
    }

    private void handleUnregisteredUser(String name, String contact, User authenticatedUser) {
        EmergencyContact emergencyContact = EmergencyContact.pending(name, contact, authenticatedUser.getId());
        emergencyContactRepository.save(emergencyContact);
        notificationEventPublisher.publishRegisterEmergencyContactEvent(authenticatedUser.getName(), contact);
    }
}
