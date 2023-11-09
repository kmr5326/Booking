package com.booking.booking.participant.service;

import com.booking.booking.global.utils.MemberUtil;
import com.booking.booking.meeting.domain.Meeting;
import com.booking.booking.meeting.domain.MeetingState;
import com.booking.booking.participant.domain.Participant;
import com.booking.booking.participant.dto.response.ParticipantResponse;
import com.booking.booking.participant.repository.ParticipantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@Service
public class ParticipantService {
    private final ParticipantRepository participantRepository;

    public Mono<Integer> countAllByMeetingId(Long meetingId) {
        log.info("[Booking:Participant] countAllByMeetingId({})", meetingId);

        return participantRepository.countAllByMeetingId(meetingId);
    }

    public Mono<Void> addParticipant(Meeting meeting, Integer memberId) {
        log.info("[Booking:Participant] addParticipant({}, {})", meeting, memberId);
        
        if (!meeting.getMeetingState().equals(MeetingState.PREPARING)) {
            return Mono.error(new RuntimeException("모임 진행 중에는 참여 불가"));
        }

        return participantRepository.countAllByMeetingId(meeting.getMeetingId())
                .flatMap(count -> {
                    if (count >= meeting.getMaxParticipants()) {
                        return Mono.error(new RuntimeException("풀방"));
                    }
                    return participantRepository.save(
                            Participant.builder()
                                    .memberId(memberId).meetingId(meeting.getMeetingId())
                                    .attendanceStatus(false).paymentStatus(false)
                                    .build());
                })
                .then()
                .doOnError(error -> {
                    log.error("[Booking:Participant ERROR] addParticipant : {}", error.getMessage());
                    throw new RuntimeException("참가자 추가 실패");
                });
    }

    public Flux<ParticipantResponse> findAllByMeetingId(Long meetingId) {
        log.info("[Booking:Participant] findAllByMeetingId({})", meetingId);

        return participantRepository.findAllByMeetingId(meetingId)
                .flatMap(participant -> MemberUtil.getMemberInfoByPk(participant.getMemberId())
                        .flatMap(member -> Mono.just(new ParticipantResponse(member, participant))))
                .onErrorResume(error -> {
                    log.error("[Booking:Participant ERROR] findAllByMeetingId : {}", error.getMessage());
                    return Flux.error(new RuntimeException("참가자 목록 조회 실패"));
                });
    }

    public Mono<Boolean> existsByMeetingIdAndMemberId(Long meetingId, Integer memberId) {
        log.info("[Booking:Participant] existsByMeetingIdAndMemberId({}, {})", meetingId, memberId);

        return participantRepository.existsByMeetingIdAndMemberId(meetingId, memberId);
    }

    public Mono<Void> deleteAllByMeetingId(Long meetingId) {
        log.info("[Booking:Participant] deleteAllByMeetingId({})", meetingId);

        return participantRepository.deleteAllByMeetingId(meetingId);
    }
}
