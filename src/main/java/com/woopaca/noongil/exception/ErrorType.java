package com.woopaca.noongil.exception;

import lombok.Getter;

@Getter
public enum ErrorType {

    TEST("TEST", ErrorHttpStatus.BAD_REQUEST);

    private final String errorCode;
    private final ErrorHttpStatus errorHttpStatus;

    ErrorType(String errorCode, ErrorHttpStatus errorHttpStatus) {
        this.errorCode = errorCode;
        this.errorHttpStatus = errorHttpStatus;
    }

    @Getter
    public enum ErrorHttpStatus {

        BAD_REQUEST(400, "Bad Request"),
        UNAUTHORIZED(401, "Unauthorized"),
        NOT_FOUND(404, "Not Found");

        private final int value;
        private final String reasonPhrase;

        ErrorHttpStatus(int value, String reasonPhrase) {
            this.value = value;
            this.reasonPhrase = reasonPhrase;
        }
    }
}
