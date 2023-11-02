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
        return Mono
                .fromCallable(() -> buildParticipant(meeting))
                        .flatMap(participant -> Mono.fromRunnable(() -> participantRepository.save(participant))
                            .subscribeOn(Schedulers.boundedElastic())
                            .then()
                        )
                .doOnError(error -> {
                    log.error("Error during addParticipant : {}", error.toString());
                    throw new MeetingException(ErrorCode.ADD_PARTICIPANT_FAILURE);
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

//    public Flux<Participant> findAllMemberByMeeting(Meeting meeting) {
//        return Mono
//                .fromCallable(() -> participantRepository.findAllByMeeting(meeting))
//                .subscribeOn(Schedulers.boundedElastic())
//                .flatMapMany(Flux::fromIterable);
//    }

    public Mono<Boolean> existsParticipantByMeetingAndMemberId(Meeting meeting, String memberId) {
        log.info("existsParticipantByMeetingAndMemberId inside");
        return Mono
                .fromCallable(() -> participantRepository.existsParticipantByMeetingAndMemberId(meeting, memberId))
                .subscribeOn(Schedulers.boundedElastic());
    }


}
