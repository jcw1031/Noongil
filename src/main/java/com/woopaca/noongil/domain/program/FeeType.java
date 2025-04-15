package com.woopaca.noongil.domain.program;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum FeeType {
    FREE("무료"),
    PAID("유료");

    private final String expression;

    FeeType(String expression) {
        this.expression = expression;
    }

    public static FeeType find(String feeTypeExpression) {
        return Arrays.stream(values())
                .filter(feeType -> feeType.getExpression().equals(feeTypeExpression))
                .findAny()
                .orElseThrow();
    }
}
