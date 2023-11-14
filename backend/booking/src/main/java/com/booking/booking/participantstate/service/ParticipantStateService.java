package com.booking.booking.participantstate.service;

import com.booking.booking.meetinginfo.domain.MeetingInfo;
import com.booking.booking.participant.service.ParticipantService;
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
    private final ParticipantService participantService;

    public Flux<ParticipantState> findParticipantStatesByMeetingId(Long meetingId) {
        return participantStateRepository.findParticipantStatesByMeetingId(meetingId);
    }

    public Mono<ParticipantState> findByMeetingIdAndMemberId(Long meetingId, Integer memberId) {
        return participantStateRepository.findByMeetinginfoIdAndAndMemberId(meetingId, memberId);
    }

    public Mono<Void> startMeeting(MeetingInfo meetingInfo) {
        return participantService.findAllByMeetingId(meetingInfo.getMeetingId())
                .flatMap(participant ->
                        participantStateRepository.save(ParticipantState.builder()
                                .memberId(participant.getMemberId())
                                .meetinginfoId(meetingInfo.getMeetinginfoId())
                                .attendanceStatus(false)
                                .paymentStatus(false)
                                .build()))
                .then();
    }

    public Mono<Void> attendMeeting(MeetingInfo meetingInfo) {
        return participantStateRepository.findById(meetingInfo.getMeetinginfoId())
                .flatMap(participantState -> {
                    if (!participantState.getPaymentStatus()) {
                        return Mono.error(new RuntimeException("참가비 x"));
                    }
                    return participantStateRepository.save(participantState.updateAttendance(true));
                })
                .then();
    }

    public Mono<Void> payMeeting(MeetingInfo meetingInfo) {
        return participantStateRepository.findById(meetingInfo.getMeetinginfoId())
                .flatMap(participantState -> participantStateRepository.save(participantState.updatePayment(true)))
                .then();
    }
}
