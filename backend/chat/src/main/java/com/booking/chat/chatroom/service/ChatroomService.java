package com.booking.chat.chatroom.service;

import com.booking.chat.chatroom.domain.Chatroom;
import com.booking.chat.chatroom.dto.request.ExitChatroomRequest;
import com.booking.chat.chatroom.dto.request.InitChatroomRequest;
import com.booking.chat.chatroom.dto.request.JoinChatroomRequest;
import com.booking.chat.chatroom.dto.response.ChatroomListResponse;
import com.booking.chat.chatroom.exception.ChatroomException;
import com.booking.chat.chatroom.repository.ChatroomRepository;
import com.booking.chat.global.exception.ErrorCode;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChatroomService {

    private final ChatroomRepository chatroomRepository;

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
}
