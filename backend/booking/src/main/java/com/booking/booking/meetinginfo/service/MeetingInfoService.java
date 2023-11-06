//package com.booking.booking.meetinginfo.service;
//
//import com.booking.booking.meeting.domain.Meeting;
//import com.booking.booking.meeting.service.MeetingService;
//import com.booking.booking.meetinginfo.dto.request.MeetingInfoRequest;
//import com.booking.booking.meetinginfo.repository.MeetingInfoRepository;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Service;
//import reactor.core.publisher.Mono;
//import reactor.core.scheduler.Schedulers;
//
//@Slf4j
//@RequiredArgsConstructor
//@Service
//public class MeetingInfoService {
//    private final MeetingInfoRepository meetingInfoRepository;
//    private final MeetingService meetingService;
//
//    public Mono<Void> createMeetingDetail(String userEmail, MeetingInfoRequest meetingInfoRequest) {
//        log.info("Booking Server MeetingInfo - createMeetingDetail({}, {})", userEmail, meetingInfoRequest);
//
//        return meetingService.findById(meetingInfoRequest.meetingId())
//                .flatMap(meetingResponse -> {
//                    Meeting meeting = meetingResponse.toEntity();
//                    // TODO 존재하는 미팅인지, 방장 맞는지 확인
//                    return Mono.fromRunnable(() -> meetingInfoRepository.save(meetingInfoRequest.toEntity(meeting)))
//                            .subscribeOn(Schedulers.boundedElastic())
//                            .then();
//                })
//                .then();
//    }
//}
