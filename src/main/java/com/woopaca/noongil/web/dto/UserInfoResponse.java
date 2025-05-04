package com.woopaca.noongil.web.dto;

import lombok.Builder;

@Builder
public record UserInfoResponse(String name, String email, Consents consents) {

    public record Consents(boolean push, boolean sms) {
    }
}
