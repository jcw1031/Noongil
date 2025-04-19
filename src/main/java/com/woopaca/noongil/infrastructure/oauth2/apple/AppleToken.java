package com.woopaca.noongil.infrastructure.oauth2.apple;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.woopaca.noongil.infrastructure.oauth2.OAuth2Token;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public record AppleToken(String accessToken, String idToken) implements OAuth2Token {

    public static AppleToken empty() {
        return null;
    }
}
