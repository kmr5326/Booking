package com.booking.booking.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    CREATED_MEETING_FAILURE(HttpStatus.BAD_REQUEST, "미팅 생성에 실패했습니다.");

    final HttpStatus httpStatus;
    final String message;
}
