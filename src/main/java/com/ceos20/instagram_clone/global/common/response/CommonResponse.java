package com.ceos20.instagram_clone.global.common.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CommonResponse<T> {

    private int code;
    private boolean inSuccess;
    private String message;
    private T result;

    public CommonResponse(ResponseCode status, T result, String message) {
        this.code = status.getCode();
        this.inSuccess = status.isInSuccess();
        this.message = message;
        this.result = result;
    }

    public CommonResponse(ResponseCode status, String message) {
        this.code = status.getCode();
        this.inSuccess = status.isInSuccess();
        this.message = message;
    }
}