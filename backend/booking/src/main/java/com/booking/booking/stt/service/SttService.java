package com.booking.booking.stt.service;

import com.booking.booking.stt.dto.GptResponseDto;
import com.booking.booking.stt.dto.SttRequestDto;
import com.booking.booking.stt.dto.SttResponseDto;
import reactor.core.publisher.Mono;

public interface SttService {
    Mono<SttResponseDto> speachToText(SttRequestDto requestDto);

    Mono<GptResponseDto> gpt();
}
