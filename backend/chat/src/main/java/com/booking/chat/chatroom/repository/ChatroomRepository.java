package com.booking.chat.chatroom.repository;

import com.booking.chat.chatroom.domain.Chatroom;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.mongodb.repository.Tailable;
import reactor.core.publisher.Flux;

public interface ChatroomRepository extends ReactiveMongoRepository<Chatroom, Long> {
   @Tailable
   Flux<Chatroom> findByMemberListContains(Long memberId);

   Flux<Chatroom> findByMemberListContainsOrderByLastMessageReceivedTimeDesc(Long memberId);


}
