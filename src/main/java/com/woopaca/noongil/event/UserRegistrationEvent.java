package com.woopaca.noongil.event;

import com.woopaca.noongil.domain.user.User;

public record UserRegistrationEvent(User user) {
}
