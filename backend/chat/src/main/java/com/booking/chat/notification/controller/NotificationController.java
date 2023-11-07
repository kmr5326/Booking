package com.booking.chat.notification.controller;

import com.booking.chat.global.jwt.JwtUtil;
import com.booking.chat.notification.dto.request.DeviceTokenInitRequest;
import com.booking.chat.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/notification")
@RestController
public class NotificationController {

    private final NotificationService notificationService;
    private static final String AUTHORIZATION = "Authorization";

    @PostMapping("/init")
    public Mono<ResponseEntity<Void>> initializeDeviceToken(@RequestHeader(AUTHORIZATION) String token, @RequestBody DeviceTokenInitRequest deviceTokenInitRequest) {
        Long memberId = JwtUtil.getMemberIdByToken(token);
        log.info(" {} user request initialize device token ", memberId);


        return Mono.empty();
    }
}
