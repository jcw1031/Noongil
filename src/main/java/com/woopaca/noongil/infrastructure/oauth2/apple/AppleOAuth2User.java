package com.woopaca.noongil.infrastructure.oauth2.apple;

import com.woopaca.noongil.infrastructure.oauth2.OAuth2User;
import org.springframework.stereotype.Component;

@Component
public class AppleOAuth2User implements OAuth2User {

    @Override
    public String identifier() {
        return "";
    }

    @Override
    public String name() {
        return "";
    }

    @Override
    public String provider() {
        return "APPLE";
    }
}
