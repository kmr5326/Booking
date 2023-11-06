package com.booking.chat.mongo.util;

import com.booking.chat.chat.domain.Message;
import com.booking.chat.chatroom.service.ChatroomService;
import com.booking.chat.mongo.service.SequenceGeneratorService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.AfterSaveEvent;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;
import reactor.core.scheduler.Schedulers;

@RequiredArgsConstructor
@Component
public class MessageIncrementListener extends AbstractMongoEventListener<Message> {

    private final SequenceGeneratorService sequenceGeneratorService;
    private final ChatroomService chatroomService;

    @Override
    public void onBeforeConvert(BeforeConvertEvent<Message> event) {
        event.getSource().setAutoIncrementId(sequenceGeneratorService.generateSequence(Message.SEQUENCE_NAME));
    }

    @Override
    public void onAfterSave(AfterSaveEvent<Message> event) {
        long chatroomId = event.getSource().getChatroomId();
        chatroomService.findByChatroomId(chatroomId)
                       .publishOn(Schedulers.boundedElastic())
                       .doOnNext(chatroom -> {
            chatroom.updateListMessageReceived();
            chatroomService.save(chatroom).subscribe();
        }).subscribe();
    }
}
