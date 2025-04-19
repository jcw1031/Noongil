package com.woopaca.noongil.adapter.auth;

import com.woopaca.noongil.application.auth.AuthenticatedUserHolder;
import com.woopaca.noongil.domain.user.User;
import com.woopaca.noongil.domain.user.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthenticateInterceptor implements HandlerInterceptor {

    private final AuthenticationProvider authenticationProvider;
    private final UserRepository userRepository;

    public AuthenticateInterceptor(AuthenticationProvider authenticationProvider, UserRepository userRepository) {
        this.authenticationProvider = authenticationProvider;
        this.userRepository = userRepository;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String subject = authenticationProvider.getSubject();
        User authenticatedUser = userRepository.findByEmail(subject)
                .orElseThrow();
        AuthenticatedUserHolder.setAuthenticatedUser(authenticatedUser);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        AuthenticatedUserHolder.clear();
    }
}
