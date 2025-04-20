package com.woopaca.noongil.web.dto;

import com.woopaca.noongil.exception.BusinessException;
import com.woopaca.noongil.exception.ErrorType;

import java.time.LocalDateTime;

public record ApiResults() {

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(LocalDateTime.now(), true, null, data);
    }

    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(LocalDateTime.now(), true, message, data);
    }

    public static ErrorResponse error(BusinessException exception) {
        return error(exception.getMessage(), exception.getErrorType());
    }

    public static ErrorResponse error(String message, ErrorType errorType) {
        return error(message, errorType.getErrorCode());
    }

    public static ErrorResponse error(String message, String errorCode) {
        return new ErrorResponse(LocalDateTime.now(), false, message, errorCode);
    }

    public record ApiResponse<T>(LocalDateTime serverTimestamp, boolean success, String message, T data) {
    }

    public record ErrorResponse(LocalDateTime serverTimestamp, boolean success, String message, String errorCode) {
    }
}
