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

@Slf4j
@RequiredArgsConstructor
@Service
public class ParticipantService {
    private final ParticipantRepository participantRepository;

    public Mono<Integer> countAllByMeetingId(Long meetingId) {
        return participantRepository.countAllByMeetingId(meetingId);
    }

    public Mono<Void> addParticipant(Meeting meeting, Integer memberId) {
        log.info("Booking Server Participant - addParticipant({}, {})", meeting, memberId);
        
        // TODO status 확인 - preparing 상태일 때만 추가 가능
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
                    log.error("Error during addParticipant : {}", error.toString());
                    throw new RuntimeException("참가자 추가 실패");
                });
    }

    public Flux<ParticipantResponse> findAllByMeetingId(Long meetingId) {
        log.info("Booking Server Participant - findAllByMeetingId({})", meetingId);

        return participantRepository.findAllByMeetingId(meetingId)
                .flatMap(participant -> MemberUtil.getMemberInfoByPk(participant.getMemberId())
                        .flatMap(member -> Mono.just(new ParticipantResponse(member, participant))))
                .onErrorResume(error -> {
                    log.error("Error during findAllByMeetingId : {}", error.toString());
                    return Flux.error(new RuntimeException("참가자 목록 조회 실패"));
                });
    }

    public Mono<Boolean> existsByMeetingIdAndMemberId(Long meetingId, Integer memberId) {
        log.info("Booking Server Participant - existsByMeetingIdAndMemberId({}, {})", meetingId, memberId);

        return participantRepository.existsByMeetingIdAndMemberId(meetingId, memberId);
    }

    //    public Flux<ParticipantResponse> findAllByMeetingId(Long meetingId) {
//        log.info("Booking Server Participant - findAllByMeetingId({})", meetingId);
//
//        return Mono.fromCallable(() -> participantRepository.findAllByMeetingMeetingId(meetingId))
//                .flatMapMany(Flux::fromIterable)
//                .flatMap(participant -> MemberUtil.getMemberInfoByPk(participant.getMemberId())
//                        .flatMap(memberInfo -> Mono.just(new ParticipantResponse(memberInfo, participant))))
//                .onErrorResume(error -> {
//                    log.error("Booking Server Participant - Error during findAllByMeetingId : {}", error.getMessage());
//                    return Flux.error(new RuntimeException("참가자 목록 조회 실패"));
//                })
//                .subscribeOn(Schedulers.boundedElastic());
//    }
}
