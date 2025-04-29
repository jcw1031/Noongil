package com.woopaca.noongil.web.dto;

import jakarta.validation.constraints.NotBlank;

public record SignInRequest(@NotBlank String authorizationCode,
                            String name,
                            String email) {
}
