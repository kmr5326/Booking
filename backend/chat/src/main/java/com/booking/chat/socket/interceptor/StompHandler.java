package com.booking.chat.socket.interceptor;

import com.booking.chat.global.jwt.JwtUtil;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class StompHandler implements ChannelInterceptor {

    private final RedisTemplate<String, List<Long>> redisTemplate;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        if(accessor.getCommand() == StompCommand.CONNECT) {
            String token = accessor.getFirstNativeHeader("Authorization");
            if(token == null) {
                throw new RuntimeException("token not found");
            }
            Long chatroomId = Long.valueOf(Objects.requireNonNull(accessor.getFirstNativeHeader("chatroomId")));
            Long memberId = JwtUtil.getMemberIdByToken(token);

            String chatroomKey = "chatroom-%d".formatted(chatroomId);

            if(Boolean.FALSE.equals(redisTemplate.hasKey(chatroomKey))) {
                storeMemberStatusWithCreateKey(chatroomKey, memberId);
            }else {
                storeMemberStatus(chatroomKey, memberId);
            }
        }

        return message;
    }

    private void storeMemberStatusWithCreateKey(String chatroomKey, Long memberId) {
        log.info(" {} member connected {} " , memberId, chatroomKey);
        List<Long> memberList = List.of(memberId);
        redisTemplate.opsForValue().set(chatroomKey, memberList);
    }

    private void storeMemberStatus(String chatroomKey, Long memberId) {
        log.info(" {} member connected {} " , memberId, chatroomKey);
        List<Long> memberList = redisTemplate.opsForValue().get(chatroomKey);
        memberList.add(memberId);
        redisTemplate.opsForValue().set(chatroomKey, memberList);
    }
}
