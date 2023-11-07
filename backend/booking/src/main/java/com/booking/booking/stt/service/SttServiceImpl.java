package com.booking.booking.stt.service;

import com.booking.booking.stt.domain.Transcription;
import com.booking.booking.stt.dto.*;
import com.booking.booking.stt.repository.TranscriptionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class SttServiceImpl implements SttService{

    @Value("${stt.invoke-url}")
    private String invokeUrl;
    @Value("${stt.key}")
    private String sttKey;
    @Value("${gpt.key}")
    private String gptKey;

    private final TranscriptionRepository transcriptionRepository;

    private final WebClient sttWebClient= WebClient.create(invokeUrl);
    private final WebClient gptWebClient= WebClient.create("https://api.openai.com");
    @Override
    public Mono<SttResponseDto> speachToText(SttRequestDto requestDto) {

        Map<String, Object> requestBody = new HashMap<>();
        //음성 001.m4a
        requestBody.put("dataKey","recording/"+requestDto.fileName());
        requestBody.put("language","ko-KR");
        requestBody.put("completion","sync");
        requestBody.put("fullText",Boolean.TRUE);
        requestBody.put("resultToObs",Boolean.TRUE);
        requestBody.put("diarization.enable",Boolean.TRUE);

        return sttWebClient.post()
                .uri("/recognizer/object-storage")
                .header("Content-Type","application/json")
                .header("X-CLOVASPEECH-API-KEY",sttKey)
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

    public Mono<GptResponseDto> gpt(){
        GptRequestDto request=new GptRequestDto();
        request.setModel("gpt-3.5-turbo");
        request.setMessages(new ArrayList<>());
        request.getMessages().add(new MessageDto("system","주어진 텍스트는 독서 모임의 대화록입니다. 텍스트 내용을 요약해주세요."));
        request.getMessages().add(new MessageDto("user","아침에 뭐 다 지 아침에 오자마자 지도 계속 쳐다보고 있는데 웹에서 제공해 주는 기능과는 달리 안드로이드에서 제공해 주는 api 진짜 거의 없더라고요 그걸 좀 더 찾아봐야 할 것 같고 오늘 오후에 사랑님 갈치해야 해서 하고 모임 생성 어제 안 돼서 현영이 된다. 그러면 이제 이후에 테스트를 몇 분만 기다리 부서 감색 다 만들어보고 이제 지연이 쪽이랑 연결하는 거 오늘 한 다음에 마이페이지 작업 스크롤이라 하이어 베이스 알 이러면 녹으면 안 되죠 나는 어제부터 stt api 설명서 읽어봤는데 오전에는 한번 해본 샘플 코드잖아 뭐라고 비비 바꾸고 있다고 아이크 김미씨 일단 저장되고 조회도 맛있어요. 잘 근데 이게 테이블이 그냥 하나에서만 되고 그 종인 공개를 매장해줘야 돼서 그거 하고는 되지 않을까 부인하면 할 게 없어요. 알림은 하여베 스폰소 어드민 집 네 달걀이랑 안경 다시 하면 안경 다시 안경 두 개야"));
        return gptWebClient.post()
                .uri("/v1/chat/completions")
                .header("Authorization","Bearer "+gptKey)
                .header("Content-Type","application/json")
                .bodyValue(request)
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
                .bodyToMono(GptResponseDto.class);
    }

    private Mono<Transcription> saveTranscription(SttResponseDto sttResponseDto) {
        Transcription transcription = Transcription.of(sttResponseDto);
        return transcriptionRepository.save(transcription);
    }
}
