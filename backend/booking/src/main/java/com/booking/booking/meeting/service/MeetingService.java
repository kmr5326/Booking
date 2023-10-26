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

    public Mono<Void> arrangeMeeting(String userEmail) {

        return getMemberInfoByEmail(userEmail)
            .flatMap(memberInfo -> {
                // 비즈니스 로직
                return Mono.empty();
            });
    }

    private Mono<MemberInfoResponse> getMemberInfoByEmail(String userEmail) {

        WebClient webClient = WebClient.builder().build();
        URI uri = URI.create(GATEWAY_URL + "/api/members/memberInfo/" + userEmail);

        return webClient.get()
                        .uri(uri)
                        .retrieve()
                        .onStatus(HttpStatus::is4xxClientError, response -> Mono.error(RuntimeException::new))
                        .onStatus(HttpStatus::is5xxServerError, response -> Mono.error(RuntimeException::new))
                        .bodyToMono(MemberInfoResponse.class);
    }
}
