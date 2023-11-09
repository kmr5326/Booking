package com.booking.booking.stt.service;

import com.booking.booking.stt.dto.request.SttRequestDto;
import com.booking.booking.stt.dto.request.SummaryControllerDto;
import com.booking.booking.stt.dto.response.SttResponseDto;
import com.booking.booking.stt.dto.response.SummaryResponse;
import com.booking.booking.stt.dto.response.TranscriptionResponse;
import reactor.core.publisher.Mono;

public interface SttService {
    Mono<SttResponseDto> speachToText(SttRequestDto requestDto);

    Mono<TranscriptionResponse> findTranscriptionByFileName(String fileName);

    Mono<SummaryResponse> summary(SummaryControllerDto req);
}
