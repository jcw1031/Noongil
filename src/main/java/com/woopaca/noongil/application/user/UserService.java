package com.woopaca.noongil.application.user;

import com.woopaca.noongil.application.auth.AuthenticatedUserHolder;
import com.woopaca.noongil.domain.user.User;
import com.woopaca.noongil.domain.user.UserRepository;
import com.woopaca.noongil.event.UserEventPublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserEventPublisher userEventPublisher;

    public UserService(UserRepository userRepository, UserEventPublisher userEventPublisher) {
        this.userRepository = userRepository;
        this.userEventPublisher = userEventPublisher;
    }

    @Transactional
    public void updateUserInfo(String contact) {
        User authenticatedUser = AuthenticatedUserHolder.getAuthenticatedUser();
        authenticatedUser.updateContact(contact);
        userRepository.save(authenticatedUser);
        userEventPublisher.publishRegisterContactEvent(authenticatedUser.getId(), contact);
    }
}
