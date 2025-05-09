package com.woopaca.noongil.application.user;

import com.woopaca.noongil.application.auth.AuthenticatedUserHolder;
import com.woopaca.noongil.domain.user.User;
import com.woopaca.noongil.domain.user.UserRepository;
import com.woopaca.noongil.event.UserRegistrationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    public UserService(UserRepository userRepository, ApplicationEventPublisher applicationEventPublisher) {
        this.userRepository = userRepository;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Transactional
    public void registerUserInfo(String contact) {
        userRepository.findByContact(contact)
                .ifPresent(user -> {
                    throw new IllegalArgumentException("이미 등록된 연락처입니다.");
                });

        User authenticatedUser = AuthenticatedUserHolder.getAuthenticatedUser();
        authenticatedUser.updateContact(contact);
        userRepository.save(authenticatedUser);
        applicationEventPublisher.publishEvent(new UserRegistrationEvent(authenticatedUser));
    }

    public void registerUserPushToken(String pushToken) {
        User authenticatedUser = AuthenticatedUserHolder.getAuthenticatedUser();
        authenticatedUser.updatePushToken(pushToken);
        userRepository.save(authenticatedUser);
    }

    public void updateUserConsents(Boolean push, Boolean sms) {
        User authenticatedUser = AuthenticatedUserHolder.getAuthenticatedUser();
        if (push != null) {
            authenticatedUser.updatePushConsent(push);
        }
        if (sms != null) {
            authenticatedUser.updateSmsConsent(sms);
        }
        userRepository.save(authenticatedUser);
    }
}
