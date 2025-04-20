package com.woopaca.noongil.application.auth;

import com.woopaca.noongil.domain.user.User;

public record SignInResult(User user, String accessToken) {
}
