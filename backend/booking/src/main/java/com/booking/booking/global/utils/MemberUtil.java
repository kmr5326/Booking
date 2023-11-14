package com.booking.booking.global.utils;

import com.booking.booking.global.dto.request.ReSendRequestDto;
import com.booking.booking.global.dto.response.MemberResponse;
import io.netty.channel.ChannelOption;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
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

import javax.net.ssl.SSLException;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class MemberUtil {

    private final WebClient webClient;
    private final String AUTHORIZATION = "Authorization";
    private final String RECIEVER = "kakao_3144707067";
//    private final String RECIEVER = "google_113902342240727897270";

    public MemberUtil(@Value("${gateway.url}") String gatewayUrl) throws SSLException {
        ConnectionProvider provider = ConnectionProvider.builder("ApiConnections")
                                                        .maxConnections(16)
                                                        .maxIdleTime(Duration.ofSeconds(30))
                                                        .pendingAcquireTimeout(Duration.ofSeconds(45))
                                                        .evictInBackground(Duration.ofSeconds(30))
                                                        .lifo()
                                                        .metrics(true)
                                                        .build();
        SslContext sslContext = SslContextBuilder
            .forClient()
            .build();

        HttpClient httpClient = HttpClient.create(provider)
                                          .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 1000 * 30)
                                          .doOnConnected(conn ->
                                              conn.addHandlerLast(new ReadTimeoutHandler(45, TimeUnit.SECONDS))
                                                  .addHandlerLast(new WriteTimeoutHandler(10, TimeUnit.SECONDS)))
                                          .responseTimeout(Duration.ofSeconds(60))
                                          .secure(spec -> spec.sslContext(sslContext)
                                                              .handshakeTimeout(Duration.ofSeconds(20)));

        this.webClient = WebClient.builder()
                                  .baseUrl(gatewayUrl)
                                  .clientConnector(new ReactorClientHttpConnector(httpClient))
                                  .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                  .build();
    }

    public Mono<MemberResponse> getMemberInfoByEmail(String userEmail) {
        log.info("[Booking:MemberUtil] getMemberInfoByEmail({})", userEmail);
        return makeGetRequest("/api/members/memberInfo/" + userEmail);
    }

    public Mono<MemberResponse> getMemberInfoByPk(Integer memberPk) {
        log.info("[Booking:MemberUtil] getMemberInfoByPk({})", memberPk);
        return makeGetRequest("/api/members/memberInfo-pk/" + memberPk);
    }

    private Mono<MemberResponse> makeGetRequest(String path) {
        return webClient.get()
                        .uri(path)
                        .retrieve()
                        .onStatus(HttpStatus::is4xxClientError,
                            response -> Mono.error(new RuntimeException("회원정보 응답 에러")))
                        .onStatus(HttpStatus::is5xxServerError,
                            response -> Mono.error(new RuntimeException("회원정보 응답 에러")))
                        .bodyToMono(MemberResponse.class);
    }

    public Mono<Void> payRequest(String token, Integer fee) {
        return Mono.empty();
        //        return webClient.post()
//                .uri("/api/payments/send")
//                .header(AUTHORIZATION, token)
//                .contentType(MediaType.APPLICATION_JSON)
//                .body(Mono.just(new PaymentRequest(RECIEVER, fee)), PaymentRequest.class)
//                .retrieve()
//                .onStatus(HttpStatus::is4xxClientError,
//                        response -> Mono.error(new RuntimeException("참가비 응답 에러")))
//                .onStatus(HttpStatus::is5xxServerError,
//                        response -> Mono.error(new RuntimeException("참가비 응답 에러")))
//                .bodyToMono(Void.class);
    }

    public Mono<String> paybackRequest(Integer memberId, Integer amount) {
        return webClient.post()
                .uri("/api/payments/resend")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(new ReSendRequestDto(memberId, amount)), ReSendRequestDto.class)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError,
                        response -> Mono.error(new RuntimeException("참가비 응답 에러")))
                .onStatus(HttpStatus::is5xxServerError,
                        response -> Mono.error(new RuntimeException("참가비 응답 에러")))
                .bodyToMono(String.class);
    }
}
