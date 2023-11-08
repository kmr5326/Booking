package com.booking.booking.meetinginfo.controller;

import com.booking.booking.global.utils.JwtUtil;
import com.booking.booking.meetinginfo.dto.request.MeetingInfoRequest;
import com.booking.booking.meetinginfo.service.MeetingInfoService;
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
@RequestMapping("/api/booking/meeting/info")
@RestController
public class MeetingInfoController {
    private final MeetingInfoService meetingInfoService;

    private static final String AUTHORIZATION = "Authorization";

    @PostMapping("/")
    public Mono<ResponseEntity<Void>> createDetailedMeeting
            (@RequestHeader(AUTHORIZATION) String token, @RequestBody MeetingInfoRequest meetingInfoRequest) {
        String userEmail = JwtUtil.getLoginEmailByToken(token);

        // TODO 에러 처리
        return meetingInfoService.createMeetingInfo(userEmail, meetingInfoRequest)
                .thenReturn(ResponseEntity.noContent().build());
    }
}
