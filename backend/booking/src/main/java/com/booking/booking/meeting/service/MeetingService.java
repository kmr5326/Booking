package com.booking.booking.meeting.service;

import com.booking.booking.global.exception.ErrorCode;
import com.booking.booking.hashtag.dto.response.HashtagResponse;
import com.booking.booking.hashtag.service.HashtagService;
import com.booking.booking.hashtagmeeting.domain.HashtagMeeting;
import com.booking.booking.hashtagmeeting.service.HashtagMeetingService;
import com.booking.booking.meeting.domain.Meeting;
import com.booking.booking.meeting.domain.MeetingState;
import com.booking.booking.meeting.dto.request.ChatroomRequest;
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
    private final ParticipantService participantService;
    private final WaitlistService waitlistService;

    private static final String GATEWAY_URL = "http://localhost:8999";

    public Mono<Long> createMeeting(String userEmail, MeetingRequest meetingRequest) {
        log.info("Booking Server Meeting - createMeeting({}, {})", userEmail, meetingRequest);

        // TODO isbn 존재 여부 확인
        return getMemberInfoByEmail(userEmail)
//                .flatMap(memberInfo -> {
//                    log.info("getMemberInfoByEmail {}", memberInfo);
//                    return Mono.just(memberInfo);
//                })
                .flatMap(memberInfo -> {
                    Meeting meeting = meetingRequest.toEntity(memberInfo, MeetingState.PREPARING);
                    return Mono.fromSupplier(() -> handleCreateMeeting(meeting, meetingRequest.hashtagList()))
                            .subscribeOn(Schedulers.boundedElastic());
                })
                .onErrorResume(error -> {
                    log.error("Booking Server Meeting - Error during createMeeting : {}", error.getMessage());
                    return Mono.error(new MeetingException(ErrorCode.CREATE_MEETING_FAILURE));
                });
    }

    private Mono<MemberInfoResponse> getMemberInfoByEmail(String userEmail) {
        log.info("Booking Server Meeting - getMemberInfoByEmail({})", userEmail);

        WebClient webClient = WebClient.builder().build();
        URI uri = URI.create(GATEWAY_URL + "/api/members/memberInfo/" + userEmail);

        return webClient.get()
                .uri(uri)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError,
                        response -> Mono.error(new MeetingException(ErrorCode.GET_MEMBERINFO_FAILURE)))
                .onStatus(HttpStatus::is5xxServerError,
                        response -> Mono.error(new MeetingException(ErrorCode.GET_MEMBERINFO_FAILURE)))
                .bodyToMono(MemberInfoResponse.class);
    }

    private Mono<Void> createChatroom(ChatroomRequest chatroomRequest) {
        log.info("Booking Server Meeting - createChatroom()");
        log.info("start inter-server comm");

        WebClient webClient = WebClient.builder().build();
        URI uri = URI.create(GATEWAY_URL + "/api/chat/room/");

        return webClient.post()
                .uri(uri)
                .bodyValue(chatroomRequest)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, response -> Mono.error(RuntimeException::new))
                .onStatus(HttpStatus::is5xxServerError, response -> Mono.error(RuntimeException::new))
                .bodyToMono(Void.class);
    }

    private Long handleCreateMeeting(Meeting meeting, List<String> hashtagList) {
        log.info("Booking Server Meeting - handleArrangeMeeting({}, {})", meeting.getMeetingTitle(), hashtagList);

        meetingRepository.save(meeting);
        // TODO 채팅방 생성, 채팅방 인원 추가
//        createChatroom(new ChatroomRequest(meeting)).subscribe();
        hashtagMeetingService.saveHashtags(meeting, hashtagList).subscribe();
        participantService.addParticipant(meeting).subscribe();

        return meeting.getMeetingId();
    }

    public Mono<MeetingResponse> findById(Long meetingId) {
        log.info("Booking Server Meeting - findById({})", meetingId);

        return Mono.justOrEmpty(meetingRepository.findById(meetingId))
                .switchIfEmpty(Mono.error(new MeetingException(ErrorCode.GET_MEETING_FAILURE)))
                .flatMap(meeting -> {
                    List<HashtagResponse> hashtagResponseList = meeting.getHashtagMeetingList().stream()
                            .map(HashtagMeeting::getHashtag)
                            .map(HashtagResponse::new)
                            .collect(Collectors.toList());

                    return Mono.just(new MeetingResponse(meeting, hashtagResponseList));
                })
                .onErrorResume(error -> {
                    log.error("Booking Server Meeting - Error during findById : {}", error.getMessage());
                    return Mono.error(error);
                });
    }

    public Flux<MeetingResponse> findAll(String userEmail) {
        log.info("Booking Server Meeting - findAll({})", userEmail);

        // TODO 사용자 위치 기반
        return Flux.fromIterable(meetingRepository.findAll())
                .flatMap(meeting -> {
                    List<HashtagResponse> hashtagResponseList = meeting.getHashtagMeetingList().stream()
                            .map(HashtagMeeting::getHashtag)
                            .map(HashtagResponse::new)
                            .collect(Collectors.toList());

                    return Mono.just(new MeetingResponse(meeting, hashtagResponseList));
                })
                .onErrorResume(error -> {
                    log.error("Booking Server Meeting - Error during findAll : {}", error.getMessage());
                    return Mono.error(error);
                });
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
