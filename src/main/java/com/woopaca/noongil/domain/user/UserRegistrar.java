package com.woopaca.noongil.domain.user;

import org.springframework.stereotype.Component;

@Component
public class UserRegistrar {

    private final UserRepository userRepository;

    public UserRegistrar(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User register(String identifier, String name, String email) {
        return userRepository.findByIdentifier(identifier)
                .orElseGet(() -> {
                    User signUpUser = User.signUp(identifier, name, email);
                    return userRepository.save(signUpUser);
                });
    }
}
