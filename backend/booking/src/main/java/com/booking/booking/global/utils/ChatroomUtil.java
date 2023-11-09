package com.booking.booking.global.utils;

import com.booking.booking.global.dto.request.InitChatroomRequest;
import com.booking.booking.global.dto.request.JoinChatroomRequest;
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
public class ChatroomUtil {
    private static String GATEWAY_URL;

    @Value("${gateway.url}")
    public void setGatewayUrl(String gatewayUrl) {
        ChatroomUtil.GATEWAY_URL = gatewayUrl;
    }

    public static Mono<Void> initializeChatroom(InitChatroomRequest initChatroomRequest){
        log.info("[Booking:Meeting] initializeChatroom({})", initChatroomRequest);

        WebClient webClient = WebClient.builder().build();
        URI uri = URI.create(GATEWAY_URL + "/api/chat/room/");

        return webClient.post()
                .uri(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(initChatroomRequest), InitChatroomRequest.class)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError,
                        response -> Mono.error(new RuntimeException("채팅방 생성 에러")))
                .onStatus(HttpStatus::is5xxServerError,
                        response -> Mono.error(new RuntimeException("채팅방 생성 에러")))
                .bodyToMono(Void.class);
    }

    public static Mono<Void> joinChatroom(JoinChatroomRequest joinChatroomRequest){
        log.info("[Booking:Meeting] joinChatroom({})", joinChatroomRequest);

        WebClient webClient = WebClient.builder().build();
        URI uri = URI.create(GATEWAY_URL + "/api/chat/room/join");

        return webClient.post()
                .uri(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(joinChatroomRequest), JoinChatroomRequest.class)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError,
                        response -> Mono.error(new RuntimeException("채팅방 참가 에러")))
                .onStatus(HttpStatus::is5xxServerError,
                        response -> Mono.error(new RuntimeException("채팅방 참가 에러")))
                .bodyToMono(Void.class);
    }
}
