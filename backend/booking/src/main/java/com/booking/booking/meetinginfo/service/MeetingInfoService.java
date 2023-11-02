package com.booking.booking.meetinginfo.service;

import com.booking.booking.meetinginfo.dto.request.MeetingInfoRequest;
import com.booking.booking.meetinginfo.repository.MeetingInfoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@Service
public class MeetingInfoService {
    private final MeetingInfoRepository meetingInfoRepository;

    public Mono<Void> createMeeting(String userEmail, MeetingInfoRequest meetingInfoRequest) {
        return Mono.empty();
    }
}
