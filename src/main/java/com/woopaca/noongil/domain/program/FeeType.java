package com.woopaca.noongil.domain.program;

import lombok.Getter;

@Getter
public enum FeeType {
    FREE("무료"),
    PAID("유료");

    private final String expression;

    FeeType(String expression) {
        this.expression = expression;
    }
}
