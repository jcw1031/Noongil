package com.woopaca.noongil.application.emergency.contact;

import com.woopaca.noongil.application.auth.AuthenticatedUserHolder;
import com.woopaca.noongil.application.user.UserValidator;
import com.woopaca.noongil.domain.emergencycontact.EmergencyContact;
import com.woopaca.noongil.domain.emergencycontact.EmergencyContactRepository;
import com.woopaca.noongil.domain.user.User;
import com.woopaca.noongil.domain.user.UserRepository;
import com.woopaca.noongil.event.NotificationEventPublisher;
import com.woopaca.noongil.infrastructure.notification.PushNotificationSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

@Service
public class EmergencyContactService {

    private final UserRepository userRepository;
    private final EmergencyContactRepository emergencyContactRepository;
    private final EmergencyContactValidator emergencyContactValidator;
    private final NotificationEventPublisher notificationEventPublisher;
    private final UserValidator userValidator;
    private final PushNotificationSender pushNotificationSender;

    public EmergencyContactService(UserRepository userRepository, EmergencyContactRepository emergencyContactRepository, EmergencyContactValidator emergencyContactValidator, NotificationEventPublisher notificationEventPublisher, UserValidator userValidator, PushNotificationSender pushNotificationSender) {
        this.userRepository = userRepository;
        this.emergencyContactRepository = emergencyContactRepository;
        this.emergencyContactValidator = emergencyContactValidator;
        this.notificationEventPublisher = notificationEventPublisher;
        this.userValidator = userValidator;
        this.pushNotificationSender = pushNotificationSender;
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
        pushNotificationSender.send(otherUser.getPushToken(), "비상연락망 등록 알림",
                String.format("%s님이 당신을 비상연락망으로 등록했어요.", authenticatedUser.getName()));
    }

    private void handleUnregisteredUser(String name, String contact, User authenticatedUser) {
        EmergencyContact emergencyContact = EmergencyContact.pending(name, contact, authenticatedUser.getId());
        emergencyContactRepository.save(emergencyContact);
        notificationEventPublisher.publishRegisterEmergencyContactEvent(authenticatedUser.getName(), contact);
    }

    public Collection<EmergencyContact> findRegisteredEmergencyContacts() {
        User authenticatedUser = AuthenticatedUserHolder.getAuthenticatedUser();
        return emergencyContactRepository.findByUserId(authenticatedUser.getId());
    }

    public void changeNotification(Long contactId, boolean notification) {
        User authenticatedUser = AuthenticatedUserHolder.getAuthenticatedUser();
        userValidator.validateActiveUser(authenticatedUser);

        EmergencyContact emergencyContact = emergencyContactRepository.findById(contactId)
                .orElseThrow(() -> new IllegalArgumentException("비상연락망이 존재하지 않습니다. contactId: " + contactId));
        emergencyContactValidator.validateChangeNotification(emergencyContact, authenticatedUser);

        emergencyContact.changeNotification(notification);
        emergencyContactRepository.save(emergencyContact);
    }

    public void deleteEmergencyContact(Long contactId) {
        User authenticatedUser = AuthenticatedUserHolder.getAuthenticatedUser();
        userRepository.acquireExclusiveLock(authenticatedUser.getId());
        userValidator.validateActiveUser(authenticatedUser);

        EmergencyContact emergencyContact = emergencyContactRepository.findById(contactId)
                .orElseThrow(() -> new IllegalArgumentException("비상연락망이 존재하지 않습니다. contactId: " + contactId));
        emergencyContactValidator.validateDeleteEmergencyContact(emergencyContact, authenticatedUser);

        emergencyContactRepository.delete(emergencyContact);
    }
}
