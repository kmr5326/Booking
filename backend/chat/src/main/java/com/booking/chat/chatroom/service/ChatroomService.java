package com.booking.chat.chatroom.service;

import com.booking.chat.chatroom.domain.Chatroom;
import com.booking.chat.chatroom.dto.request.InitChatroomRequest;
import com.booking.chat.chatroom.dto.request.JoinChatroomRequest;
import com.booking.chat.chatroom.dto.response.ChatroomListResponse;
import com.booking.chat.chatroom.exception.ChatroomException;
import com.booking.chat.chatroom.repository.ChatroomRepository;
import com.booking.chat.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChatroomService {

    private final ChatroomRepository chatroomRepository;

    public Mono<Chatroom> initializeChatroom(InitChatroomRequest initChatroomRequest) {

        Chatroom chatroom = Chatroom.createWithLeader(initChatroomRequest);
        return chatroomRepository.save(chatroom);
    }

    public Mono<Void> joinChatroom(JoinChatroomRequest joinChatroomRequest) {
        return chatroomRepository.findById(joinChatroomRequest.meetingId())
            .switchIfEmpty(Mono.error(new ChatroomException(ErrorCode.CHATROOM_NOT_FOUND)))
            .flatMap(chatroom -> {
                chatroom.getMemberList().add(joinChatroomRequest.memberId());
                return chatroomRepository.save(chatroom);
            })
            .then();
    }

    public Flux<ChatroomListResponse> getChatroomListByMemberId(Long memberId) {
        return chatroomRepository.findByMemberListContains(memberId)
                                 .map(ChatroomListResponse::from);
    }
}
