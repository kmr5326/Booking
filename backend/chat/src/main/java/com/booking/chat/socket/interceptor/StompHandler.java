package com.booking.chat.socket.interceptor;

import com.booking.chat.global.jwt.JwtUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Slf4j
@RequiredArgsConstructor
@Component
public class StompHandler implements ChannelInterceptor {

    private final ReactiveRedisTemplate<String, List<Long>> reactiveRedisTemplate;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        if (accessor.getCommand() == StompCommand.CONNECT) {
            log.info(" stomp message info => {} ", message.toString());
            String token = accessor.getFirstNativeHeader("Authorization");
            if (token == null) {
                throw new RuntimeException("token not found");
            }
            Long chatroomId = Long.valueOf(Objects.requireNonNull(accessor.getFirstNativeHeader("chatroomId")));
            Long memberId = JwtUtil.getMemberIdByToken(token);
            log.info(" {} member request STOMP connect", memberId);

            String chatroomKey = "chatroom-%d".formatted(chatroomId);

            reactiveRedisTemplate.hasKey(chatroomKey)
                                 .publishOn(Schedulers.boundedElastic()) // 블로킹 작업에 적합한 스레드 사용
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

            return message;
        }

        return message;
    }

    private Mono<Boolean> storeMemberStatusWithCreateKey(String chatroomKey, Long memberId) {
        log.info(" {} member connected {} ", memberId, chatroomKey);
        List<Long> memberList = List.of(memberId);
        return reactiveRedisTemplate.opsForValue()
                                    .set(chatroomKey, memberList);
    }

    private Mono<Boolean> storeMemberStatus(String chatroomKey, Long memberId) {
        log.info(" {} member connected {} ", memberId, chatroomKey);
        return reactiveRedisTemplate.opsForValue()
                                    .get(chatroomKey)
                                    .defaultIfEmpty(new ArrayList<>())
                                    .doOnNext(memberList -> memberList.add(memberId))
                                    .flatMap(memberList -> reactiveRedisTemplate.opsForValue()
                                                                                .set(chatroomKey, memberList));
    }
}