package com.woopaca.noongil.web.dto;

import jakarta.validation.constraints.NotBlank;

public record RegisterPushTokenRequest(@NotBlank String pushToken) {
}
