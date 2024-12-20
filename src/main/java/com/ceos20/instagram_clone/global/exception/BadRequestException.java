package com.ceos20.instagram_clone.global.exception;

import lombok.Getter;

@Getter
public class BadRequestException extends RuntimeException {

    private final int code;
    private final boolean inSuccess;
    private final String message;

    public BadRequestException(final ExceptionCode exceptionCode){
        this.code = exceptionCode.getCode();
        this.inSuccess = exceptionCode.isInSuccess();
        this.message = exceptionCode.getMessage();
    }
}
