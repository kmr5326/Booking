package com.booking.booking.global.utils;

import com.booking.booking.global.dto.request.EnrollNotificationRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.URI;

@Slf4j
@Component
public class NotificationUtil {
    private static String GATEWAY_URL;

    @Value("${gateway.url}")
    public void setGatewayUrl(String gatewayUrl) {
        NotificationUtil.GATEWAY_URL = gatewayUrl;
    }

    public static Mono<Void> enrollNotification(EnrollNotificationRequest enrollNotificationRequest) {
        log.info("[Booking:NotificationUtil] enrollNotification({})", enrollNotificationRequest);

        WebClient webClient = WebClient.builder().build();
        URI uri = URI.create(GATEWAY_URL + "/api/notification/enroll");

        return webClient.post()
                .uri(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(enrollNotificationRequest), EnrollNotificationRequest.class)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError,
                        response -> Mono.error(new RuntimeException("알람 응답 에러")))
                .onStatus(HttpStatus::is5xxServerError,
                        response -> Mono.error(new RuntimeException("알람 응답 에러")))
                .bodyToMono(Void.class);
    }
}
