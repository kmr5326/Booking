package com.booking.booking.participantstate.service;

import com.booking.booking.participant.domain.Participant;
import com.booking.booking.participantstate.domain.ParticipantState;
import com.booking.booking.participantstate.repository.ParticipantStateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@Service
public class ParticipantStateService {
    private final ParticipantStateRepository participantStateRepository;

    public Flux<ParticipantState> findParticipantStatesByMeetingId(Long meetingId) {
        log.info("[Booking:ParticipantState] findParticipantStatesByMeetingId({})", meetingId);
        return participantStateRepository.findParticipantStatesByMeetingId(meetingId);
    }

    public Mono<ParticipantState> findByMeetingIdAndMemberId(Long meetingId, Integer memberId) {
        return participantStateRepository.findByMeetinginfoIdAndMemberId(meetingId, memberId);
    }

    public Mono<Void> startMeeting(Long meetingInfoId, Participant participant) {
        return participantStateRepository.save(ParticipantState.builder()
                .memberId(participant.getMemberId())
                .meetinginfoId(meetingInfoId)
                .attendanceStatus(false)
                .paymentStatus(false)
                .build())
                .then();
    }

    public Mono<Void> attendMeeting(ParticipantState participantState) {
        if (!participantState.getPaymentStatus()) {
            return Mono.error(new RuntimeException("참가비 x"));
        }
        return participantStateRepository.save(participantState.updateAttendance(true))
                .then();
    }

    public Mono<Void> payMeeting(ParticipantState participantState) {
        return participantStateRepository.save(participantState.updatePayment(true))
                .then();
    }
}
