package com.booking.booking.global.utils;

import com.booking.booking.global.dto.response.MemberResponse;
import java.time.Duration;
import javax.net.ssl.SSLException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

@Slf4j
@Component
public class MemberUtil {

    private final WebClient webClient;

    public MemberUtil(@Value("${gateway.url}") String gatewayUrl) throws SSLException {
        ConnectionProvider provider = ConnectionProvider.builder("ApiConnections")
                                                        .maxConnections(16)
                                                        .maxIdleTime(Duration.ofSeconds(30))
                                                        .maxLifeTime(Duration.ofSeconds(60))
                                                        .pendingAcquireTimeout(Duration.ofSeconds(60))
                                                        .evictInBackground(Duration.ofSeconds(120))
                                                        .build();


        this.webClient = WebClient.builder()
                                  .baseUrl(gatewayUrl)
                                  .clientConnector(new ReactorClientHttpConnector(HttpClient.create(provider)))
                                  .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                  .build();
    }

    public Mono<MemberResponse> getMemberInfoByEmail(String userEmail) {
        log.info("[Booking:MemberUtil] getMemberInfoByEmail({})", userEmail);
        return makeRequest("/api/members/memberInfo/" + userEmail);
    }

    public Mono<MemberResponse> getMemberInfoByPk(Integer memberPk) {
        log.info("[Booking:MemberUtil] getMemberInfoByPk({})", memberPk);
        return makeRequest("/api/members/memberInfo-pk/" + memberPk);
    }

    public Mono<MemberResponse> getMemberInfoByNickname(String nickname) {
        log.info("[Booking:MemberUtil] getMemberInfoByNickname({})", nickname);
        return makeRequest("/api/members/memberInfo-nick/" + nickname);
    }

    private Mono<MemberResponse> makeRequest(String path) {
        return webClient.get()
                        .uri(path)
                        .retrieve()
                        .onStatus(HttpStatus::is4xxClientError,
                            response -> Mono.error(new RuntimeException("회원정보 응답 에러 4xx")))
                        .onStatus(HttpStatus::is5xxServerError,
                            response -> Mono.error(new RuntimeException("회원정보 응답 에러 5xx")))
                        .bodyToMono(MemberResponse.class);
    }
}
