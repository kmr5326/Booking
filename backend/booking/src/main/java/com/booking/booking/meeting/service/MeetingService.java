package com.booking.booking.meeting.service;

import com.booking.booking.global.exception.ErrorCode;
import com.booking.booking.meeting.dto.response.MemberInfoResponse;
import com.booking.booking.meeting.exception.MeetingException;
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
                    // 모임 방 생성, 채팅 방 생성, 채팅 서버로 이 내역을 전송
                    // 모임을 만들고
                    // chatRoomService 에서 채팅 방
                    // 참가자 목록에 방장 넣어주고
                    // 채팅 서버로 이 내역을 전송해 준다.
                },
                error -> {
                    // onError 이벤트 처리
                    // 에러가 발생하면 이 부분이 호출됩니다.
                    // throw new RuntimeException();
                    throw new MeetingException(ErrorCode.CREATED_MEETING_FAILURE);
                    //log.error("Error occurred: ", error);
                },
                () -> {
                    // onComplete 이벤트 처리
                    // 스트림이 성공적으로 완료되면 이 부분이 호출됩니다.
                    log.info("Completed arrangeMeetings");
                }
            );
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
