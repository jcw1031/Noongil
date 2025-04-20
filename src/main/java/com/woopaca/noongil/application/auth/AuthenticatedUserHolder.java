package com.woopaca.noongil.application.auth;

import com.woopaca.noongil.domain.user.User;

public final class AuthenticatedUserHolder {

    private static final ThreadLocal<User> contextHolder = new ThreadLocal<>();

    private AuthenticatedUserHolder() {
    }

    public static void setAuthenticatedUser(User user) {
        contextHolder.set(user);
    }

    public static User getAuthenticatedUser() {
        User authenticatedUser = contextHolder.get();
        if (authenticatedUser == null) {
            throw new IllegalStateException("사용자 인증 정보가 없습니다.");
        }
        return authenticatedUser;
    }

    public static void clear() {
        contextHolder.remove();
    }
}
