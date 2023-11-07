package com.booking.booking.stt.service;

import com.booking.booking.stt.domain.Transcription;
import com.booking.booking.stt.dto.SttRequestDto;
import com.booking.booking.stt.dto.SttResponseDto;
import com.booking.booking.stt.repository.TranscriptionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class SttServiceImpl implements SttService{

    @Value("${stt.invoke-url}")
    private String invokeUrl;
    @Value("${stt.key}")
    private String key;

    private final TranscriptionRepository transcriptionRepository;
    @Override
    public Mono<SttResponseDto> speachToText(SttRequestDto requestDto) {
        WebClient webClient= WebClient.create(invokeUrl);

        Map<String, Object> requestBody = new HashMap<>();
        //음성 001.m4a
        requestBody.put("dataKey","recording/"+requestDto.fileName());
        requestBody.put("language","ko-KR");
        requestBody.put("completion","sync");
        requestBody.put("fullText",Boolean.TRUE);
        requestBody.put("resultToObs",Boolean.TRUE);
        requestBody.put("diarization.enable",Boolean.TRUE);

        return webClient.post()
                .uri("/recognizer/object-storage")
                .header("Content-Type","application/json")
                .header("X-CLOVASPEECH-API-KEY",key)
                .bodyValue(requestBody)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, clientResponse -> {
                    return clientResponse.bodyToMono(String.class).doOnNext(body -> {
                        // 로그에 오류 본문을 출력
                        log.error("4xx error: {}", body);
                    }).then(Mono.error(new RuntimeException("Client error")));
                })
                .onStatus(HttpStatus::is5xxServerError, clientResponse -> {
                    return clientResponse.bodyToMono(String.class).doOnNext(body -> {
                        // 로그에 오류 본문을 출력
                        log.error("5xx error: {}", body);
                    }).then(Mono.error(new RuntimeException("Server error")));
                })
                .bodyToMono(SttResponseDto.class)
                .flatMap(sttResponseDto -> saveTranscription(sttResponseDto).thenReturn(sttResponseDto))
                .doOnNext(resp-> log.info("stt 결과 {}",resp));
    }

    private Mono<Transcription> saveTranscription(SttResponseDto sttResponseDto) {
        Transcription transcription = Transcription.of(sttResponseDto);
        return transcriptionRepository.save(transcription);
    }
}
