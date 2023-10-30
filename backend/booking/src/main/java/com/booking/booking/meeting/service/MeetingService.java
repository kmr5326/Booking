package com.booking.booking.meeting.service;

import com.booking.booking.global.exception.ErrorCode;
import com.booking.booking.meeting.domain.Meeting;
import com.booking.booking.meeting.domain.MeetingState;
import com.booking.booking.meeting.dto.request.MeetingRequest;
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
import reactor.core.scheduler.Schedulers;

@Slf4j
@RequiredArgsConstructor
@Service
public class MeetingService {

    private final MeetingRepository meetingRepository;
    private static final String GATEWAY_URL = "http://localhost:8999";

    public Mono<Void> arrangeMeeting(String userEmail, MeetingRequest meetingRequest) {
        return getMemberInfoByEmail(userEmail)
            .flatMap(memberInfo -> {
                log.info("hello web-flux");
                Meeting meeting = Meeting.builder()
                                         .leaderId(memberInfo.loginId())
                                         .address(memberInfo.address())
                                         .bookIsbn(meetingRequest.bookIsbn())
                                         .meetingTitle(meetingRequest.meetingTitle())
                                         .description(meetingRequest.description())
                                         .maxParticipants(meetingRequest.maxParticipants())
                                         .meetingState(MeetingState.PREPARING)
                                         .build();
                log.info("meeting name : {}", meeting.getMeetingTitle());
                // 별도의 스레드 풀에서 블로킹 작업 실행
                return Mono.fromRunnable(() -> meetingRepository.save(meeting))
                           .subscribeOn(Schedulers.boundedElastic())
                           .then();
            })
            .doOnError(error -> {
                log.error("Error during arrangeMeeting : {}", error.toString());
                throw new MeetingException(ErrorCode.CREATED_MEETING_FAILURE);
            });
    }

    private Mono<MemberInfoResponse> getMemberInfoByEmail(String userEmail) {
        log.info("start inter-server comm");
        WebClient webClient = WebClient.builder()
                                       .build();
        URI uri = URI.create(GATEWAY_URL + "/api/members/memberInfo/" + userEmail);

        Mono<MemberInfoResponse> memberInfoResponse = webClient.get()
                                                               .uri(uri)
                                                               .retrieve()
                                                               .onStatus(
                                                                   HttpStatus::is4xxClientError,
                                                                   response -> Mono.error(
                                                                       RuntimeException::new))
                                                               .onStatus(
                                                                   HttpStatus::is5xxServerError,
                                                                   response -> Mono.error(
                                                                       RuntimeException::new))
                                                               .bodyToMono(
                                                                   MemberInfoResponse.class);

        log.info("end inter-server comm");
        return memberInfoResponse;
    }
}
