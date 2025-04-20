package com.woopaca.noongil.web.dto;

import com.woopaca.noongil.application.auth.SignInResult;
import com.woopaca.noongil.domain.user.AccountStatus;

public record SignInResponse(String accessToken, AccountStatus accountStatus) {

    public static SignInResponse from(SignInResult signInResult) {
        return new SignInResponse(signInResult.accessToken(), signInResult.user().getStatus());
    }
}
