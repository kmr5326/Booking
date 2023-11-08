package com.booking.chat.chat.service;


import com.booking.chat.chat.domain.Message;
import com.booking.chat.chat.repository.MessageRepository;
import com.booking.chat.chatroom.domain.Chatroom;
import com.booking.chat.chatroom.service.ChatroomService;
import com.booking.chat.kafka.domain.KafkaMessage;
import com.booking.chat.notification.dto.response.NotificationResponse;
import com.booking.chat.notification.service.NotificationService;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final ChatroomService chatroomService;
    private final KafkaTemplate<String, KafkaMessage> kafkaTemplate;
    private final NotificationService notificationService;

    public void processAndSend(KafkaMessage kafkaMessage, Long chatroomId) {
        save(kafkaMessage, chatroomId)
            .retry(3L)
            .then(Mono.fromRunnable(() -> proceedMessageSendProcess(kafkaMessage, chatroomId)))
            .subscribe();
    }

    private void proceedMessageSendProcess(KafkaMessage kafkaMessage, Long chatroomId) {
        chatroomService.findByChatroomId(chatroomId)
                       .flatMapMany(chatroom -> {
                           String meetingTitle = chatroom.getMeetingTitle();
                           String message = kafkaMessage.getMessage();
                           String memberName = kafkaMessage.getSenderName();
                           List<Long> notificationList = new ArrayList<>(chatroom.getMemberList());
                           notificationList.remove(kafkaMessage.getSenderId());

                           // flux로 stream list 만들기
                           return Flux.fromIterable(notificationList)
                                      .flatMap(memberId -> notificationService.sendChattingNotification(new NotificationResponse(meetingTitle, message, memberName ,memberId, kafkaMessage.extractData(chatroomId))), 5)
                                      .thenMany(Flux.just(chatroom)); // flux로 이어가기
                       })
                       .then() // On completion of all notifications, continue to send the message to Kafka
                       .doOnSuccess(aVoid -> sendMessageToKafka(kafkaMessage, chatroomId))
                       .subscribe();
    }

    private void sendMessageToKafka(KafkaMessage kafkaMessage, Long chatroomId) {
        // Kafka로 메세지와 함께 채팅방 ID를 헤더에 추가하여 전달
        ProducerRecord<String, KafkaMessage> record = new ProducerRecord<>("Chatroom-" + chatroomId, null, null, kafkaMessage);
        record.headers().add("chatroomId", chatroomId.toString().getBytes(StandardCharsets.UTF_8));
        kafkaTemplate.send(record);

        // test
        // notificationService.sendChattingNotification(kafkaMessage.getSenderId()).subscribe();
    }

    public Mono<Void> save(KafkaMessage message, Long chatroomId) {
        return chatroomService.findByChatroomId(chatroomId)
                              .flatMap(chatroom -> {
                                  Long idx = chatroom.getMessageIndex();
                                  chatroom.updateIndex();
                                  // int readCount = getReadCount(chatroom, message.getSenderId()).block();
                                  return chatroomService.save(chatroom)
                                                        .then(Mono.just(Message.builder()
                                                                               .chatroomId(chatroomId)
                                                                               .messageId(idx)
                                                                               .memberId(message.getSenderId())
                                                                               .content(message.getMessage())
                                                                               .build()))
                                                        .flatMap(messageRepository::save);
                              })
                              .then();
    }

    public Flux<Message> findAllByRoomId(Long roomId) {

        return messageRepository.findByChatRoomId(roomId);
    }

    // TODO : redis
    private Mono<Integer> getReadCount(Chatroom chatroom, Long senderId) {

        int totalMemberNumber = chatroom.getMemberList().size() - 1;
        int cachingChatroomMember = 0; // do something

        return Mono.just(totalMemberNumber - cachingChatroomMember);
    }
}
