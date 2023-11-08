package com.booking.booking.stt.service;

import com.booking.booking.stt.dto.SttRequestDto;
import com.booking.booking.stt.dto.SttResponseDto;
import com.booking.booking.stt.dto.SummaryControllerDto;
import com.booking.booking.stt.dto.SummaryResponse;
import reactor.core.publisher.Mono;

public interface SttService {
    Mono<SttResponseDto> speachToText(SttRequestDto requestDto);

    Mono<SummaryResponse> summary(SummaryControllerDto req);
}
