package com.booking.booking.participant.service;

import com.booking.booking.global.exception.ErrorCode;
import com.booking.booking.meeting.domain.Meeting;
import com.booking.booking.meeting.exception.MeetingException;
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

    public Mono<Void> addParticipant(Meeting meeting) {
        return Mono.just(meeting)
                .flatMap(data -> {
                    Participant participant = buildParticipant(data);
                    return Mono.fromRunnable(() -> participantRepository.save(participant))
                            .subscribeOn(Schedulers.boundedElastic())
                            .then();
                })
                .doOnError(error -> {
                    log.error("Error during arrangeMeeting : {}", error.toString());
                    throw new MeetingException(ErrorCode.CREATED_MEETING_FAILURE);
                });
    }

    private Participant buildParticipant(Meeting meeting) {
        return Participant.builder()
                .meeting(meeting)
                .memberId(meeting.getLeaderId())
                .attendanceStatus(false)
                .paymentStatus(false)
                .build();
    }
}
