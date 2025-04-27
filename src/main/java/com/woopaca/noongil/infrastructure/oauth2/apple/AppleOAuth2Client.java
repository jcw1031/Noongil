package com.woopaca.noongil.infrastructure.oauth2.apple;

import com.woopaca.noongil.infrastructure.apple.AppleProperties;
import com.woopaca.noongil.infrastructure.oauth2.OAuth2Client;
import com.woopaca.noongil.infrastructure.oauth2.OAuth2Token;
import com.woopaca.noongil.infrastructure.oauth2.OAuth2User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClient;

@Slf4j
@Component
public class AppleOAuth2Client extends OAuth2Client {

    private static final String APPLE_OAUTH2_URL = "https://appleid.apple.com/auth/token";
    private static final String GRANT_TYPE = "authorization_code";

    private final AppleProperties appleProperties;
    private final AppleClientSecretGenerator appleClientSecretGenerator;

    public AppleOAuth2Client(RestClient restClient, AppleProperties appleProperties, AppleClientSecretGenerator appleClientSecretGenerator) {
        super(restClient);
        this.appleProperties = appleProperties;
        this.appleClientSecretGenerator = appleClientSecretGenerator;
    }

    @Override
    public OAuth2Token requestToken(String authorizationCode) {
        MultiValueMap<String, Object> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("client_id", appleProperties.getClientId());
        requestBody.add("client_secret", appleClientSecretGenerator.generateClientSecret());
        requestBody.add("code", authorizationCode);
        requestBody.add("grant_type", GRANT_TYPE);

        try {
            return restClient.post()
                    .uri(APPLE_OAUTH2_URL)
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(requestBody)
                    .retrieve()
                    .body(AppleToken.class);
        } catch (HttpStatusCodeException e) {
            log.error("애플 OAuth2 액세스 토큰 요청 실패. message: {}", e.getMessage());
            return AppleToken.empty();
        }
    }

    @Override
    public OAuth2User requestUserInfo(String accessToken) {
        throw new UnsupportedOperationException();
    }
}
