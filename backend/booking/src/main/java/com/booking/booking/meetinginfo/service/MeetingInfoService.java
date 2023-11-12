package com.booking.booking.meetinginfo.service;

import com.booking.booking.meetinginfo.domain.MeetingInfo;
import com.booking.booking.meetinginfo.dto.response.MeetingInfoResponse;
import com.booking.booking.meetinginfo.repository.MeetingInfoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
@Service
public class MeetingInfoService {
    private final MeetingInfoRepository meetingInfoRepository;

    public Mono<MeetingInfo> createMeetingInfo(MeetingInfo meetingInfo) {
        log.info("[Booking:MeetingInfo] createMeetingInfo({})", meetingInfo);

        if (meetingInfo.getDate().isBefore(LocalDateTime.now().plusHours(1L))) {
            return Mono.error(new RuntimeException("시간 다시"));
        }
        return meetingInfoRepository.save(meetingInfo)
                .onErrorResume(error -> {
                    log.error("[Booking:MeetingInfo ERROR] createMeetingInfo : {}", error.getMessage());
                    return Mono.error(new RuntimeException("미팅정보 저장 실패"));
                });
    }

    public Flux<MeetingInfoResponse> findAllByMeetingId(Long meetingId) {
        log.info("[Booking:MeetingInfo] findAllByMeetingId({})", meetingId);

        return meetingInfoRepository.findAllByMeetingId(meetingId)
                .flatMap(meetingInfo -> Mono.just(new MeetingInfoResponse(meetingInfo)))
                .onErrorResume(error -> {
                    log.error("[Booking:MeetingInfo ERROR] findAllByMeetingId : {}", error.getMessage());
                    return Mono.error(new RuntimeException("미팅정보 목록 조회 실패"));
                });
    }
}
