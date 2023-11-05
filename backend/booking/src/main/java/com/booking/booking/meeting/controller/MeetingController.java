package com.booking.booking.meeting.controller;

import com.booking.booking.global.jwt.JwtUtil;
import com.booking.booking.meeting.dto.request.MeetingRequest;
import com.booking.booking.meeting.dto.response.MeetingResponse;
import com.booking.booking.meeting.service.MeetingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
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
    public Mono<ResponseEntity<Long>> createMeeting
            (@RequestHeader(AUTHORIZATION) String token, @RequestBody MeetingRequest meetingRequest) {
        String userEmail = JwtUtil.getLoginEmailByToken(token);

        return meetingService.createMeeting(userEmail, meetingRequest)
                .map(meetingId -> ResponseEntity.ok().body(meetingId))
                .onErrorResume(error ->
                        Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, error.getMessage())));
    }

    @GetMapping("/{meetingId}")
    public Mono<ResponseEntity<MeetingResponse>> findById(@PathVariable("meetingId") Long meetingId) {
        return meetingService.findById(meetingId)
                .map(meeting -> ResponseEntity.ok().body(meeting))
                .onErrorResume(error ->
                        Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, error.getMessage())));
    }

    @GetMapping("/")
    public ResponseEntity<Flux<MeetingResponse>> findAllByLocation(@RequestHeader(AUTHORIZATION) String token) {
        String userEmail = JwtUtil.getLoginEmailByToken(token);

        Flux<MeetingResponse> meetingResponseFlux = meetingService.findAllByLocation(userEmail)
                .onErrorResume(error ->
                        Flux.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, error.getMessage())));

        return ResponseEntity.ok().body(meetingResponseFlux);
    }

    @PostMapping("/{meetingId}/waiting")
    public Mono<ResponseEntity<Void>> enrollMeeting
            (@RequestHeader(AUTHORIZATION) String token, @PathVariable("meetingId") Long meetingId) {
        String userEmail = JwtUtil.getLoginEmailByToken(token);

        return meetingService.enrollMeeting(userEmail, meetingId)
                .thenReturn(ResponseEntity.ok().<Void>build())
                .onErrorResume(error ->
                        Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, error.getMessage())));
    }

    @PostMapping("/{meetingId}/accept/{memberId}")
    public Mono<ResponseEntity<Void>> acceptMeeting(@RequestHeader(AUTHORIZATION) String token, @PathVariable("meetingId") Long meetingId, @PathVariable("memberId") Integer memberId) {
        String userEmail = JwtUtil.getLoginEmailByToken(token);

        return meetingService.acceptMeeting(userEmail, meetingId, memberId)
                .thenReturn(ResponseEntity.ok().<Void>build())
                .onErrorResume(error ->
                        Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, error.getMessage())));
    }
}
