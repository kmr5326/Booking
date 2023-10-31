package com.booking.chat.mongo.util;

import com.booking.chat.chat.domain.Message;
import com.booking.chat.mongo.service.SequenceGeneratorService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class MessageIncrementListener extends AbstractMongoEventListener<Message> {

    private final SequenceGeneratorService sequenceGeneratorService;

    @Override
    public void onBeforeConvert(BeforeConvertEvent<Message> event) {
        event.getSource().setAutoIncrementId(sequenceGeneratorService.generateSequence(Message.SEQUENCE_NAME));
    }
}
