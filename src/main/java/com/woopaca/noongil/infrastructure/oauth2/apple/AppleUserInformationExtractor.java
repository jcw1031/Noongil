package com.woopaca.noongil.infrastructure.oauth2.apple;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AppleUserInformationExtractor {

    public AppleOAuth2User extractUserInfo(String idToken) {
        DecodedJWT decodedToken = JWT.decode(idToken);
        log.info("decodedToken: {}", decodedToken);
        String subject = decodedToken.getSubject();
        String email = decodedToken.getClaim("email")
                .asString();
        return new AppleOAuth2User(subject, email);
    }
}
