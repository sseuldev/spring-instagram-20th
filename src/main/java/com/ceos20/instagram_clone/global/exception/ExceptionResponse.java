package com.ceos20.instagram_clone.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ExceptionResponse {

    private final int code;
    private boolean inSuccess = false;
    private final String message;
}
