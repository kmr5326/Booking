package com.booking.booking.meeting.service;

import com.booking.booking.global.dto.MemberInfoResponse;
import com.booking.booking.global.utils.MemberUtil;
import com.booking.booking.hashtag.dto.response.HashtagResponse;
import com.booking.booking.hashtagmeeting.domain.HashtagMeeting;
import com.booking.booking.hashtagmeeting.service.HashtagMeetingService;
import com.booking.booking.meeting.domain.Meeting;
import com.booking.booking.meeting.domain.MeetingState;
import com.booking.booking.meeting.dto.request.MeetingRequest;
import com.booking.booking.meeting.dto.response.MeetingResponse;
import com.booking.booking.meeting.repository.MeetingRepository;
import com.booking.booking.participant.service.ParticipantService;
import com.booking.booking.waitlist.service.WaitlistService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class MeetingService {
    private final MeetingRepository meetingRepository;
    private final HashtagMeetingService hashtagMeetingService;
    private final ParticipantService participantService;
    private final WaitlistService waitlistService;

    private static final String GATEWAY_URL = "http://localhost:8999";

    public Mono<Long> createMeeting(String userEmail, MeetingRequest meetingRequest) {
        log.info("Booking Server Meeting - createMeeting({}, {})", userEmail, meetingRequest);

        // TODO isbn 존재 여부 확인
        return MemberUtil.getMemberInfoByEmail(userEmail)
                .flatMap(memberInfo -> {
                    Meeting meeting = meetingRequest.toEntity(memberInfo, MeetingState.PREPARING);
                    return handleCreateMeeting(meeting, meetingRequest.hashtagList())
                            .subscribeOn(Schedulers.boundedElastic());
                })
                .onErrorResume(error -> {
                    log.error("Booking Server Meeting - Error during createMeeting : {}", error.getMessage());
                    return Mono.error(new RuntimeException("미팅 생성 실패"));
                });
    }

    private Mono<Long> handleCreateMeeting(Meeting meeting, List<String> hashtagList) {
        log.info("Booking Server Meeting - handleArrangeMeeting({}, {})", meeting.getMeetingTitle(), hashtagList);

        return Mono
                .fromCallable(() -> {
                    // TODO 채팅방 생성 요청
                    meetingRepository.save(meeting);
                    hashtagMeetingService.saveHashtags(meeting, hashtagList).block();
                    participantService.addParticipant(meeting, meeting.getLeaderId()).block();

                    return meeting.getMeetingId();
                })
                .subscribeOn(Schedulers.boundedElastic())
                .onErrorResume(error -> {
                    // TODO 롤백 어떻게 하지
                    log.error("Booking Server Meeting - Error during handleCreateMeeting : {}", error.getMessage());
                    return Mono.error(error);
                });
    }

    private Mono<MeetingResponse> makeMeetingResponse(Meeting meeting) {
        List<HashtagResponse> hashtagResponseList = meeting.getHashtagMeetingList().stream()
                .map(HashtagMeeting::getHashtag)
                .map(HashtagResponse::new)
                .collect(Collectors.toList());

        return Mono.just(new MeetingResponse(meeting, hashtagResponseList));
    }

    public Mono<MeetingResponse> findById(Long meetingId) {
        log.info("Booking Server Meeting - findById({})", meetingId);

        return Mono.justOrEmpty(meetingRepository.findById(meetingId))
                .switchIfEmpty(Mono.error(new RuntimeException("존재하지 않는 미팅")))
                .flatMap(this::makeMeetingResponse)
                .onErrorResume(error -> {
                    log.error("Booking Server Meeting - Error during findById : {}", error.getMessage());
                    return Mono.error(error);
                });
    }

    public Flux<MeetingResponse> findAllByLocation(String userEmail) {
        log.info("Booking Server Meeting - findAllByLocation({})", userEmail);

        // TODO 사용자 위치 기반
        return Mono.fromCallable(meetingRepository::findAll)
                .flatMapMany(Flux::fromIterable)
                .flatMap(this::makeMeetingResponse)
                .onErrorResume(error -> {
                    log.error("Booking Server Meeting - Error during findAllByLocation : {}", error.getMessage());
                    return Flux.error(new RuntimeException("미팅 목록 조회 실패"));
                });
    }

    public Mono<Void> enrollMeeting(String userEmail, Long meetingId) {
        log.info("Booking Server Meeting - enrollMeeting({}, {})", userEmail, meetingId);

        return Mono.zip(
                    Mono.justOrEmpty(meetingRepository.findById(meetingId))
                            .switchIfEmpty(Mono.error(new RuntimeException("존재하지 않는 모임"))),
                        MemberUtil.getMemberInfoByEmail(userEmail))
                .flatMap(it -> {
                    Meeting meeting = it.getT1();
                    Integer memberId = it.getT2().memberPk();

                    return Mono.zip(
                            participantService.existsByMeetingAndMemberId(meeting, memberId),
                            waitlistService.existsByMeetingAndMemberId(meeting, memberId))
                            .flatMap(it2 -> {
                                if (it2.getT1()) {
                                    return Mono.error(new RuntimeException("이미 등록한 회원"));
                                } else if (it2.getT2()) {
                                    return Mono.error(new RuntimeException("이미 대기 중인 회원"));
                                }
                                return waitlistService.enrollMeeting(meeting, memberId);
                            });
                })
                .onErrorResume(error -> {
                    log.error("Booking Server Meeting - Error during enrollMeeting : {}", error.getMessage());
                    return Mono.error(error);
                });
    }

    public Mono<Void> acceptMeeting(String userEmail, Long meetingId, Integer memberId) {
        log.info("Booking Server Meeting - acceptMeeting({}, {}, {})", userEmail, meetingId, memberId);

        // TODO 최대 인원 확인
        return Mono.zip(
                Mono.justOrEmpty(meetingRepository.findById(meetingId))
                        .switchIfEmpty(Mono.error(new RuntimeException("존재하지 않는 모임"))),
                MemberUtil.getMemberInfoByEmail(userEmail))
                .flatMap(it -> {
                    Meeting meeting = it.getT1();
                    Integer leaderId = it.getT2().memberPk();

                    if (!meeting.getLeaderId().equals(leaderId)) {
                        return Mono.error(new RuntimeException("모임 수락 권한 없음"));
                    }

                    return waitlistService.existsByMeetingAndMemberId(meeting, memberId)
                            .flatMap(exist -> {
                                if (!exist) {
                                    return Mono.error(new RuntimeException("대기 목록에 없는 회원"));
                                }
                                return Mono.empty();
                            })
                            .then(Mono.defer(() -> waitlistService.deleteByMeetingAndMemberId(meeting, memberId)))
                            .then(Mono.defer(() -> participantService.addParticipant(meeting, memberId)));
                })
                .onErrorResume(error -> {
                    log.error("Booking Server Meeting - Error during acceptMeeting : {}", error.getMessage());
                    return Mono.error(error);
                });



    }
}
