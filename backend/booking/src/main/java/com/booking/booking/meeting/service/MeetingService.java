package com.booking.booking.meeting.service;

import com.booking.booking.global.dto.request.EnrollNotificationRequest;
import com.booking.booking.global.dto.request.InitChatroomRequest;
import com.booking.booking.global.dto.request.JoinChatroomRequest;
import com.booking.booking.global.dto.response.MemberResponse;
import com.booking.booking.global.utils.BookUtil;
import com.booking.booking.global.utils.ChatroomUtil;
import com.booking.booking.global.utils.MemberUtil;
import com.booking.booking.global.utils.NotificationUtil;
import com.booking.booking.hashtag.dto.response.HashtagResponse;
import com.booking.booking.hashtag.service.HashtagService;
import com.booking.booking.hashtagmeeting.service.HashtagMeetingService;
import com.booking.booking.meeting.domain.Meeting;
import com.booking.booking.meeting.domain.MeetingState;
import com.booking.booking.meeting.dto.request.MeetingRequest;
import com.booking.booking.meeting.dto.request.MeetingUpdateRequest;
import com.booking.booking.meeting.dto.response.MeetingDetailResponse;
import com.booking.booking.meeting.dto.response.MeetingListResponse;
import com.booking.booking.meeting.repository.MeetingRepository;
import com.booking.booking.meetinginfo.dto.request.MeetingInfoRequest;
import com.booking.booking.meetinginfo.service.MeetingInfoService;
import com.booking.booking.participant.service.ParticipantService;
import com.booking.booking.waitlist.service.WaitlistService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@Service
public class MeetingService {
    private final MeetingRepository meetingRepository;
    private final HashtagService hashtagService;
    private final HashtagMeetingService hashtagMeetingService;
    private final MeetingInfoService meetingInfoService;
    private final ParticipantService participantService;
    private final WaitlistService waitlistService;

    private final static double RADIUS = 10.0;

    @Transactional
    public Mono<Meeting> createMeeting(String userEmail, MeetingRequest meetingRequest) {
        log.info("[Booking:Meeting] createMeeting({}, {})", userEmail, meetingRequest);

        return Mono.zip(MemberUtil.getMemberInfoByEmail(userEmail),
                        BookUtil.getBookByIsbn(meetingRequest.bookIsbn()))
                .flatMap(tuple -> handleCreateMeeting(tuple.getT1(), meetingRequest))
                .onErrorResume(error -> {
                    log.error("[Booking:Meeting ERROR] createMeeting : {}", error.getMessage());
                    return Mono.error(new RuntimeException("미팅 생성 실패"));
                });
    }

    private Mono<Meeting> handleCreateMeeting(MemberResponse memberResponse, MeetingRequest meetingRequest) {
        log.info("[Booking:Meeting] handleCreateMeeting({}, {})", memberResponse, meetingRequest);

        return meetingRepository.save(meetingRequest.toEntity(memberResponse, MeetingState.PREPARING))
                .flatMap(meeting ->
                        Mono.defer(() -> ChatroomUtil.initializeChatroom(new InitChatroomRequest(meeting))
                                .then(participantService.addParticipant(meeting, memberResponse.memberPk()))
                                .then(hashtagMeetingService
                                        .saveHashtags(meeting.getMeetingId(),meetingRequest.hashtagList()))
                                .thenReturn(meeting)))
                .onErrorResume(error -> {
                    log.error("[Booking:Meeting ERROR] handleCreateMeeting : {}", error.getMessage());
                    return Mono.error(error);
                });
    }

    public Flux<MeetingListResponse> findAllByLocation(String userEmail) {
        log.info("[Booking:Meeting] findAllByLocation({})", userEmail);

        return MemberUtil.getMemberInfoByEmail(userEmail)
                .flatMapMany(member -> meetingRepository.findAllByRadius(member.lat(), member.lgt(), RADIUS))
                .flatMap(this::buildMeetingListResponse)
                .onErrorResume(error -> {
                    log.error("[Booking:Meeting ERROR] findAllByLocation : {}", error.getMessage());
                    return Mono.error(error);
                });
    }

    public Flux<MeetingListResponse> findAllByHashtagId(String userEmail, Long hashtagId) {
        log.info("[Booking:Meeting] - findAllByHashtagId({}, {})", userEmail, hashtagId);

        return MemberUtil.getMemberInfoByEmail(userEmail)
                .flatMapMany(member ->
                        meetingRepository.findAllByHashtagId(member.lat(), member.lgt(), RADIUS, hashtagId))
                .flatMap(this::buildMeetingListResponse)
                .onErrorResume(error -> {
                    log.error("[Booking:Meeting Error] findAllByHashtagId : {}", error.getMessage());
                    return Mono.error(error);
                });
    }

    public Flux<MeetingListResponse> findOngoingByNickname(String nickname) {
        log.info("[Booking:Meeting] - findOngoingByNickname({})", nickname);

        return MemberUtil.getMemberInfoByNickname(nickname)
                .flatMapMany(member -> meetingRepository.findOngoingByMemberId(member.memberPk()))
                .flatMap(this::buildMeetingListResponse)
                .onErrorResume(error -> {
                    log.error("[Booking:Meeting Error] findOngoingByNickname : {}", error.getMessage());
                    return Mono.error(error);
                });
    }

    public Flux<MeetingListResponse> findFinishByNickname(String nickname) {
        log.info("[Booking:Meeting] - findFinishByNickname({})", nickname);

        return MemberUtil.getMemberInfoByNickname(nickname)
                .flatMapMany(member -> meetingRepository.findFinishByMemberId(member.memberPk()))
                .flatMap(this::buildMeetingListResponse)
                .onErrorResume(error -> {
                    log.error("[Booking:Meeting Error] findFinishByNickname : {}", error.getMessage());
                    return Mono.error(error);
                });
    }

    private Mono<MeetingListResponse> buildMeetingListResponse(Meeting meeting) {
        log.info("[Booking:Meeting] buildMeetingListResponse({})", meeting);

        return Mono.zip(BookUtil.getBookByIsbn(meeting.getBookIsbn()),
                        participantService.countAllByMeetingId(meeting.getMeetingId()),
                        hashtagService.findHashtagsByMeetingId(meeting.getMeetingId())
                                .flatMap(hashtag -> Mono.just(new HashtagResponse(hashtag))).collectList())
                .map(tuple -> new MeetingListResponse(meeting, tuple.getT1(), tuple.getT2(), tuple.getT3()));
    }

    public Mono<MeetingDetailResponse> findByMeetingId(Long meetingId) {
        log.info("[Booking:Meeting] findByMeetingId({})", meetingId);

        return meetingRepository.findByMeetingId(meetingId)
                .switchIfEmpty(Mono.error(new RuntimeException("존재하지 않는 미팅")))
                .flatMap(this::buildMeetingDetailResponse)
                .onErrorResume(error -> {
                    log.error("[Booking:Meeting ERROR] findByMeetingId : {}", error.getMessage());
                    return Mono.error(error);
                });
    }

    private Mono<MeetingDetailResponse> buildMeetingDetailResponse(Meeting meeting) {
        log.info("[Booking:Meeting] buildMeetingDetailResponse({})", meeting);

        return Mono.zip(BookUtil.getBookByIsbn(meeting.getBookIsbn()),
                        participantService.findAllByMeetingId(meeting.getMeetingId()).collectList(),
                        hashtagService.findHashtagsByMeetingId(meeting.getMeetingId())
                                .flatMap(hashtag -> Mono.just(new HashtagResponse(hashtag))).collectList(),
                        meetingInfoService.findAllByMeetingId(meeting.getMeetingId()).collectList())
                .map(tuple ->
                        new MeetingDetailResponse(meeting, tuple.getT1(), tuple.getT2(), tuple.getT3(), tuple.getT4()));
    }

    @Transactional
    public Mono<Void> enrollMeeting(String userEmail, Long meetingId) {
        log.info("[Booking:Meeting] enrollMeeting({}, {})", userEmail, meetingId);

        return Mono.zip(meetingRepository.findById(meetingId)
                                .switchIfEmpty(Mono.error(new RuntimeException("존재하지 않는 모임"))),
                        MemberUtil.getMemberInfoByEmail(userEmail))
                .flatMap(tuple -> Mono.zip(
                                participantService.existsByMeetingIdAndMemberId(meetingId, tuple.getT2().memberPk()),
                                waitlistService.existsByMeetingIdAndMemberId(meetingId, tuple.getT2().memberPk()))
                        .flatMap(tuple2 -> {
                            if (tuple2.getT1()) {
                                return Mono.error(new RuntimeException("이미 등록한 회원"));
                            } else if (tuple2.getT2()) {
                                return Mono.error(new RuntimeException("이미 대기 중인 회원"));
                            }
                            return waitlistService.enrollMeeting(meetingId, tuple.getT2().memberPk())
                                    .then(NotificationUtil.enrollNotification(new EnrollNotificationRequest(meetingId, tuple.getT1().getMeetingTitle())));
                        }))
                .onErrorResume(error -> {
                    log.error("[Booking:Meeting ERROR] enrollMeeting : {}", error.getMessage());
                    return Mono.error(error);
                });
    }

    @Transactional
    public Mono<Void> acceptMeeting(String userEmail, Long meetingId, Integer memberId) {
        log.info("[Booking:Meeting] acceptMeeting({}, {}, {})", userEmail, meetingId, memberId);

        return Mono.zip(meetingRepository.findByMeetingId(meetingId)
                                .switchIfEmpty(Mono.error(new RuntimeException("존재하지 않는 모임"))),
                        MemberUtil.getMemberInfoByEmail(userEmail))
                .flatMap(tuple -> {
                    Meeting meeting = tuple.getT1();
                    Integer leaderId = tuple.getT2().memberPk();

                    if (!meeting.getLeaderId().equals(leaderId)) {
                        return Mono.error(new RuntimeException("모임 수락 권한 없음"));
                    }
                    return handleAcceptMeeting(meeting, memberId);
                })
                .onErrorResume(error -> {
                    log.error("[Booking:Meeting ERROR] acceptMeeting : {}", error.getMessage());
                    return Mono.error(error);
                });
    }

    private Mono<Void> handleAcceptMeeting(Meeting meeting, Integer memberId) {
        log.info("[Booking:Meeting] handleAcceptMeeting({}, {})", meeting, memberId);

        return waitlistService.existsByMeetingIdAndMemberId(meeting.getMeetingId(), memberId)
                .flatMap(exist -> {
                    if (!exist) {
                        return Mono.error(new RuntimeException("대기 목록에 없는 회원"));
                    }
                    return waitlistService.deleteByMeetingIdAndMemberId(meeting.getMeetingId(), memberId)
                            .then(participantService.addParticipant(meeting, memberId))
                            .then(ChatroomUtil.joinChatroom(new JoinChatroomRequest(meeting.getMeetingId(), memberId)));
                })
                .then();
    }

    @Transactional
    public Mono<Void> rejectMeeting(String userEmail, Long meetingId, Integer memberId) {
        log.info("[Booking:Meeting] rejectMeeting({}, {}, {})", userEmail, meetingId, memberId);

        return Mono.zip(meetingRepository.findByMeetingId(meetingId)
                                .switchIfEmpty(Mono.error(new RuntimeException("존재하지 않는 모임"))),
                        MemberUtil.getMemberInfoByEmail(userEmail))
                .flatMap(tuple -> {
                    Meeting meeting = tuple.getT1();
                    Integer leaderId = tuple.getT2().memberPk();

                    if (!meeting.getLeaderId().equals(leaderId)) {
                        return Mono.error(new RuntimeException("모임 거절 권한 없음"));
                    }
                    return handleRejectMeeting(meeting, memberId);
                })
                .onErrorResume(error -> {
                    log.error("[Booking:Meeting ERROR] acceptMeeting : {}", error.getMessage());
                    return Mono.error(error);
                });
    }

    private Mono<Void> handleRejectMeeting(Meeting meeting, Integer memberId) {
        log.info("[Booking:Meeting] handleRejectMeeting({}, {})", meeting, memberId);

        return waitlistService.existsByMeetingIdAndMemberId(meeting.getMeetingId(), memberId)
                .flatMap(exist -> {
                    if (!exist) {
                        return Mono.error(new RuntimeException("대기 목록에 없는 회원"));
                    }
                    return waitlistService.deleteByMeetingIdAndMemberId(meeting.getMeetingId(), memberId);
                });
    }

    public Mono<Void> exitMeeting(String userEmail, Long meetingId) {
        log.info("[Booking:Meeting] exitMeeting({}, {})", userEmail, meetingId);

        return Mono.zip(meetingRepository.findByMeetingId(meetingId)
                                .switchIfEmpty(Mono.error(new RuntimeException("존재하지 않는 모임"))),
                        MemberUtil.getMemberInfoByEmail(userEmail))
                .flatMap(tuple -> handleExitMeeting(tuple.getT1(), tuple.getT2().memberPk()))
                .onErrorResume(error -> {
                    log.error("[Booking:Meeting ERROR] acceptMeeting : {}", error.getMessage());
                    return Mono.error(error);
                });
    }

    private Mono<Void> handleExitMeeting(Meeting meeting, Integer memberId) {
        log.info("[Booking:Meeting] handleExitMeeting({}, {})", meeting, memberId);

        return waitlistService.existsByMeetingIdAndMemberId(meeting.getMeetingId(), memberId)
                .flatMap(exist -> {
                    if (exist) {
                        return waitlistService.deleteByMeetingIdAndMemberId(meeting.getMeetingId(), memberId);
                    } else if (meeting.getMeetingState().equals(MeetingState.ONGOING)) {
                        return Mono.error(new RuntimeException("모임 진행 중"));
                    } else if (meeting.getLeaderId().equals(memberId)) {
                        return Mono.error(new RuntimeException("방장은 나갈 수 없음"));
                    }
                    return participantService.deleteByMeetingIdAndMemberId(meeting.getMeetingId(), memberId);
                });
    }

    @Transactional
    public Mono<Void> createMeetingInfo(String userEmail, MeetingInfoRequest meetingInfoRequest) {
        log.info("[Booking:Meeting] createMeetingInfo({}, {})", userEmail, meetingInfoRequest);

        return Mono.zip(meetingRepository.findByMeetingId(meetingInfoRequest.meetingId())
                                .switchIfEmpty(Mono.error(new RuntimeException("존재하지 않는 모임"))),
                MemberUtil.getMemberInfoByEmail(userEmail))
                .flatMap(tuple -> {
                    Meeting meeting = tuple.getT1();
                    Integer leaderId = tuple.getT2().memberPk();

                    if (!meeting.getLeaderId().equals(leaderId)) {
                        return Mono.error(new RuntimeException("모임 정보 생성 권한 없음"));
                    } else if (meeting.getMeetingState().equals(MeetingState.ONGOING)) {
                        return Mono.error(new RuntimeException("진행 중인 모임 있음"));
                    }

                    return meetingInfoService.createMeetingInfo(meetingInfoRequest.toEntity())
                            .then(meetingRepository.save(meeting.updateState(MeetingState.ONGOING)))
                            .then();
                })
                .onErrorResume(error -> {
                    log.error("[Booking:Meeting ERROR] createMeetingInfo : {}", error.getMessage());
                    return Mono.error(error);
                });
    }

    @Transactional
    public Mono<Void> updateMeeting(String userEmail, MeetingUpdateRequest meetingUpdateRequest) {
        log.info("[Booking:Meeting] updateMeeting({}, {})", userEmail, meetingUpdateRequest);

        return Mono.zip(MemberUtil.getMemberInfoByEmail(userEmail),
                        meetingRepository.findByMeetingId(meetingUpdateRequest.meetingId())
                                .switchIfEmpty(Mono.error(new RuntimeException("존재하지 않는 미팅"))))
                .flatMap(tuple -> {
                    MemberResponse member = tuple.getT1();
                    Meeting meeting = tuple.getT2();

                    if (!member.memberPk().equals(meeting.getLeaderId())) {
                        return Mono.error(new RuntimeException("미팅 수정 권한 없음"));
                    } else if(meeting.getMeetingState().equals(MeetingState.ONGOING)) {
                        return Mono.error(new RuntimeException("진행 중에는 미팅 수정 불가"));
                    } else if(meeting.getMeetingState().equals(MeetingState.FINISH)) {
                        return Mono.error(new RuntimeException("종료 후에는 미팅 수정 불가"));
                    }
                    return handleUpdateMeeting(meeting, meetingUpdateRequest);
                })
                .onErrorResume(error -> {
                    log.error("[Booking:Meeting ERROR] updateMeeting : {}", error.getMessage());
                    return Mono.error(new RuntimeException("미팅 수정 실패"));
                });
    }

    private Mono<Void> handleUpdateMeeting(Meeting meeting, MeetingUpdateRequest meetingUpdateRequest) {
        log.info("[Booking:Meeting] handleUpdateMeeting({}, {})", meeting, meetingUpdateRequest);

        return meetingRepository.save(meeting.updateMeeting(meetingUpdateRequest))
                .then(hashtagMeetingService.updateHashtags(meeting.getMeetingId(), meetingUpdateRequest.hashtagList()))
                .onErrorResume(error -> {
                    log.error("[Booking:Meeting ERROR] handleCreateMeeting : {}", error.getMessage());
                    return Mono.error(error);
                })
                .then();
    }

    @Transactional
    public Mono<Void> deleteMeeting(String userEmail, Long meetingId) {
        log.info("[Booking:Meeting] deleteMeeting({}, {})", userEmail, meetingId);

        return Mono.zip(MemberUtil.getMemberInfoByEmail(userEmail),
                        meetingRepository.findByMeetingId(meetingId)
                                .switchIfEmpty(Mono.error(new RuntimeException("존재하지 않는 미팅"))))
                .flatMap(tuple -> {
                    MemberResponse member = tuple.getT1();
                    Meeting meeting = tuple.getT2();

                    if (!member.memberPk().equals(meeting.getLeaderId())) {
                        return Mono.error(new RuntimeException("미팅 삭제 권한 없음"));
                    } else if (!meeting.getMeetingState().equals(MeetingState.PREPARING)) {
                        return Mono.error(new RuntimeException("진행 된 미팅 삭제 불가"));
                    }
                    return handleDeleteMeeting(meetingId);
                })
                .onErrorResume(error -> {
                        log.error("[Booking:Meeting ERROR] deleteMeeting : {}", error.getMessage());
                        return Mono.error(new RuntimeException("미팅 삭제 실패"));
                    });
    }

    private Mono<Void> handleDeleteMeeting(Long meetingId) {
        log.info("[Booking:Meeting] handleDeleteMeeting({})", meetingId);

        // TODO post 삭제
        return participantService.deleteAllByMeetingId(meetingId)
                .then(waitlistService.deleteAllByMeetingId(meetingId))
                .then(hashtagMeetingService.deleteAllByMeetingId(meetingId))
                .then(meetingRepository.deleteByMeetingId(meetingId))
                .onErrorResume(error -> {
                    log.error("[Booking:Meeting ERROR] handleDeleteMeeting : {}", error.getMessage());
                    return Mono.error(new RuntimeException("미팅 삭제 실패"));
                })
                .then();
    }
}
