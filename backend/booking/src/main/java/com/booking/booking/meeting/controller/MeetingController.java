package com.booking.booking.meeting.controller;

import com.booking.booking.meeting.service.MeetingService;
import com.booking.booking.global.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;


@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/booking/meeting")
@RestController
public class MeetingController {

    private final MeetingService meetingService;
    private static final String AUTHORIZATION = "Authorization";

    @PostMapping("/")
    public Mono<ResponseEntity<Void>> createMeeting(@RequestHeader(AUTHORIZATION) String token) {

        String userEmail = JwtUtil.getLoginEmailByToken(token);
        meetingService.arrangeMeeting(userEmail);


        return Mono.just(ResponseEntity.noContent().build());
    }

}
