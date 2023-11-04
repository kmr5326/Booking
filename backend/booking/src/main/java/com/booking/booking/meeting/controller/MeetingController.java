package com.booking.booking.meeting.controller;

import com.booking.booking.global.jwt.JwtUtil;
import com.booking.booking.meeting.dto.request.MeetingRequest;
import com.booking.booking.meeting.dto.response.MeetingResponse;
import com.booking.booking.meeting.service.MeetingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/booking/meeting")
@RestController
public class MeetingController {

    private final MeetingService meetingService;
    private static final String AUTHORIZATION = "Authorization";

    @PostMapping("/")
    public Mono<ResponseEntity<Void>> createMeeting(@RequestHeader(AUTHORIZATION) String token, @RequestBody MeetingRequest meetingRequest) {

        String userEmail = JwtUtil.getLoginEmailByToken(token);

        return meetingService.arrangeMeeting(userEmail, meetingRequest)
                .thenReturn(ResponseEntity.noContent().build());
    }

    @GetMapping("/{meetingId}")
    public Mono<ResponseEntity<MeetingResponse>> findById(@PathVariable("meetingId") Long meetingId) {
        return meetingService.findById(meetingId)
                .map(meeting -> ResponseEntity.ok().body(meeting));
    }

    @GetMapping("/")
    public ResponseEntity<Flux<MeetingResponse>> findAllById() {
        return ResponseEntity.ok().body(meetingService.findAll());
    }

    @GetMapping("/enroll/{meetingId}")
    public Mono<ResponseEntity<Void>> enrollMeeting(@RequestHeader(AUTHORIZATION) String token, @PathVariable("meetingId") Long meetingId) {
        String userEmail = JwtUtil.getLoginEmailByToken(token);

        return meetingService.enrollMeeting(userEmail, meetingId)
                .thenReturn(ResponseEntity.ok().build());
    }

}
