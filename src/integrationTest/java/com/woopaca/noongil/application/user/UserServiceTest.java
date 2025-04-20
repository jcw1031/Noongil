package com.woopaca.noongil.application.user;

import com.woopaca.noongil.application.auth.AuthenticatedUserHolder;
import com.woopaca.noongil.application.emergency.contact.EmergencyContactService;
import com.woopaca.noongil.domain.user.User;
import com.woopaca.noongil.domain.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserService userService;
    @Autowired
    private EmergencyContactService emergencyContactService;
    @Autowired
    private UserRepository userRepository;

    @Test
    void registerUserInfo() throws InterruptedException {
        User user = userRepository.findById(1L)
                .get();
        AuthenticatedUserHolder.setAuthenticatedUser(user);
        emergencyContactService.registerEmergencyContact("테스트", "01011111111");
        AuthenticatedUserHolder.clear();

        Thread.sleep(2_000L);

        User testUser = userRepository.findById(3L)
                .get();
        AuthenticatedUserHolder.setAuthenticatedUser(testUser);
        userService.registerUserInfo("01011111111");

        Thread.sleep(2_000L);
    }
}