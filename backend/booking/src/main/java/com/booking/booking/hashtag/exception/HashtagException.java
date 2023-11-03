package com.booking.booking.hashtag.exception;

import com.booking.booking.global.exception.BaseException;
import com.booking.booking.global.exception.ErrorCode;

public class HashtagException extends BaseException {
    public HashtagException(ErrorCode errorCode) {
        super(errorCode);
    }
}
