package com.woopaca.noongil.application.auth;

import com.woopaca.noongil.domain.user.User;
import com.woopaca.noongil.domain.user.UserRegistrar;
import com.woopaca.noongil.infrastructure.oauth2.OAuth2Client;
import com.woopaca.noongil.infrastructure.oauth2.apple.AppleOAuth2User;
import com.woopaca.noongil.infrastructure.oauth2.apple.AppleToken;
import com.woopaca.noongil.infrastructure.oauth2.apple.AppleUserInformationExtractor;
import com.woopaca.noongil.security.JwtProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class AuthenticationService {

    private final OAuth2Client oAuth2Client;
    private final AppleUserInformationExtractor userInformationExtractor;
    private final UserRegistrar userRegistrar;
    private final JwtProvider jwtProvider;

    public AuthenticationService(OAuth2Client oAuth2Client, AppleUserInformationExtractor userInformationExtractor, UserRegistrar userRegistrar, JwtProvider jwtProvider) {
        this.oAuth2Client = oAuth2Client;
        this.userInformationExtractor = userInformationExtractor;
        this.userRegistrar = userRegistrar;
        this.jwtProvider = jwtProvider;
    }

    @Transactional
    public SignInResult authenticateUser(String authorizationCode, String name, String email) {
        AppleToken appleToken = (AppleToken) oAuth2Client.requestToken(authorizationCode);
        if (appleToken.isEmpty()) {
            log.error("authorizationCode: {}, name: {}, email: {}", authorizationCode, name, email);
            throw new IllegalArgumentException("애플 로그인에 실패했습니다.");
        }

        AppleOAuth2User appleUser = userInformationExtractor.extractUserInfo(appleToken.idToken());
        User registeredUser = userRegistrar.register(appleUser.identifier(), name, email);
        String accessToken = jwtProvider.issueAccessToken(registeredUser);
        return new SignInResult(registeredUser, accessToken);
    }
}
