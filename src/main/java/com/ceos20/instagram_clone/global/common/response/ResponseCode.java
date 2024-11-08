package com.ceos20.instagram_clone.global.common.response;

import lombok.Getter;

@Getter
public enum ResponseCode {

    SUCCESS(2000, true, "요청에 성공하였습니다.");

    private int code;
    private boolean inSuccess;
    private String message;

    ResponseCode(int code, boolean inSuccess, String message) {
        this.inSuccess = inSuccess;
        this.code = code;
        this.message = message;
    }
}