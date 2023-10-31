package com.booking.booking.meeting.service;

import com.booking.booking.chatroom.service.ChatroomService;
import com.booking.booking.global.exception.ErrorCode;
import com.booking.booking.meeting.domain.Meeting;
import com.booking.booking.meeting.domain.MeetingState;
import com.booking.booking.meeting.dto.request.MeetingRequest;
import com.booking.booking.meeting.dto.response.MemberInfoResponse;
import com.booking.booking.meeting.exception.MeetingException;
import com.booking.booking.meeting.repository.MeetingRepository;
import com.booking.booking.meetinghashtag.service.MeetingHashtagService;
import com.booking.booking.participant.service.ParticipantService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.net.URI;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class MeetingService {

    private final MeetingRepository meetingRepository;
    private final MeetingHashtagService meetingHashtagService;
    private final ChatroomService chatroomService;
    private final ParticipantService participantService;
    private static final String GATEWAY_URL = "http://localhost:8999";

    public Mono<Void> arrangeMeeting(String userEmail, MeetingRequest meetingRequest) {
        return getMemberInfoByEmail(userEmail)
            .flatMap(memberInfo -> {
                log.info("hello web-flux");
                Meeting meeting = buildMeeting(memberInfo, meetingRequest);
                log.info("meeting name : {}", meeting.getMeetingTitle());
                return Mono.fromRunnable(() -> handleBlockingTasks(meeting, meetingRequest.hashtagList()))
                           .subscribeOn(Schedulers.boundedElastic())
                           .then();
            })
            .doOnError(error -> {
                log.error("Error during arrangeMeeting : {}", error.toString());
                throw new MeetingException(ErrorCode.CREATED_MEETING_FAILURE);
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

    private Meeting buildMeeting(MemberInfoResponse memberInfo, MeetingRequest meetingRequest) {
        return Meeting.builder()
                .leaderId(memberInfo.loginId())
                .address(memberInfo.address())
                .bookIsbn(meetingRequest.bookIsbn())
                .meetingTitle(meetingRequest.meetingTitle())
                .description(meetingRequest.description())
                .maxParticipants(meetingRequest.maxParticipants())
                .meetingState(MeetingState.PREPARING)
                .build();
    }

    private void handleBlockingTasks(Meeting meeting, List<String> hashtagList) {
        meetingRepository.save(meeting);
        meetingHashtagService.saveHashtags(meeting, hashtagList);
        chatroomService.createChatroom(meeting).subscribe();
        participantService.addParticipant(meeting).subscribe();
    }
}
