package com.woopaca.noongil.adapter.auth;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationProvider {

    public String getSubject() {
        Object principal = SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        if (UserDetails.class.isAssignableFrom(principal.getClass())) {
            UserDetails userDetails = (UserDetails) principal;
            return userDetails.getUsername();
        }

        return String.valueOf(principal);
    }
}
