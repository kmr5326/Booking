package com.booking.booking.global.exception;

import com.booking.booking.global.dto.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;

@Slf4j
@RestControllerAdvice
public class ExceptionControllerHandler {

    @ExceptionHandler(BaseException.class)
    public Mono<ResponseEntity<ErrorResponse>> test(BaseException e) {
        log.error(" '{}' ", e.errorCode.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(e.getErrorCode());
        return Mono.just(ResponseEntity.status(errorResponse.httpStatus()).body(errorResponse));
    }
}
