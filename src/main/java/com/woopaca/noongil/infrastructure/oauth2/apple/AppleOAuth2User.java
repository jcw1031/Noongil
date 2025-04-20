package com.woopaca.noongil.infrastructure.oauth2.apple;

import com.woopaca.noongil.infrastructure.oauth2.OAuth2User;

public record AppleOAuth2User(String name, String email) implements OAuth2User {

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
