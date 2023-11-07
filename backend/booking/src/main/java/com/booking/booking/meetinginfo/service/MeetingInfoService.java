package com.booking.booking.meetinginfo.service;

import com.booking.booking.meetinginfo.dto.request.MeetingInfoRequest;
import com.booking.booking.meetinginfo.dto.response.MeetingInfoResponse;
import com.booking.booking.meetinginfo.repository.MeetingInfoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@Service
public class MeetingInfoService {
    private final MeetingInfoRepository meetingInfoRepository;
//    private final MeetingService meetingService;

    public Mono<Void> createMeetingInfo(String userEmail, MeetingInfoRequest meetingInfoRequest) {
        log.info("Booking Server MeetingInfo - createMeetingDetail({}, {})", userEmail, meetingInfoRequest);

        // TODO 방장 맞는지 확인, 미팅 있는지 확인
//        return meetingService.findByMeetingId(meetingInfoRequest.meetingId())
//                .flatMap(meetingResponse -> meetingInfoRepository.save(meetingInfoRequest.toEntity()))
//                .then();
        return meetingInfoRepository.save(meetingInfoRequest.toEntity()).then();
    }

    public Flux<MeetingInfoResponse> findAllByMeetingId(Long meetingId) {
        return meetingInfoRepository.findAllByMeetingId(meetingId)
                .flatMap(meetingInfo -> Mono.just(new MeetingInfoResponse(meetingInfo)));
    }
}
