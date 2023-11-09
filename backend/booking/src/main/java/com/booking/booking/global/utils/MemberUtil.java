package com.booking.booking.global.utils;

import com.booking.booking.global.dto.response.MemberResponse;
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
    private static String GATEWAY_URL;

    @Value("${gateway.url}")
    public void setGatewayUrl(String gatewayUrl) {
        MemberUtil.GATEWAY_URL = gatewayUrl;
    }

    public static Mono<MemberResponse> getMemberInfoByEmail(String userEmail) {
        log.info("[Booking:MemberUtil] getMemberInfoByEmail({})", userEmail);

        WebClient webClient = WebClient.builder().build();
        URI uri = URI.create(GATEWAY_URL + "/api/members/memberInfo/" + userEmail);

        return webClient.get()
                .uri(uri)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError,
                        response -> Mono.error(new RuntimeException("회원정보 응답 에러")))
                .onStatus(HttpStatus::is5xxServerError,
                        response -> Mono.error(new RuntimeException("회원정보 응답 에러")))
                .bodyToMono(MemberResponse.class);
    }

    public static Mono<MemberResponse> getMemberInfoByPk(Integer memberPk) {
        log.info("[Booking:MemberUtil] getMemberInfoByPk({})", memberPk);

        WebClient webClient = WebClient.builder().build();
        URI uri = URI.create(GATEWAY_URL + "/api/members/memberInfo-pk/" + memberPk);

        return webClient.get()
                .uri(uri)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError,
                        response -> Mono.error(new RuntimeException("회원정보 응답 에러")))
                .onStatus(HttpStatus::is5xxServerError,
                        response -> Mono.error(new RuntimeException("회원정보 응답 에러")))
                .bodyToMono(MemberResponse.class);
    }

    public static Mono<MemberResponse> getMemberInfoByNickname(String nickname) {
        log.info("[Booking:MemberUtil] getMemberInfoByNickname({})", nickname);

        return WebClient.create(GATEWAY_URL + "/api/members/memberInfo-nick")
                .get()
                .uri("/{nickname}", nickname)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError,
                        response -> Mono.error(new RuntimeException("회원정보 응답 에러 4xx")))
                .onStatus(HttpStatus::is5xxServerError,
                        response -> Mono.error(new RuntimeException("회원정보 응답 에러 5xx")))
                .bodyToMono(MemberResponse.class);
    }
}
