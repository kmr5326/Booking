package com.booking.chat.message.repository;

import com.booking.chat.message.domain.Message;
import java.time.LocalDateTime;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.mongodb.repository.Tailable;
import reactor.core.publisher.Flux;

public interface MessageRepository extends ReactiveMongoRepository<Message, Long> {

    @Tailable
    @Query("{ chatroomId:  ?0}")
    Flux<Message> findByChatRoomId(Long roomId);

    //@Query("{ 'chatroomId': ?0, '_id': { $gt: ?1 } }")
    @Query("{ 'chatroomId': ?0, '_id': { $gte: ?1 } }")
    Flux<Message> findByChatroomIdAndMessageIdGreaterThanEqual(Long chatroomId, String MessageId);

    @Query("{ 'chatroomId': ?0, 'timestamp': { $gt: ?1 } }")
    Flux<Message> findByChatroomIdAndTimestampGreaterThanEqual(Long chatroomId, LocalDateTime lastMessageTimestamp);

    @Query("{ 'chatroomId': ?0 }")
    Flux<Message> findLatestByChatroomId(Long chatroomId, Pageable pageable);


}
