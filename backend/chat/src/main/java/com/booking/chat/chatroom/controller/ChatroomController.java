package com.booking.chat.chatroom.controller;

import com.booking.chat.chatroom.dto.request.InitChatroomRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/chat")
@RestController
public class ChatroomController {


    @PostMapping("/room")
    public Mono<ResponseEntity<Void>> initializeChatroom(@RequestBody InitChatroomRequest initChatroomRequest) {
        log.info(" Request to create room number : {} , the LeaderId is : {} ", initChatroomRequest.meetingId(), initChatroomRequest.leaderId());




    }
}
