package com.booking.booking.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    GET_MEMBERINFO_FAILURE(HttpStatus.BAD_REQUEST, "회원 조회를 실패했습니다."),
    CREATE_MEETING_FAILURE(HttpStatus.BAD_REQUEST, "미팅 생성을 실패했습니다."),
    GET_MEETING_FAILURE(HttpStatus.BAD_REQUEST, "미팅 조회를 실패했습니다."),
    ADD_PARTICIPANT_FAILURE(HttpStatus.BAD_REQUEST, "참가자 추가를 실패했습니다."),
    ADD_WAITLIST_FAILURE(HttpStatus.BAD_REQUEST, "참가 신청을 실패했습니다."),
    CONFIRM_MEETING_FAILURE(HttpStatus.BAD_REQUEST, "미팅 확정을 실패했습니다."),
    CREATE_HASHTAG_FAILURE(HttpStatus.BAD_REQUEST, "해시태그 생성을 실패했습니다."),
    GET_HASHTAG_FAILURE(HttpStatus.BAD_REQUEST, "해시태그 조회를 실패했습니다.");

    final HttpStatus httpStatus;
    final String message;
}
