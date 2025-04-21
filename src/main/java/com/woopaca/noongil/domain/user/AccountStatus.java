package com.woopaca.noongil.domain.user;

public enum AccountStatus {

    PENDING("대기"), //연락처 등록 대기
    ACTIVE("활성");

    private final String expression;

    AccountStatus(String expression) {
        this.expression = expression;
    }
}
