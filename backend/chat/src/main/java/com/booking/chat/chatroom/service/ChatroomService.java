package com.booking.chat.chatroom.service;

import com.booking.chat.chatroom.domain.Chatroom;
import com.booking.chat.chatroom.dto.request.InitChatroomRequest;
import com.booking.chat.chatroom.dto.response.ChatroomListResponse;
import com.booking.chat.chatroom.repository.ChatroomRepository;
import java.util.Collections;
import java.util.List;
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

    public Mono<List<ChatroomListResponse>> getChatroomListByMemberId(Long memberId) {

        return chatroomRepository.findByMemberListContains(memberId)
            .flatMapMany(Flux::fromIterable)
            .map(chatroom -> new ChatroomListResponse(chatroom.get_id(), Collections.singletonList(chatroom)))
            .collectList();

    }
}
