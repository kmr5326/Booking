package com.booking.booking.meeting.service;

import com.booking.booking.meeting.dto.response.MemberInfoResponse;
import com.booking.booking.meeting.repository.MeetingRepository;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@Service
public class MeetingService {

    private final MeetingRepository meetingRepository;
    private static final String GATEWAY_URL = "http://localhost:8999";

    public void arrangeMeeting(String userEmail) {
        log.info("start arrageMettings");
        getMemberInfoByEmail(userEmail)
            .flatMap(memberInfo -> {
                log.info(memberInfo.toString());
                log.info("hello world!!!");
                return Mono.empty();
            })
            .subscribe( // 이 부분이 추가되어야 합니다.
                result -> {
                    // onNext 이벤트 처리
                    // 데이터 처리가 성공적으로 완료되면 이 부분이 호출됩니다.
                },
                error -> {
                    // onError 이벤트 처리
                    // 에러가 발생하면 이 부분이 호출됩니다.
                    log.error("Error occurred: ", error);
                },
                () -> {
                    // onComplete 이벤트 처리
                    // 스트림이 성공적으로 완료되면 이 부분이 호출됩니다.
                    log.info("Completed arrangeMeetings");
                }
            );
        log.info("hello"); // 이 로그 메시지는 구독이 시작되자마자 출력될 것입니다.
    }

    private Mono<MemberInfoResponse> getMemberInfoByEmail(String userEmail) {
        log.info("start inter-server comm");
        WebClient webClient = WebClient.builder().build();
        URI uri = URI.create(GATEWAY_URL + "/api/members/memberInfo/" + userEmail);

        Mono<MemberInfoResponse> memberInfoResponse = webClient.get()
                                                               .uri(uri)
                                                               .retrieve()
                                                               .onStatus(HttpStatus::is4xxClientError, response -> Mono.error(RuntimeException::new))
                                                               .onStatus(HttpStatus::is5xxServerError, response -> Mono.error(RuntimeException::new))
                                                               .bodyToMono(MemberInfoResponse.class);

        log.info("end inter-server comm");
        return memberInfoResponse;
    }
}
