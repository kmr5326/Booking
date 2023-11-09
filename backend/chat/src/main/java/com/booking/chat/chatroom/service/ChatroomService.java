package com.booking.chat.chatroom.service;

import com.booking.chat.chat.domain.Message;
import com.booking.chat.chatroom.domain.Chatroom;
import com.booking.chat.chatroom.dto.request.ExitChatroomRequest;
import com.booking.chat.chatroom.dto.request.InitChatroomRequest;
import com.booking.chat.chatroom.dto.request.JoinChatroomRequest;
import com.booking.chat.chatroom.dto.request.LastMessageRequest;
import com.booking.chat.chatroom.dto.response.ChatroomListResponse;
import com.booking.chat.chatroom.exception.ChatroomException;
import com.booking.chat.chatroom.repository.ChatroomRepository;
import com.booking.chat.global.exception.ErrorCode;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChatroomService {

    private final ChatroomRepository chatroomRepository;
    private final ReactiveRedisTemplate<String, List<Long>> reactiveRedisTemplate;

    public Mono<Chatroom> initializeChatroom(InitChatroomRequest initChatroomRequest) {
        return chatroomRepository.findById(initChatroomRequest.meetingId())
                                 .flatMap(existingChatroom -> Mono.<Chatroom>error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Chatroom with given meetingId already exists")))
                                 .switchIfEmpty(Mono.defer(() -> {
                                     Chatroom chatroom = Chatroom.createWithLeader(initChatroomRequest);
                                     return chatroomRepository.save(chatroom);
                                 }));
    }

    public Mono<Long> joinChatroom(JoinChatroomRequest joinChatroomRequest) {
        return chatroomRepository.findById(joinChatroomRequest.meetingId())
                                 .flatMap(chatroom -> {
                                     List<Long> members = chatroom.getMemberList();
                                     if (members.contains(joinChatroomRequest.memberId())) {
                                         return Mono.empty(); // 에러 대신 Mono<Void> 반환
                                     }
                                     members.add(joinChatroomRequest.memberId());
                                     return chatroomRepository.save(chatroom).thenReturn(joinChatroomRequest.memberId());
                                 });
    }

    public Mono<Long> exitChatroom(ExitChatroomRequest exitChatroomRequest) {
        return chatroomRepository.findById(exitChatroomRequest.meetingId())
                                 .flatMap(chatroom -> {
                                     boolean removed = chatroom.getMemberList().remove(exitChatroomRequest.memberId());
                                     if (!removed) {
                                         return Mono.empty(); // 대신 Mono<Void>를 반환
                                     }
                                     return chatroomRepository.save(chatroom).thenReturn(exitChatroomRequest.memberId());
                                 });
    }
    public Flux<ChatroomListResponse> getChatroomListByMemberId(Long memberId) {
        return chatroomRepository.findByMemberListContains(memberId)
                                 .map(ChatroomListResponse::from);
    }

    public Flux<ChatroomListResponse> getChatroomListByMemberIdOrderByDesc(Long memberId) {
        return chatroomRepository.findByMemberListContainsOrderByLastMessageReceivedTimeDesc(memberId)
                                 .map(ChatroomListResponse::from);
    }


    public Mono<Chatroom> findByChatroomId(Long chatroomId) {
        return chatroomRepository.findById(chatroomId).switchIfEmpty(Mono.error(new ChatroomException(ErrorCode.CHATROOM_NOT_FOUND)));
    }

    public Mono<Chatroom> save(Chatroom chatroom) {
        return chatroomRepository.save(chatroom);
    }

    public Mono<String> getChatroomMeetingTitle(Long chatroomId) {
        return chatroomRepository.findById(chatroomId)
                                 .map(Chatroom::getMeetingTitle);
    }

    public Flux<Message> enterChatroom(Long chatroomId, Long memberId, LastMessageRequest lastMessageRequest) {

        String chatroomKey = "chatroom-%d".formatted(chatroomId);
        redisManager(chatroomKey, memberId).subscribe();

        return Flux.empty();
    }

    public Mono<Void> disconnectChatroom(Long chatroomId, Long memberId) {

        String chatroomName = "chatroom-%d".formatted(chatroomId);

        return reactiveRedisTemplate.opsForValue().get(chatroomName)
                                    .flatMap(memberList -> {
                                        if(!memberList.remove(memberId)) {
                                            return Mono.error(new ChatroomException(ErrorCode.MEMBER_NOT_PART_OF_CHATROOM));
                                        };
                                        if (memberList.isEmpty()) {
                                            return reactiveRedisTemplate.delete(chatroomName).then();
                                        } else {
                                            return reactiveRedisTemplate.opsForValue().set(chatroomName, memberList).then();
                                        }
                                    })
                                    .then()
            .onErrorResume(error -> Mono.error(new ChatroomException(ErrorCode.MEMBER_NOT_PART_OF_CHATROOM)));
    }

    private Mono<Void> redisManager(String chatroomKey, Long memberId) {
        reactiveRedisTemplate.hasKey(chatroomKey)
                             .publishOn(
                                 Schedulers.boundedElastic()) // 블로킹 작업에 적합한 스레드 사용
                             .flatMap(exists -> {
                                 if (Boolean.FALSE.equals(exists)) {
                                     return storeMemberStatusWithCreateKey(chatroomKey,
                                         memberId);
                                 } else {
                                     return storeMemberStatus(chatroomKey, memberId);
                                 }
                             })
                             .subscribe(
                                 result -> {}, // onNext
                                 error -> log.error("Error on storing member status", error) // onError
                             );

        return Mono.empty();
    }

    private Mono<Boolean> storeMemberStatusWithCreateKey(String chatroomKey, Long memberId) {
        log.info(" {} member connected {} and stored by redis", memberId, chatroomKey);
        List<Long> memberList = List.of(memberId);
        return reactiveRedisTemplate.opsForValue()
                                    .set(chatroomKey, memberList);
    }

    private Mono<Boolean> storeMemberStatus(String chatroomKey, Long memberId) {
        log.info(" {} member connected {} by redis", memberId, chatroomKey);
        return reactiveRedisTemplate.opsForValue()
                                    .get(chatroomKey)
                                    .defaultIfEmpty(new ArrayList<>())
                                    .doOnNext(memberList -> memberList.add(memberId))
                                    .flatMap(memberList -> reactiveRedisTemplate.opsForValue()
                                                                                .set(chatroomKey, memberList));
    }
}
