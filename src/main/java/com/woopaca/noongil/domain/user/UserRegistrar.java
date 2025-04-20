package com.woopaca.noongil.domain.user;

import org.springframework.stereotype.Component;

@Component
public class UserRegistrar {

    private final UserRepository userRepository;

    public UserRegistrar(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User register(String name, String email) {
        return userRepository.findByEmail(email)
                .orElseGet(() -> {
                    User signUpUser = User.signUp(name, email);
                    return userRepository.save(signUpUser);
                });
    }
}
