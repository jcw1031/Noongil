package com.woopaca.noongil.infrastructure.oauth2.apple;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AppleUserInformationExtractor {

    public void parseIdToken(String idToken) {
        DecodedJWT decodedToken = JWT.decode(idToken);
        String subject = decodedToken.getSubject();
        String email = decodedToken.getClaim("email").asString();
        log.info("subject: {}", subject);
        log.info("email: {}", email);
    }
}
