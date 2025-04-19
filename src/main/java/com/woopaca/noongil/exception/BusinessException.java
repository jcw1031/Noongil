package com.woopaca.noongil.exception;

import com.woopaca.noongil.exception.ErrorType.ErrorHttpStatus;
import lombok.Getter;

@Getter
public abstract class BusinessException extends RuntimeException {

    private final ErrorType errorType;

    protected BusinessException(String message, ErrorType errorType) {
        super(message);
        this.errorType = errorType;
    }

    public ErrorHttpStatus getErrorHttpStatus() {
        ErrorHttpStatus errorHttpStatus = errorType.getErrorHttpStatus();
        if (errorHttpStatus == null) {
            return ErrorHttpStatus.BAD_REQUEST;
        }
        return errorHttpStatus;
    }
}
