package com.booking.booking.meeting.service;

import com.booking.booking.global.dto.request.InitChatroomRequest;
import com.booking.booking.global.dto.request.JoinChatroomRequest;
import com.booking.booking.global.dto.response.BookResponse;
import com.booking.booking.global.dto.response.MemberResponse;
import com.booking.booking.global.utils.BookUtil;
import com.booking.booking.global.utils.ChatroomUtil;
import com.booking.booking.global.utils.MemberUtil;
import com.booking.booking.hashtag.dto.response.HashtagResponse;
import com.booking.booking.hashtagmeeting.service.HashtagMeetingService;
import com.booking.booking.meeting.domain.Meeting;
import com.booking.booking.meeting.domain.MeetingState;
import com.booking.booking.meeting.dto.request.MeetingRequest;
import com.booking.booking.meeting.dto.response.MeetingDetailResponse;
import com.booking.booking.meeting.dto.response.MeetingListResponse;
import com.booking.booking.meeting.repository.MeetingRepository;
import com.booking.booking.meetinginfo.dto.response.MeetingInfoResponse;
import com.booking.booking.meetinginfo.service.MeetingInfoService;
import com.booking.booking.participant.dto.response.ParticipantResponse;
import com.booking.booking.participant.service.ParticipantService;
import com.booking.booking.waitlist.service.WaitlistService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class MeetingService {
    private final MeetingRepository meetingRepository;
    private final HashtagMeetingService hashtagMeetingService;
    private final MeetingInfoService meetingInfoService;
    private final ParticipantService participantService;
    private final WaitlistService waitlistService;

    private final static double RADIUS = 10.0;

    public Mono<Meeting> createMeeting(String userEmail, MeetingRequest meetingRequest) {
        log.info("Booking Server Meeting - createMeeting({}, {})", userEmail, meetingRequest);

        return Mono.zip(
                        MemberUtil.getMemberInfoByEmail(userEmail),
                        BookUtil.getBookByIsbn(meetingRequest.bookIsbn()))
                .flatMap(tuple -> {
                    MemberResponse member = tuple.getT1();
                    BookResponse book = tuple.getT2();

                    return handleCreateMeeting(tuple.getT1(), meetingRequest);
                })
                .onErrorResume(error -> {
                    log.error("Booking Server Meeting - Error during createMeeting : {}", error.getMessage());
                    return Mono.error(new RuntimeException("미팅 생성 실패"));
                });
    }

    private Mono<Meeting> handleCreateMeeting(MemberResponse memberResponse, MeetingRequest meetingRequest) {
        log.info("Booking Server Meeting - handleCreateMeeting({}, {})", memberResponse, meetingRequest);

        return meetingRepository.save(meetingRequest.toEntity(memberResponse, MeetingState.PREPARING))
                .flatMap(meeting -> Mono.defer(() -> {
                    // TODO 뭔가 이상한데
                    ChatroomUtil.initializeChatroom(new InitChatroomRequest(meeting)).subscribe();
                    participantService.addParticipant(meeting, memberResponse.memberPk()).subscribe();
                    hashtagMeetingService.saveHashtags(meeting.getMeetingId(), meetingRequest.hashtagList()).subscribe();
                    return Mono.just(meeting);
                }).thenReturn(meeting))
                .onErrorResume(error -> {
                    // TODO 롤백 어떻게 하지
                    log.error("Booking Server Meeting - Error during handleCreateMeeting : {}", error.getMessage());
                    return Mono.error(error);
                });
    }

    public Flux<MeetingListResponse> findAllByLocation(String userEmail) {
        log.info("Booking Server Meeting - findAllByLocation({})", userEmail);

        return MemberUtil.getMemberInfoByEmail(userEmail)
                .flatMapMany(member -> meetingRepository.findAllByRadius(member.lat(), member.lgt(), RADIUS))
                .flatMap(meeting -> {
                    Mono<BookResponse> bookResponseMono = BookUtil.getBookByIsbn(meeting.getBookIsbn());
                    Mono<Integer> curPartipantsMono = participantService.countAllByMeetingId(meeting.getMeetingId());
                    Mono<List<HashtagResponse>> hashtagResponseFlux =
                            hashtagMeetingService.findHashtagByMeetingId(meeting.getMeetingId())
                            .flatMap(hashtag -> Mono.just(new HashtagResponse(hashtag)))
                            .collectList();

                    return Mono.zip(bookResponseMono, curPartipantsMono, hashtagResponseFlux)
                            .map(tuple -> new MeetingListResponse(meeting, tuple.getT1(), tuple.getT2(), tuple.getT3()));
                })
                .onErrorResume(error -> {
                    log.error("Booking Server Meeting - Error during findAllByLocation : {}", error.getMessage());
                    return Mono.error(error);
                });
    }

    public Flux<MeetingListResponse> findAllByHashtagId(String userEmail, Long hashtagId) {
        log.info("Booking Server Meeting - findAllByHashtagId({})", userEmail);

        return MemberUtil.getMemberInfoByEmail(userEmail)
                .flatMapMany(member -> meetingRepository.findAllByHashtagId(member.lat(), member.lgt(), RADIUS, hashtagId))
                .flatMap(meeting -> {
                    Mono<BookResponse> bookResponseMono = BookUtil.getBookByIsbn(meeting.getBookIsbn());
                    Mono<Integer> curPartipantsMono = participantService.countAllByMeetingId(meeting.getMeetingId());
                    Mono<List<HashtagResponse>> hashtagResponseFlux =
                            hashtagMeetingService.findHashtagByMeetingId(meeting.getMeetingId())
                                    .flatMap(hashtag -> Mono.just(new HashtagResponse(hashtag)))
                                    .collectList();

                    return Mono.zip(bookResponseMono, curPartipantsMono, hashtagResponseFlux)
                            .map(tuple -> new MeetingListResponse(meeting, tuple.getT1(), tuple.getT2(), tuple.getT3()));
                })
                .onErrorResume(error -> {
                    log.error("Booking Server Meeting - Error during findAllByHashtagId : {}", error.getMessage());
                    return Mono.error(error);
                });
    }

    public Mono<MeetingDetailResponse> findByMeetingId(Long meetingId) {
        log.info("Booking Server Meeting - findByMeetingId({})", meetingId);

        return meetingRepository.findByMeetingId(meetingId)
                .switchIfEmpty(Mono.error(new RuntimeException("미팅 없음")))
                .flatMap(meeting -> {
                    Mono<BookResponse> bookResponseMono = BookUtil.getBookByIsbn(meeting.getBookIsbn());
                    Mono<List<ParticipantResponse>> participantResponseMono =
                            participantService.findAllByMeetingId(meetingId).collectList();
                    Mono<List<HashtagResponse>> hashtagResponseFlux =
                            hashtagMeetingService.findHashtagByMeetingId(meeting.getMeetingId())
                                    .flatMap(hashtag -> Mono.just(new HashtagResponse(hashtag)))
                                    .collectList();
                    Mono<List<MeetingInfoResponse>> MeetingInfoResponseMono =
                            meetingInfoService.findAllByMeetingId(meetingId)
                                    .collectList();


                    return Mono.zip(
                            bookResponseMono, participantResponseMono, hashtagResponseFlux, MeetingInfoResponseMono)
                            .map(tuple -> new MeetingDetailResponse
                                    (meeting, tuple.getT1(), tuple.getT2(), tuple.getT3(), tuple.getT4()));
                })
                .onErrorResume(error -> {
                    log.error("Booking Server Meeting - Error during findByMeetingId : {}", error.getMessage());
                    return Mono.error(error);
                });
    }

    public Mono<Void> enrollMeeting(String userEmail, Long meetingId) {
        log.info("Booking Server Meeting - enrollMeeting({}, {})", userEmail, meetingId);

        return Mono.zip(
                    meetingRepository.findById(meetingId)
                            .switchIfEmpty(Mono.error(new RuntimeException("존재하지 않는 모임"))),
                        MemberUtil.getMemberInfoByEmail(userEmail))
                .flatMap(it -> {
                    Meeting meeting = it.getT1();
                    Integer memberId = it.getT2().memberPk();

                    return Mono.zip(
                            participantService.existsByMeetingIdAndMemberId(meetingId, memberId),
                            waitlistService.existsByMeetingIdAndMemberId(meetingId, memberId))
                            .flatMap(it2 -> {
                                if (it2.getT1()) {
                                    return Mono.error(new RuntimeException("이미 등록한 회원"));
                                } else if (it2.getT2()) {
                                    return Mono.error(new RuntimeException("이미 대기 중인 회원"));
                                }
                                return waitlistService.enrollMeeting(meetingId, memberId);
                            });
                })
                .onErrorResume(error -> {
                    log.error("Booking Server Meeting - Error during enrollMeeting : {}", error.getMessage());
                    return Mono.error(error);
                });
    }

    @Transactional
    public Mono<Void> acceptMeeting(String userEmail, Long meetingId, Integer memberId) {
        log.info("Booking Server Meeting - acceptMeeting({}, {}, {})", userEmail, meetingId, memberId);

        // TODO 최대 인원 확인
        return Mono.zip(
                    meetingRepository.findById(meetingId)
                            .switchIfEmpty(Mono.error(new RuntimeException("존재하지 않는 모임"))),
                    MemberUtil.getMemberInfoByEmail(userEmail))
                .flatMap(it -> {
                    Meeting meeting = it.getT1();
                    Integer leaderId = it.getT2().memberPk();

                    if (!meeting.getLeaderId().equals(leaderId)) {
                        return Mono.error(new RuntimeException("모임 수락 권한 없음"));
                    }

                    return waitlistService.existsByMeetingIdAndMemberId(meetingId, memberId)
                            .flatMap(exist -> {
                                if (!exist) {
                                    return Mono.error(new RuntimeException("대기 목록에 없는 회원"));
                                }
                                return waitlistService.deleteByMeetingIdAndMemberId(meetingId, memberId)
                                        .then(participantService.addParticipant(meeting, memberId))
                                        .then(ChatroomUtil.joinChatroom(new JoinChatroomRequest(meetingId, memberId)));
                            })
                            .then();
                })
                .onErrorResume(error -> {
                    log.error("Booking Server Meeting - Error during acceptMeeting : {}", error.getMessage());
                    return Mono.error(error);
                });
    }
}
