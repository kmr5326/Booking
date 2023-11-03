//package com.booking.booking.global.dto;
//
//import com.booking.booking.global.exception.ErrorCode;
//import org.springframework.http.HttpStatus;
//
//public record ErrorResponse(
//    HttpStatus httpStatus,
//    String message
//) {
//    public ErrorResponse(ErrorCode errorCode) {
//        this (
//            errorCode.getHttpStatus(),
//            errorCode.getMessage()
//        );
//    }
//}
