package com.booking.chat.chatroom.repository;

import com.booking.chat.chatroom.domain.Chatroom;
import java.util.List;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface ChatroomRepository extends ReactiveMongoRepository<Chatroom, Long> {

    Mono<List<Chatroom>> findByMemberListContains(Long memberId);

}
