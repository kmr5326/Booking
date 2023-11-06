package com.booking.booking.participant.service;

import com.booking.booking.global.utils.MemberUtil;
import com.booking.booking.meeting.domain.Meeting;
import com.booking.booking.participant.domain.Participant;
import com.booking.booking.participant.dto.response.ParticipantResponse;
import com.booking.booking.participant.repository.ParticipantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Slf4j
@RequiredArgsConstructor
@Service
public class ParticipantService {
    private final ParticipantRepository participantRepository;

    public Mono<Void> addParticipant(Meeting meeting, Integer memberId) {
        log.info("Booking Server Participant - addParticipant({}, {})", meeting, memberId);

        return Mono
                .fromCallable(() -> buildParticipant(meeting, memberId))
                        .flatMap(participant -> Mono.fromRunnable(() -> participantRepository.save(participant))
                            .subscribeOn(Schedulers.boundedElastic())
                            .then()
                        )
                .doOnError(error -> {
                    log.error("Error during addParticipant : {}", error.toString());
                    throw new RuntimeException("참가자 추가 실패");
                });
    }

    private Participant buildParticipant(Meeting meeting, Integer memberId) {
        return Participant.builder()
                .meeting(meeting)
                .memberId(memberId)
                .attendanceStatus(false)
                .paymentStatus(false)
                .build();
    }

    public Mono<Boolean> existsByMeetingAndMemberId(Meeting meeting, Integer memberId) {
        log.info("Booking Server Participant - existsByMeetingAndMemberId({}, {})", meeting, memberId);

        return Mono
                .fromCallable(() -> participantRepository.existsByMeetingAndMemberId(meeting, memberId))
                .subscribeOn(Schedulers.boundedElastic());
    }

    public Flux<ParticipantResponse> findAllByMeetingId(Long meetingId) {
        log.info("Booking Server Participant - findAllByMeetingId({})", meetingId);

        return Mono.fromCallable(() -> participantRepository.findAllByMeetingMeetingId(meetingId))
                .flatMapMany(Flux::fromIterable)
                .flatMap(participant -> MemberUtil.getMemberInfoByPk(participant.getMemberId())
                        .flatMap(memberInfo -> Mono.just(new ParticipantResponse(memberInfo, participant))))
                .onErrorResume(error -> {
                    log.error("Booking Server Participant - Error during findAllByMeetingId : {}", error.getMessage());
                    return Flux.error(new RuntimeException("참가자 목록 조회 실패"));
                })
                .subscribeOn(Schedulers.boundedElastic());
    }
}
