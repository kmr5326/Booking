package com.booking.booking.meeting.exception;

import com.booking.booking.global.exception.BaseException;
import com.booking.booking.global.exception.ErrorCode;

public class MeetingException extends BaseException {
    public MeetingException(ErrorCode errorCode) {
        super(errorCode);
    }
}
