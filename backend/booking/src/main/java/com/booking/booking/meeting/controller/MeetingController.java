package com.booking.booking.meeting.controller;

import com.booking.booking.global.utils.JwtUtil;
import com.booking.booking.meeting.dto.request.MeetingRequest;
import com.booking.booking.meeting.dto.request.MeetingUpdateRequest;
import com.booking.booking.meeting.dto.response.MeetingDetailResponse;
import com.booking.booking.meeting.dto.response.MeetingListResponse;
import com.booking.booking.meeting.service.MeetingService;
import com.booking.booking.meetinginfo.dto.request.MeetingInfoRequest;
import com.booking.booking.post.dto.request.PostRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/booking/meeting")
@RestController
public class MeetingController {
    private final MeetingService meetingService;

    private static final String AUTHORIZATION = "Authorization";

    @PostMapping("/")
    public Mono<ResponseEntity<Long>> createMeeting(@RequestHeader(AUTHORIZATION) String token,
                                                    @RequestBody @Valid MeetingRequest meetingRequest) {
        String userEmail = JwtUtil.getLoginEmailByToken(token);

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

    @GetMapping("/hashtag/{hashtagId}")
    public ResponseEntity<Flux<MeetingListResponse>> findAllByHashtagId(@RequestHeader(AUTHORIZATION) String token,
                                                                        @PathVariable("hashtagId") Long hashtagId) {
        String userEmail = JwtUtil.getLoginEmailByToken(token);

        Flux<MeetingListResponse> meetingListResponseFlux = meetingService.findAllByHashtagId(userEmail, hashtagId)
                .onErrorResume(error ->
                        Flux.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, error.getMessage())));

        return ResponseEntity.ok().body(meetingListResponseFlux);
    }

    @GetMapping("/nickname/ongoing/{nickname}")
    public ResponseEntity<Flux<MeetingListResponse>> findOngoingByNickname(@PathVariable("nickname") String nickname) {
        Flux<MeetingListResponse> meetingListResponseFlux = meetingService.findOngoingByNickname(nickname)
                .onErrorResume(error ->
                        Flux.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, error.getMessage())));

        return ResponseEntity.ok().body(meetingListResponseFlux);
    }

    @GetMapping("/nickname/finish/{nickname}")
    public ResponseEntity<Flux<MeetingListResponse>> findFinishByNickname(@PathVariable("nickname") String nickname) {
        Flux<MeetingListResponse> meetingListResponseFlux = meetingService.findFinishByNickname(nickname)
                .onErrorResume(error ->
                        Flux.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, error.getMessage())));

        return ResponseEntity.ok().body(meetingListResponseFlux);
    }

    @GetMapping("/detail/{meetingId}")
    public Mono<ResponseEntity<MeetingDetailResponse>> findById(@PathVariable("meetingId") Long meetingId) {
        return meetingService.findByMeetingId(meetingId)
                .map(meeting -> ResponseEntity.ok().body(meeting))
                .onErrorResume(error ->
                        Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, error.getMessage())));
    }

    @PostMapping("/{meetingId}/waiting")
    public Mono<ResponseEntity<Void>> enrollMeeting(@RequestHeader(AUTHORIZATION) String token,
                                                    @PathVariable("meetingId") Long meetingId) {
        String userEmail = JwtUtil.getLoginEmailByToken(token);

        return meetingService.enrollMeeting(userEmail, meetingId)
                .thenReturn(ResponseEntity.ok().<Void>build())
                .onErrorResume(error ->
                        Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, error.getMessage())));
    }

    @PostMapping("/{meetingId}/accept/{memberId}")
    public Mono<ResponseEntity<Void>> acceptMeeting(@RequestHeader(AUTHORIZATION) String token,
                                                    @PathVariable("meetingId") Long meetingId,
                                                    @PathVariable("memberId") Integer memberId) {
        String userEmail = JwtUtil.getLoginEmailByToken(token);

        return meetingService.acceptMeeting(userEmail, meetingId, memberId)
                .thenReturn(ResponseEntity.ok().<Void>build())
                .onErrorResume(error ->
                        Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, error.getMessage())));
    }

    @PostMapping("/{meetingId}/reject/{memberId}")
    public Mono<ResponseEntity<Void>> rejectMeeting(@RequestHeader(AUTHORIZATION) String token,
                                                    @PathVariable("meetingId") Long meetingId,
                                                    @PathVariable("memberId") Integer memberId) {
        String userEmail = JwtUtil.getLoginEmailByToken(token);

        return meetingService.rejectMeeting(userEmail, meetingId, memberId)
                .thenReturn(ResponseEntity.ok().<Void>build())
                .onErrorResume(error ->
                        Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, error.getMessage())));
    }

    @DeleteMapping("/{meetingId}/exit")
    public Mono<ResponseEntity<Void>> exitMeeting(@RequestHeader(AUTHORIZATION) String token,
                                                  @PathVariable("meetingId") Long meetingId) {
        String userEmail = JwtUtil.getLoginEmailByToken(token);

        return meetingService.exitMeeting(userEmail, meetingId)
                .thenReturn(ResponseEntity.ok().<Void>build())
                .onErrorResume(error ->
                        Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, error.getMessage())));
    }

    @PostMapping("/info/")
    public Mono<ResponseEntity<Void>> createDetailedMeeting(@RequestHeader(AUTHORIZATION) String token,
                                                            @RequestBody MeetingInfoRequest meetingInfoRequest) {
        String userEmail = JwtUtil.getLoginEmailByToken(token);

        return meetingService.createMeetingInfo(userEmail, meetingInfoRequest)
                .thenReturn(ResponseEntity.ok().<Void>build())
                .onErrorResume(error ->
                        Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, error.getMessage())));
    }

    @PatchMapping("/")
    public Mono<ResponseEntity<Void>> updateMeeting(@RequestHeader(AUTHORIZATION) String token,
                                                    @RequestBody @Valid MeetingUpdateRequest meetingUpdateRequest) {
        String userEmail = JwtUtil.getLoginEmailByToken(token);

        return meetingService.updateMeeting(userEmail, meetingUpdateRequest)
                .thenReturn(ResponseEntity.ok().<Void>build())
                .onErrorResume(error ->
                        Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, error.getMessage())));
    }

    @DeleteMapping("/{meetingId}")
    public Mono<ResponseEntity<Void>> deleteMeeting(@RequestHeader(AUTHORIZATION) String token,
                                                    @PathVariable("meetingId") Long meetingId) {
        String userEmail = JwtUtil.getLoginEmailByToken(token);

        return meetingService.deleteMeeting(userEmail, meetingId)
                .thenReturn(ResponseEntity.ok().<Void>build())
                .onErrorResume(error ->
                        Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, error.getMessage())));
    }

    @PostMapping("/post/")
    public Mono<ResponseEntity<Long>> createPost(@RequestHeader(AUTHORIZATION) String token,
                                                 @RequestBody PostRequest postRequest) {
        String userEmail = JwtUtil.getLoginEmailByToken(token);

        return meetingService.createPost(userEmail, postRequest)
                .map(post -> ResponseEntity.ok().body(post.getPostId()))
                .onErrorResume(error ->
                        Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, error.getMessage())));
    }
}
