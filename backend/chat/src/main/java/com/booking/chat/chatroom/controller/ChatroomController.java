package com.booking.chat.chatroom.controller;

import com.booking.chat.chatroom.dto.request.InitChatroomRequest;
import com.booking.chat.chatroom.dto.response.ChatroomListResponse;
import com.booking.chat.chatroom.service.ChatroomService;
import com.booking.chat.global.jwt.JwtUtil;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/chat/room")
@RestController
public class ChatroomController {

    private final ChatroomService chatroomService;
    private static final String AUTHORIZATION = "Authorization";

    @PostMapping("/")
    public Mono<ResponseEntity<Void>> initializeChatroom(@RequestBody InitChatroomRequest initChatroomRequest) {
        return chatroomService.initializeChatroom(initChatroomRequest)
                              .flatMap(chatroom -> {
                                  log.info(
                                      " Request to create room number : {} , the LeaderId is : {} ",
                                      initChatroomRequest.meetingId(),
                                      initChatroomRequest.leaderId());
                                  return Mono.just(new ResponseEntity<Void>(HttpStatus.CREATED));
                              })
                              .defaultIfEmpty(ResponseEntity.badRequest()
                                                            .build());
    }

    @GetMapping("/list")
    public Mono<ResponseEntity<List<ChatroomListResponse>>> getChatroomListByMemberId(@RequestHeader(AUTHORIZATION) String token) {

        Long memberId = JwtUtil.getMemberIdByToken(token);

        return chatroomService.getChatroomListByMemberId(memberId)
                              .map(ResponseEntity::ok);
    }


}
