package com.booking.booking.stt.controller;

import com.booking.booking.stt.dto.SttRequestDto;
import com.booking.booking.stt.dto.SttResponseDto;
import com.booking.booking.stt.service.SttService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Mono;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/booking/stt")
@Slf4j
public class SttController {

    private final SttService sttService;

    @PostMapping("")
    public Mono<ResponseEntity<SttResponseDto>> speachToText(@RequestBody SttRequestDto requestDto) {
        log.info("stt 요청 파일 이름 {}",requestDto.fileName());
        return sttService.speachToText(requestDto)
                .map(resp->ResponseEntity.ok().body(resp));
    }
}
