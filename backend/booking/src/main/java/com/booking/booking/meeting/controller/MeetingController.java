package com.booking.booking.meeting.controller;

import com.booking.booking.global.utils.JwtUtil;
import com.booking.booking.meeting.dto.request.MeetingRequest;
import com.booking.booking.meeting.dto.response.MeetingListResponse;
import com.booking.booking.meeting.service.MeetingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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

//    @GetMapping("/test")
//    public ResponseEntity<Flux<Long>> test() {
//        Flux<Long> hashtagFlux = meetingService.test().onErrorResume(error -> Mono.error(new RuntimeException("테스트 에러")));
//        return ResponseEntity.ok().body(hashtagFlux);
//    }

    @PostMapping("/")
    public Mono<ResponseEntity<Long>> createMeeting
            (@RequestHeader(AUTHORIZATION) String token, @RequestBody MeetingRequest meetingRequest) {
        String userEmail = JwtUtil.getLoginEmailByToken(token);

        // TODO 채팅방 생성 요청 실패했는데 200
        return meetingService.createMeeting(userEmail, meetingRequest)
                .map(meeting -> ResponseEntity.ok().body(meeting.getMeetingId()))
                .onErrorResume(error ->
                        Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, error.getMessage())));
    }

    @GetMapping("/")
    public ResponseEntity<Flux<MeetingListResponse>> findAllByLocation(@RequestHeader(AUTHORIZATION) String token) {
        String userEmail = JwtUtil.getLoginEmailByToken(token);

        Flux<MeetingListResponse> meetingResponseFlux = meetingService.findAllByLocation(userEmail)
                .onErrorResume(error ->
                        Flux.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, error.getMessage())));

        return ResponseEntity.ok().body(meetingResponseFlux);
    }


//    @GetMapping("/{meetingId}")
//    public Mono<ResponseEntity<MeetingDetailResponse>> findById(@PathVariable("meetingId") Long meetingId) {
//        return meetingService.findMeetingWithHashtags(meetingId)
//                .map(meeting -> ResponseEntity.ok().body(meeting))
//                .onErrorResume(error ->
//                        Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, error.getMessage())));
//    }



//    @PostMapping("/{meetingId}/waiting")
//    public Mono<ResponseEntity<Void>> enrollMeeting
//            (@RequestHeader(AUTHORIZATION) String token, @PathVariable("meetingId") Long meetingId) {
//        String userEmail = JwtUtil.getLoginEmailByToken(token);
//
//        return meetingService.enrollMeeting(userEmail, meetingId)
//                .thenReturn(ResponseEntity.ok().<Void>build())
//                .onErrorResume(error ->
//                        Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, error.getMessage())));
//    }
//
//    @PostMapping("/{meetingId}/accept/{memberId}")
//    public Mono<ResponseEntity<Void>> acceptMeeting(@RequestHeader(AUTHORIZATION) String token, @PathVariable("meetingId") Long meetingId, @PathVariable("memberId") Integer memberId) {
//        String userEmail = JwtUtil.getLoginEmailByToken(token);
//
//        return meetingService.acceptMeeting(userEmail, meetingId, memberId)
//                .thenReturn(ResponseEntity.ok().<Void>build())
//                .onErrorResume(error ->
//                        Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, error.getMessage())));
//    }
}
