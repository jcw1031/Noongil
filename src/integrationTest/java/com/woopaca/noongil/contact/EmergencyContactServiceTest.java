package com.woopaca.noongil.contact;

import com.woopaca.noongil.application.auth.AuthenticatedUserHolder;
import com.woopaca.noongil.application.emergency.contact.EmergencyContactService;
import com.woopaca.noongil.domain.user.User;
import com.woopaca.noongil.domain.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class EmergencyContactServiceTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmergencyContactService emergencyContactService;

    @Test
    void registerEmergencyContact() throws InterruptedException {
        User user = userRepository.findById(1L)
                .get();
        AuthenticatedUserHolder.setAuthenticatedUser(user);
        emergencyContactService.registerEmergencyContact("테스트", "01012345678");

        Thread.sleep(1_000);
    }
}