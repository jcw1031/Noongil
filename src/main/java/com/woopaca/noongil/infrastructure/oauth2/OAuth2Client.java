package com.woopaca.noongil.infrastructure.oauth2;

import org.springframework.web.client.RestClient;

public abstract class OAuth2Client {

    protected final RestClient restClient;

    protected OAuth2Client(RestClient restClient) {
        this.restClient = restClient;
    }

    public abstract OAuth2Token requestToken(String authorizationCode);

    public abstract OAuth2User requestUserInfo(String accessToken);
}
