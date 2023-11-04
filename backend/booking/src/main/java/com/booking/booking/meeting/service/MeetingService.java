package com.booking.booking.meeting.service;

import com.booking.booking.chatroom.service.ChatroomService;
import com.booking.booking.global.exception.ErrorCode;
import com.booking.booking.hashtag.dto.response.HashtagResponse;
import com.booking.booking.hashtag.service.HashtagService;
import com.booking.booking.hashtagmeeting.domain.HashtagMeeting;
import com.booking.booking.hashtagmeeting.service.HashtagMeetingService;
import com.booking.booking.meeting.domain.Meeting;
import com.booking.booking.meeting.dto.request.MeetingRequest;
import com.booking.booking.meeting.dto.response.MeetingResponse;
import com.booking.booking.meeting.dto.response.MemberInfoResponse;
import com.booking.booking.meeting.exception.MeetingException;
import com.booking.booking.meeting.repository.MeetingRepository;
import com.booking.booking.participant.service.ParticipantService;
import com.booking.booking.waitlist.service.WaitlistService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class MeetingService {
    private final MeetingRepository meetingRepository;
    private final HashtagMeetingService hashtagMeetingService;
    private final HashtagService hashtagService;
    private final ChatroomService chatroomService;
    private final ParticipantService participantService;
    private final WaitlistService waitlistService;

    private static final String GATEWAY_URL = "http://localhost:8999";

    public Mono<Void> arrangeMeeting(String userEmail, MeetingRequest meetingRequest) {
        log.info("Booking Server - '{}' request arrangeMeeting", meetingRequest.toString());

        return getMemberInfoByEmail(userEmail)
                .flatMap(memberInfo -> {
                    Meeting meeting = meetingRequest.toEntity(memberInfo);
                    return Mono.fromRunnable(() -> handleArrangeMeeting(meeting, meetingRequest.hashtagList()))
                            .subscribeOn(Schedulers.boundedElastic())
                            .then();
                })
                .doOnError(error -> {
                    log.error("Error during arrangeMeeting : {}", error.toString());
                    throw new MeetingException(ErrorCode.CREATE_MEETING_FAILURE);
                });
    }

    private Mono<MemberInfoResponse> getMemberInfoByEmail(String userEmail) {
        log.info("start inter-server comm");
        WebClient webClient = WebClient.builder().build();
        URI uri = URI.create(GATEWAY_URL + "/api/members/memberInfo/" + userEmail);

        Mono<MemberInfoResponse> memberInfoResponse = webClient.get()
                .uri(uri)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, response -> Mono.error(RuntimeException::new))
                .onStatus(HttpStatus::is5xxServerError, response -> Mono.error(RuntimeException::new))
                .bodyToMono(MemberInfoResponse.class);

        log.info("end inter-server comm");
        return memberInfoResponse;
    }

    private void handleArrangeMeeting(Meeting meeting, List<String> hashtagList) {
        meetingRepository.save(meeting);
        hashtagMeetingService.saveHashtags(meeting, hashtagList).subscribe();
        chatroomService.createChatroom(meeting).subscribe();
        participantService.addParticipant(meeting).subscribe();
    }

    public Mono<MeetingResponse> findById(Long id) {
        return Mono.justOrEmpty(meetingRepository.findById(id))
                .flatMap(meeting -> {
                    List<HashtagResponse> hashtagResponseList = meeting.getHashtagMeetingList().stream()
                            .map(HashtagMeeting::getHashtag)
                            .map(HashtagResponse::new)
                            .collect(Collectors.toList());

                    return Mono.just(new MeetingResponse(meeting, hashtagResponseList));
                })
                .switchIfEmpty(Mono.error(new MeetingException(ErrorCode.GET_MEETING_FAILURE)));
    }

    public Flux<MeetingResponse> findAll() {
        return Flux.fromIterable(meetingRepository.findAll())
                .flatMap(meeting -> {
                    List<HashtagResponse> hashtagResponseList = meeting.getHashtagMeetingList().stream()
                            .map(HashtagMeeting::getHashtag)
                            .map(HashtagResponse::new)
                            .collect(Collectors.toList());

                    return Mono.just(new MeetingResponse(meeting, hashtagResponseList));
                })
                .subscribeOn(Schedulers.boundedElastic());
    }

    public Mono<Void> enrollMeeting(String userEmail, Long meetingId) {
        log.info("Booking Server - '{}' request enrollMeeting", userEmail);

        // TODO 참가자랑 대기중인 사람은 등록 불가, 존재하지 않는 미팅
//        return Mono.justOrEmpty(meetingRepository.findById(meetingId))
//                        .zipWith(getMemberInfoByEmail(userEmail))
//                .flatMap(it -> participantService.existsParticipantByMeetingAndMemberId(it.getT1(), it.getT2().loginId())
//                        .doOnNext(exists -> log.info("existsParticipantByMeetingAndMemberId"))
//                        .flatMap(exists -> {
//                            if (exists) {
//                                return Mono.error(new RuntimeException("이미 가입한 모임입니다."));
//                            }
//                            return Mono.just(it);
//                        }))
//                .flatMap(it -> Mono.fromRunnable(() -> waitlistService.enrollMeeting(it.getT1(), it.getT2().loginId())))
//                .subscribeOn(Schedulers.boundedElastic())
//                .then();
        return Mono.empty();
    }
}
