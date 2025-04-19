package com.woopaca.noongil.infrastructure.oauth2;

public interface OAuth2User {

    String identifier();

    String name();

    String provider();
}
