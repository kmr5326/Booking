package com.booking.booking.meeting.service;

import com.booking.booking.global.dto.request.EnrollNotificationRequest;
import com.booking.booking.global.dto.request.InitChatroomRequest;
import com.booking.booking.global.dto.request.JoinChatroomRequest;
import com.booking.booking.global.dto.response.BookResponse;
import com.booking.booking.global.dto.response.MemberResponse;
import com.booking.booking.global.utils.BookUtil;
import com.booking.booking.global.utils.ChatroomUtil;
import com.booking.booking.global.utils.MemberUtil;
import com.booking.booking.global.utils.NotificationUtil;
import com.booking.booking.hashtag.service.HashtagService;
import com.booking.booking.hashtagmeeting.service.HashtagMeetingService;
import com.booking.booking.meeting.domain.Meeting;
import com.booking.booking.meeting.domain.MeetingState;
import com.booking.booking.meeting.dto.request.MeetingAttendRequest;
import com.booking.booking.meeting.dto.request.MeetingRequest;
import com.booking.booking.meeting.dto.request.MeetingUpdateRequest;
import com.booking.booking.meeting.dto.response.MeetingDetailResponse;
import com.booking.booking.meeting.dto.response.MeetingListResponse;
import com.booking.booking.meeting.repository.MeetingRepository;
import com.booking.booking.meetinginfo.dto.request.MeetingInfoRequest;
import com.booking.booking.meetinginfo.service.MeetingInfoService;
import com.booking.booking.participant.service.ParticipantService;
import com.booking.booking.participantstate.service.ParticipantStateService;
import com.booking.booking.post.domain.Post;
import com.booking.booking.post.dto.request.PostRequest;
import com.booking.booking.post.dto.request.PostUpdateRequest;
import com.booking.booking.post.dto.response.PostDetailResponse;
import com.booking.booking.post.dto.response.PostListResponse;
import com.booking.booking.post.service.PostService;
import com.booking.booking.waitlist.service.WaitlistService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@RequiredArgsConstructor
@Service
public class MeetingService {
    private final MeetingRepository meetingRepository;
    private final HashtagService hashtagService;
    private final HashtagMeetingService hashtagMeetingService;
    private final MeetingInfoService meetingInfoService;
    private final ParticipantService participantService;
    private final ParticipantStateService participantStateService;
    private final WaitlistService waitlistService;
    private final PostService postService;
    private final MemberUtil memberUtil;

    private final static double RADIUS = 10.0;

    @Transactional
    public Mono<Meeting> createMeeting(String userEmail, MeetingRequest meetingRequest) {
        log.info("[Booking:Meeting] createMeeting({}, {})", userEmail, meetingRequest);

        return Mono.zip(memberUtil.getMemberInfoByEmail(userEmail),
                        BookUtil.getBookByIsbn(meetingRequest.bookIsbn()))
                .flatMap(tuple -> handleCreateMeeting(tuple.getT1(), tuple.getT2(), meetingRequest))
                .onErrorResume(error -> {
                    log.error("[Booking:Meeting ERROR] createMeeting : {}", error.getMessage());
                    return Mono.error(new RuntimeException("미팅 생성 실패"));
                });
    }

    private Mono<Meeting> handleCreateMeeting(MemberResponse member, BookResponse book, MeetingRequest meetingRequest) {
        log.info("[Booking:Meeting] handleCreateMeeting({}, {}, {})", member, book, meetingRequest);

        return meetingRepository.save(meetingRequest.toEntity(member, MeetingState.PREPARING))
                .flatMap(meeting ->
                        ChatroomUtil.initializeChatroom(new InitChatroomRequest(meeting, book))
                                .then(participantService.addParticipant(meeting, member.memberPk()))
                                .then(hashtagMeetingService
                                        .saveHashtags(meeting.getMeetingId(), meetingRequest.hashtagList()))
                                .thenReturn(meeting));
    }

    // TODO 정렬 추가
    public Flux<MeetingListResponse> findAllByLocation(String userEmail) {
        log.info("[Booking:Meeting] findAllByLocation({})", userEmail);

        return memberUtil.getMemberInfoByEmail(userEmail)
                .flatMapMany(member -> meetingRepository.findAllByRadius(member.lat(), member.lgt(), RADIUS))
                .flatMap(this::buildMeetingListResponse)
                .onErrorResume(error -> {
                    log.error("[Booking:Meeting ERROR] findAllByLocation : {}", error.getMessage());
                    return Mono.error(error);
                });
    }

    public Flux<MeetingListResponse> findAllByHashtagId(String userEmail, Long hashtagId) {
        log.info("[Booking:Meeting] - findAllByHashtagId({}, {})", userEmail, hashtagId);

        return memberUtil.getMemberInfoByEmail(userEmail)
                .flatMapMany(member ->
                        meetingRepository.findAllByHashtagId(member.lat(), member.lgt(), RADIUS, hashtagId))
                .flatMap(this::buildMeetingListResponse)
                .onErrorResume(error -> {
                    log.error("[Booking:Meeting Error] findAllByHashtagId : {}", error.getMessage());
                    return Mono.error(error);
                });
    }

    public Flux<MeetingListResponse> findAllByTitle(String userEmail, String title) {
        log.info("[Booking:Meeting] - findAllByTitle({}, {})", userEmail, title);

        return memberUtil.getMemberInfoByEmail(userEmail)
                .flatMapMany(member ->
                        meetingRepository.findAllByMeetingTitle(member.lat(), member.lgt(), RADIUS, "%" + title + "%"))
                .flatMap(this::buildMeetingListResponse)
                .onErrorResume(error -> {
                    log.error("[Booking:Meeting Error] findAllByTitle : {}", error.getMessage());
                    return Mono.error(error);
                });
    }

    public Flux<MeetingListResponse> findAllByMemberId(Integer memberId) {
        log.info("[Booking:Meeting] - findAllByMemberId({})", memberId);

        return memberUtil.getMemberInfoByPk(memberId)
                .flatMapMany(member -> meetingRepository.findAllByMemberId(member.memberPk()))
                .flatMap(this::buildMeetingListResponse)
                .onErrorResume(error -> {
                    log.error("[Booking:Meeting Error] findAllByMemberId : {}", error.getMessage());
                    return Mono.error(error);
                });
    }

    private Mono<MeetingListResponse> buildMeetingListResponse(Meeting meeting) {
        log.info("[Booking:Meeting] buildMeetingListResponse({})", meeting);

        return Mono.zip(BookUtil.getBookByIsbn(meeting.getBookIsbn()),
                        participantService.countAllByMeetingId(meeting.getMeetingId()),
                        hashtagService.findHashtagsByMeetingId(meeting.getMeetingId()).collectList())
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
                        participantService.countAllByMeetingId(meeting.getMeetingId()),
                        hashtagService.findHashtagsByMeetingId(meeting.getMeetingId()).collectList(),
                        meetingInfoService.findAllByMeetingId(meeting.getMeetingId()).collectList())
                .map(tuple ->
                        new MeetingDetailResponse(meeting, tuple.getT1(), tuple.getT2(), tuple.getT3(), tuple.getT4()));
    }

    @Transactional
    public Mono<Void> enrollMeeting(String userEmail, Long meetingId) {
        log.info("[Booking:Meeting] enrollMeeting({}, {})", userEmail, meetingId);

        return Mono.zip(meetingRepository.findById(meetingId)
                                .switchIfEmpty(Mono.error(new RuntimeException("존재하지 않는 모임"))),
                        memberUtil.getMemberInfoByEmail(userEmail))
                .flatMap(tuple -> handleEnrollMeeting(tuple.getT1(), tuple.getT2()))
                .onErrorResume(error -> {
                    log.error("[Booking:Meeting ERROR] enrollMeeting : {}", error.getMessage());
                    return Mono.error(error);
                });
    }

    private Mono<Void> handleEnrollMeeting(Meeting meeting, MemberResponse member) {
        log.info("[Booking:Meeting] handleEnrollMeeting({}, {})", meeting, member);

        return Mono.zip(participantService.existsByMeetingIdAndMemberId(meeting.getMeetingId(), member.memberPk()),
                        waitlistService.existsByMeetingIdAndMemberId(meeting.getMeetingId(), member.memberPk()))
                .flatMap(tuple2 -> {
                    if (tuple2.getT1()) {
                        return Mono.error(new RuntimeException("이미 등록한 회원"));
                    } else if (tuple2.getT2()) {
                        return Mono.error(new RuntimeException("이미 대기 중인 회원"));
                    }
                    return waitlistService.enrollMeeting(meeting.getMeetingId(), member.memberPk())
                            .then(NotificationUtil.enrollNotification(
                                    new EnrollNotificationRequest(meeting.getLeaderId(), meeting.getMeetingTitle())));
                });
    }

    @Transactional
    public Mono<Void> acceptMeeting(String userEmail, Long meetingId, Integer memberId) {
        log.info("[Booking:Meeting] acceptMeeting({}, {}, {})", userEmail, meetingId, memberId);

        return Mono.zip(meetingRepository.findByMeetingId(meetingId)
                                .switchIfEmpty(Mono.error(new RuntimeException("존재하지 않는 모임"))),
                        memberUtil.getMemberInfoByEmail(userEmail))
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
                });
    }

    @Transactional
    public Mono<Void> rejectMeeting(String userEmail, Long meetingId, Integer memberId) {
        log.info("[Booking:Meeting] rejectMeeting({}, {}, {})", userEmail, meetingId, memberId);

        return Mono.zip(meetingRepository.findByMeetingId(meetingId)
                                .switchIfEmpty(Mono.error(new RuntimeException("존재하지 않는 모임"))),
                        memberUtil.getMemberInfoByEmail(userEmail))
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
                        memberUtil.getMemberInfoByEmail(userEmail))
                .flatMap(tuple -> {
                    Meeting meeting = tuple.getT1();
                    Integer leaderId = tuple.getT2().memberPk();

                    if (meeting.getMeetingState().equals(MeetingState.ONGOING)) {
                        return Mono.error(new RuntimeException("모임 진행 중"));
                    } else if (meeting.getLeaderId().equals(leaderId)) {
                        return Mono.error(new RuntimeException("방장은 나갈 수 없음"));
                    }
                    return handleExitMeeting(meeting, leaderId);
                })
                .onErrorResume(error -> {
                    log.error("[Booking:Meeting ERROR] acceptMeeting : {}", error.getMessage());
                    return Mono.error(error);
                });
    }

    private Mono<Void> handleExitMeeting(Meeting meeting, Integer memberId) {
        log.info("[Booking:Meeting] handleExitMeeting({}, {})", meeting, memberId);

        return Mono.zip(waitlistService.existsByMeetingIdAndMemberId(meeting.getMeetingId(), memberId),
                        participantService.existsByMeetingIdAndMemberId(meeting.getMeetingId(), memberId))
                .flatMap(tuple -> {
                    if (tuple.getT1()) {
                        return waitlistService.deleteByMeetingIdAndMemberId(meeting.getMeetingId(), memberId);
                    } else if(tuple.getT2()){
                        return participantService.deleteByMeetingIdAndMemberId(meeting.getMeetingId(), memberId);
                    }
                    return Mono.error(new RuntimeException("참가 목록에 없는 회원"));
                });
    }

    @Transactional
    public Mono<Void> updateMeeting(String userEmail, MeetingUpdateRequest meetingUpdateRequest) {
        log.info("[Booking:Meeting] updateMeeting({}, {})", userEmail, meetingUpdateRequest);

        return Mono.zip(memberUtil.getMemberInfoByEmail(userEmail),
                        meetingRepository.findByMeetingId(meetingUpdateRequest.meetingId())
                                .switchIfEmpty(Mono.error(new RuntimeException("존재하지 않는 미팅"))),
                        participantService.countAllByMeetingId(meetingUpdateRequest.meetingId()))
                .flatMap(tuple -> {
                    Integer memberId = tuple.getT1().memberPk();
                    Meeting meeting = tuple.getT2();
                    Integer curParticipants = tuple.getT3();

                    if (!memberId.equals(meeting.getLeaderId())) {
                        return Mono.error(new RuntimeException("미팅 수정 권한 없음"));
                    } else if (meeting.getMeetingState().equals(MeetingState.ONGOING)) {
                        return Mono.error(new RuntimeException("진행 중에는 미팅 수정 불가"));
                    } else if (meeting.getMeetingState().equals(MeetingState.FINISH)) {
                        return Mono.error(new RuntimeException("종료 후에는 미팅 수정 불가"));
                    } else if (curParticipants > meeting.getMaxParticipants()) {
                        return Mono.error(new RuntimeException("현재 인원 수보다 작게 수정 불가"));
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
                .then(hashtagMeetingService.updateHashtags(meeting.getMeetingId(), meetingUpdateRequest.hashtagList()));
    }

    @Transactional
    public Mono<Void> deleteMeeting(String userEmail, Long meetingId) {
        log.info("[Booking:Meeting] deleteMeeting({}, {})", userEmail, meetingId);

        return Mono.zip(memberUtil.getMemberInfoByEmail(userEmail),
                        meetingRepository.findByMeetingId(meetingId)
                                .switchIfEmpty(Mono.error(new RuntimeException("존재하지 않는 미팅"))))
                .flatMap(tuple -> {
                    MemberResponse member = tuple.getT1();
                    Meeting meeting = tuple.getT2();

                    if (!member.memberPk().equals(meeting.getLeaderId())) {
                        return Mono.error(new RuntimeException("미팅 삭제 권한 없음"));
                    } else if (!meeting.getMeetingState().equals(MeetingState.PREPARING)) {
                        return Mono.error(new RuntimeException("시작한 미팅 삭제 불가"));
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

        return participantService.deleteAllByMeetingId(meetingId)
                .then(waitlistService.deleteAllByMeetingId(meetingId))
                .then(hashtagMeetingService.deleteAllByMeetingId(meetingId))
                .then(postService.deleteAllByMeetingId(meetingId))
                .then(meetingRepository.deleteByMeetingId(meetingId));
    }

    @Transactional
    public Mono<Void> createMeetingInfo(String userEmail, MeetingInfoRequest meetingInfoRequest) {
        log.info("[Booking:Meeting] createMeetingInfo({}, {})", userEmail, meetingInfoRequest);

        return Mono.zip(meetingRepository.findByMeetingId(meetingInfoRequest.meetingId())
                                .switchIfEmpty(Mono.error(new RuntimeException("존재하지 않는 모임"))),
                        memberUtil.getMemberInfoByEmail(userEmail))
                .flatMap(tuple -> {
                    Meeting meeting = tuple.getT1();
                    Integer leaderId = tuple.getT2().memberPk();

                    if (!meeting.getLeaderId().equals(leaderId)) {
                        return Mono.error(new RuntimeException("모임 정보 생성 권한 없음"));
                    } else if (meeting.getMeetingState().equals(MeetingState.ONGOING)) {
                        return Mono.error(new RuntimeException("진행 중인 모임 있음"));
                    } else if (meeting.getMeetingState().equals(MeetingState.FINISH)) {
                        return Mono.error(new RuntimeException("종료된 모임"));
                    }
                    return meetingInfoService.createMeetingInfo(meetingInfoRequest.toEntity())
                            .flatMap(participantStateService::startMeeting)
                            .then(meetingRepository.save(meeting.updateState(MeetingState.ONGOING)))
                            .then();
                })
                .onErrorResume(error -> {
                    log.error("[Booking:Meeting ERROR] createMeetingInfo : {}", error.getMessage());
                    return Mono.error(error);
                });
    }

    @Transactional
    public Mono<Void> finishMeeting(String userEmail, Long meetingId, Boolean isFinish) {
        log.info("[Booking:Meeting] finishMeeting({}, {})", userEmail, meetingId);

        return Mono.zip(meetingRepository.findByMeetingId(meetingId)
                                .switchIfEmpty(Mono.error(new RuntimeException("존재하지 않는 모임"))),
                        memberUtil.getMemberInfoByEmail(userEmail))
                .flatMap(tuple -> {
                    Meeting meeting = tuple.getT1();
                    MemberResponse member = tuple.getT2();

                    if (!meeting.getLeaderId().equals(member.memberPk())) {
                        return Mono.error(new RuntimeException("모임 종료 권한 없음"));
                    } else if (!meeting.getMeetingState().equals(MeetingState.ONGOING)) {
                        return Mono.error(new RuntimeException("진행 중인 모임 아님"));
                    }
                    return handleFinishMeeting(meeting, isFinish);
                })
                .onErrorResume(error -> {
                    log.error("[Booking:Meeting ERROR] finishMeeting : {}", error.getMessage());
                    return Mono.error(error);
                })
                .then();
    }

    private Mono<Void> handleFinishMeeting(Meeting meeting, Boolean isFinish) {
        return meetingInfoService.findByMeetingId(meeting.getMeetingId())
                .flatMap(meetingInfo -> {
                    if (meetingInfo.getDate().isAfter(LocalDateTime.now())) {
                        return Mono.error(new RuntimeException("모임 전에는 종료할 수 없음"));
                    }
                    return participantStateService.findParticipantStatesByMeetingId(meeting.getMeetingId())
                            .collectList()
                            .flatMap(participantStates -> {
                                AtomicInteger paymentCount = new AtomicInteger();
                                AtomicInteger attendanceCount = new AtomicInteger();
                                participantStates.forEach(participantState -> {
                                    if (participantState.getPaymentStatus()) {
                                        paymentCount.getAndIncrement();
                                    }
                                    if (participantState.getAttendanceStatus()) {
                                        attendanceCount.getAndIncrement();
                                    }
                                });
                                int totalFee = meetingInfo.getFee() * paymentCount.get();
                                if (attendanceCount.get() == 0) {
                                    return Mono.empty();
                                }
                                return Flux.fromIterable(participantStates)
                                        .flatMap(participantState ->
                                        {
                                            if (participantState.getAttendanceStatus()){
                                                return  memberUtil.paybackRequest(participantState.getMemberId(),
                                                        totalFee / attendanceCount.get());
                                            }
                                            return Mono.empty();
                                        })
                                        .then();
                            })
                            .then(meetingRepository.save(meeting
                                    .updateState(isFinish ? MeetingState.FINISH : MeetingState.PREPARING)));
                })
                .then(BookUtil.increaseMeetingCount(meeting.getBookIsbn()));
    }

    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final double EARTH_RADIUS = 6371000;

        double lat1Rad = Math.toRadians(lat1);
        double lon1Rad = Math.toRadians(lon1);
        double lat2Rad = Math.toRadians(lat2);
        double lon2Rad = Math.toRadians(lon2);

        double c = Math.acos(Math.cos(lat1Rad) * Math.cos(lat2Rad) * Math.cos(lon2Rad-lon1Rad)
                + Math.sin(lat1Rad) * Math.sin(lat2Rad)) ;

        // 거리 (미터 단위)
        return EARTH_RADIUS * c;
    }

    public Mono<Void> attendMeeting(String userEmail, MeetingAttendRequest meetingAttendRequest) {
        log.info("[Booking:Meeting] attendMeeting({}, {})", userEmail, meetingAttendRequest);

        return Mono.zip(meetingRepository.findByMeetingId(meetingAttendRequest.meetingId())
                                .switchIfEmpty(Mono.error(new RuntimeException("존재하지 않는 모임"))),
                        memberUtil.getMemberInfoByEmail(userEmail))
                .flatMap(tuple -> {
                    Meeting meeting = tuple.getT1();
                    MemberResponse member = tuple.getT2();

                    if (!meeting.getMeetingState().equals(MeetingState.ONGOING)) {
                        return Mono.error(new RuntimeException("진행 중인 모임 아님"));
                    }
                    return handleAttendMeeting(member.memberPk(), meetingAttendRequest);
                })
                .onErrorResume(error -> {
                    log.error("[Booking:Meeting ERROR] attendMeeting : {}", error.getMessage());
                    return Mono.error(error);
                });
    }

    private Mono<Void> handleAttendMeeting(Integer memberId, MeetingAttendRequest meetingAttendRequest) {
        return participantService.existsByMeetingIdAndMemberId(meetingAttendRequest.meetingId(), memberId)
                .flatMap(exists -> {
                    if (!exists) {
                        return Mono.error(new RuntimeException("모임 참가자가 아님"));
                    }

                    LocalDateTime now = LocalDateTime.now();
                    return meetingInfoService.findByMeetingId(meetingAttendRequest.meetingId())
                            .flatMap(meetingInfo -> {
                                LocalDateTime meetingTime = meetingInfo.getDate();
                                if (calculateDistance(meetingInfo.getLat(), meetingInfo.getLgt(),
                                        meetingAttendRequest.lat(), meetingAttendRequest.lgt()) > 100) {
                                    return Mono.error(new RuntimeException("거리가 너무 멀다"));
                                } else if (now.isBefore(meetingTime.minusMinutes(10))
                                        || now.isAfter(meetingTime.plusMinutes(10))) {
                                    return Mono.error(new RuntimeException("출석 시간 아님"));
                                } else {
                                    return participantStateService.attendMeeting(meetingInfo);
                                }
                            })
                            .then();
                });
    }

    public Mono<Void> payMeeting(String token, String userEmail, Long meetingId) {
        log.info("[Booking:Meeting] payMeeting({}, {})", userEmail, meetingId);

        return Mono.zip(meetingRepository.findByMeetingId(meetingId)
                                .switchIfEmpty(Mono.error(new RuntimeException("존재하지 않는 모임"))),
                        memberUtil.getMemberInfoByEmail(userEmail))
                .flatMap(tuple -> {
                    Meeting meeting = tuple.getT1();
                    MemberResponse member = tuple.getT2();

                    if (!meeting.getMeetingState().equals(MeetingState.ONGOING)) {
                        return Mono.error(new RuntimeException("진행 중인 모임 아님"));
                    }
                    return participantService.existsByMeetingIdAndMemberId(meetingId, member.memberPk())
                            .flatMap(exists -> {
                                if (!exists) {
                                    return Mono.error(new RuntimeException("모임 참가자가 아님"));
                                }

                                return meetingInfoService.findByMeetingId(meetingId)
                                        .flatMap(meetingInfo ->
                                                {
                                                    if (meetingInfo.getDate().isBefore(LocalDateTime.now())) {
                                                        return Mono.error(new RuntimeException("이미 시작한 모임"));
                                                    }
                                                    return participantStateService.findByMeetingIdAndMemberId(meetingInfo.getMeetinginfoId(), member.memberPk())
                                                            .switchIfEmpty(Mono.error(new RuntimeException("참여 중 아님")))
                                                            .flatMap(participantState -> {
                                                                if (participantState.getPaymentStatus()) {
                                                                    return Mono.error(new RuntimeException("참가비 지불 완료"));
                                                                }
                                                                return memberUtil.payRequest(token, meetingInfo.getFee())
                                                                        .then(participantStateService.payMeeting(meetingInfo));
                                                            });
                                                }
                                        );
                            })
                            .onErrorResume(error -> {
                                log.error("[Booking:Meeting ERROR] payMeeting : {}", error.getMessage());
                                return Mono.error(error);
                            });
                });
    }

    public Mono<Post> createPost(String userEmail, PostRequest postRequest) {
        log.info("[Booking:Meeting] createPost({}, {})", userEmail, postRequest);

        return Mono.zip(memberUtil.getMemberInfoByEmail(userEmail),
                meetingRepository.findByMeetingId(postRequest.meetingId())
                        .switchIfEmpty(Mono.error(new RuntimeException("존재하지 않는 미팅"))))
                .flatMap(tuple -> {
                    Integer memberId = tuple.getT1().memberPk();

                    return participantService.existsByMeetingIdAndMemberId(postRequest.meetingId(), memberId)
                            .flatMap(exists -> {
                                if (!exists) {
                                    return Mono.error(new RuntimeException("미팅 참여자만 글 작성 가능"));
                                }
                                return postService.createPost(postRequest.toEntity(memberId));
                            });
                })
                .onErrorResume(error -> {
                    log.error("[Booking:Meeting ERROR] createPost : {}", error.getMessage());
                    return Mono.error(error);
                });
    }

    public Flux<PostListResponse> findPostsByMeetingId(Long meetingId) {
        log.info("[Booking:Meeting] findPostsByMeetingId({})", meetingId);

        return meetingRepository.findByMeetingId(meetingId)
                .switchIfEmpty(Mono.error(new RuntimeException("존재하지 않는 미팅")))
                .thenMany(postService.findAllByMeetingId(meetingId))
                .onErrorResume(error -> {
                    log.error("[Booking:Meeting ERROR] findPostsByMeetingId : {}", error.getMessage());
                    return Mono.error(error);
                });
    }

    public Mono<PostDetailResponse> findByPostId(Long postId) {
        log.info("[Booking:Meeting] findByPostId({})", postId);

        return postService.findByPostId(postId)
                .switchIfEmpty(Mono.error(new RuntimeException("게시글 없음")))
                .flatMap(post -> memberUtil.getMemberInfoByPk(post.getMemberId())
                        .flatMap(member -> Mono.just(new PostDetailResponse(post, member))))
                .onErrorResume(error -> {
                    log.error("[Booking:Meeting ERROR] findByPostId : {}", error.getMessage());
                    return Mono.error(error);
                });
    }

    public Mono<PostDetailResponse> updatePost(String userEmail, PostUpdateRequest postUpdateRequest) {
        log.info("[Booking:Meeting] updatePost({}, {})", userEmail, postUpdateRequest);

        return Mono.zip(memberUtil.getMemberInfoByEmail(userEmail),
                postService.findByPostId(postUpdateRequest.postId())
                        .switchIfEmpty(Mono.error(new RuntimeException("존재하지 않는 게시글"))))
                .flatMap(tuple -> {
                    MemberResponse member = tuple.getT1();
                    Post post = tuple.getT2();

                    if (!post.getMemberId().equals(member.memberPk())) {
                        return Mono.error(new RuntimeException("게시글 수정 권한 없음"));
                    }
                    return postService.updatePost(post.update(postUpdateRequest))
                            .flatMap(updatedPost -> Mono.just(new PostDetailResponse(updatedPost, member)));
                })
                .onErrorResume(error -> {
                    log.error("[Booking:Meeting ERROR] updatePost : {}", error.getMessage());
                    return Mono.error(error);
                });
    }

    public Mono<Void> deletePost(String userEmail, Long postId) {
        log.info("[Booking:Meeting] deletePost({}, {})", userEmail, postId);

        return Mono.zip(memberUtil.getMemberInfoByEmail(userEmail),
                postService.findByPostId(postId)
                        .switchIfEmpty(Mono.error(new RuntimeException("존재하지 않는 게시글"))))
                .flatMap(tuple -> {
                    MemberResponse member = tuple.getT1();
                    Post post = tuple.getT2();

                    if (!post.getMemberId().equals(member.memberPk())) {
                        return Mono.error(new RuntimeException("게시글 수정 권한 없음"));
                    }
                    return postService.deleteByPostId(postId);
                })
                .onErrorResume(error -> {
                    log.error("[Booking:Meeting ERROR] deletePost : {}", error.getMessage());
                    return Mono.error(error);
                });
    }
}
