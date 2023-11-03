package com.booking.booking.global.exception;

import lombok.Getter;

@Getter
public class BaseException extends RuntimeException{
    ErrorCode errorCode;

    public BaseException(ErrorCode errorCode) {
        super(errorCode.getMessage());
    }
}
