package com.booking.booking.participant.service;

import com.booking.booking.meeting.domain.Meeting;
import com.booking.booking.participant.domain.Participant;
import com.booking.booking.participant.repository.ParticipantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Slf4j
@RequiredArgsConstructor
@Service
public class ParticipantService {
    private final ParticipantRepository participantRepository;

    public Mono<Void> addParticipant(Meeting meeting, Integer memberId) {
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
        log.info("existsParticipantByMeetingAndMemberId inside");
        return Mono
                .fromCallable(() -> participantRepository.existsByMeetingAndMemberId(meeting, memberId))
                .subscribeOn(Schedulers.boundedElastic());
    }

}
