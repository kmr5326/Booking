package com.booking.booking.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    CREATE_MEETING_FAILURE(HttpStatus.BAD_REQUEST, "미팅 생성을 실패했습니다."),
    ADD_PARTICIPANT_FAILURE(HttpStatus.BAD_REQUEST, "참가자 추가를 실패했습니다.");

    final HttpStatus httpStatus;
    final String message;
}
