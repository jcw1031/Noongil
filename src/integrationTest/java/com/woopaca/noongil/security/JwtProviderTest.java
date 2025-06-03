package com.woopaca.noongil.security;

import com.woopaca.noongil.domain.user.User;
import com.woopaca.noongil.domain.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@Slf4j
@ActiveProfiles("local-develop")
@SpringBootTest
class JwtProviderTest {

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private UserRepository userRepository;

    @Test
    void issueAccessToken() {
        User user = userRepository.findById(46L)
                .get();
        String accessToken = jwtProvider.issueAccessToken(user);
        log.info("accessToken: {}", accessToken);
    }
}
