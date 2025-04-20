package com.woopaca.noongil.web.dto;

import jakarta.validation.constraints.NotBlank;

public record RegisterContactRequest(@NotBlank String contact) {

    public RegisterContactRequest {
        if (!contact.matches("\\d{11}")) {
            throw new IllegalArgumentException("전화번호는 11자리 숫자이어야 합니다.");
        }
    }
}
