package com.woopaca.noongil.web.dto;

import jakarta.validation.constraints.NotBlank;

public record SignInRequest(@NotBlank String authorizationCode,
                            @NotBlank String name,
                            @NotBlank String email) {
}
