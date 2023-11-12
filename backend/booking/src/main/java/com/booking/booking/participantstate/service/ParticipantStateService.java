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
}
