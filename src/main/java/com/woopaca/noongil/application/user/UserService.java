package com.woopaca.noongil.application.user;

import com.woopaca.noongil.application.auth.AuthenticatedUserHolder;
import com.woopaca.noongil.domain.user.User;
import com.woopaca.noongil.domain.user.UserRepository;
import com.woopaca.noongil.event.UserEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserEventPublisher userEventPublisher;

    public UserService(UserRepository userRepository, UserEventPublisher userEventPublisher) {
        this.userRepository = userRepository;
        this.userEventPublisher = userEventPublisher;
    }

    @Transactional
    public void registerUserInfo(String contact) {
        User authenticatedUser = AuthenticatedUserHolder.getAuthenticatedUser();
        authenticatedUser.updateContact(contact);
        userRepository.save(authenticatedUser);
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
