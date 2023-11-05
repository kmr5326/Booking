package com.booking.booking.global.utils;

import com.booking.booking.global.dto.MemberInfoResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.URI;

@Slf4j
public class MemberUtil {
    private static final String GATEWAY_URL = "http://localhost:8999";

    public static Mono<MemberInfoResponse> getMemberInfoByEmail(String userEmail) {
        log.info("Booking Server MemberUtil - getMemberInfoByEmail({})", userEmail);

        WebClient webClient = WebClient.builder().build();
        URI uri = URI.create(GATEWAY_URL + "/api/members/memberInfo/" + userEmail);

        return webClient.get()
                .uri(uri)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError,
                        response -> Mono.error(new RuntimeException("회원정보 응답 에러")))
                .onStatus(HttpStatus::is5xxServerError,
                        response -> Mono.error(new RuntimeException("회원정보 응답 에러")))
                .bodyToMono(MemberInfoResponse.class);
    }
}
