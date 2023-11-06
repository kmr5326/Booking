package com.booking.booking.global.utils;

import com.booking.booking.global.dto.MemberInfoResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.URI;

@Slf4j
@Component
public class MemberUtil {
    @Value("${gateway.url}")
    private String GATEWAY_URL;

    public Mono<MemberInfoResponse> getMemberInfoByEmail(String userEmail) {
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

    public Mono<MemberInfoResponse> getMemberInfoByPk(Integer memberPk) {
        log.info("Booking Server MemberUtil - getMemberInfoByPk({})", memberPk);

        WebClient webClient = WebClient.builder().build();
        URI uri = URI.create(GATEWAY_URL + "/api/members/memberInfo-pk/" + memberPk);

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
