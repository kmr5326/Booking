package com.booking.booking.stt.domain;

import com.booking.booking.stt.dto.SttResponseDto;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.Id;
import javax.persistence.PrePersist;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Document(collection = "transcriptions")
public class Transcription {

    @Id
    private String id;

    @Field("segments")
    private List<Segment> segments;

    @Field("text")
    private String text;

    @Field("speakers")
    private List<Speaker> speakers;

    @Field("created_at")
    @CreatedDate
    private LocalDateTime createdAt;

    public static Transcription of(SttResponseDto dto) {
        Transcription transcription = new Transcription();
        transcription.segments=dto.getSegments();
        transcription.speakers=dto.getSpeakers();
//        transcription.text=dto.getText();
        transcription.text = dto.getSegments().stream()
                .map(Segment::getText)
                .filter(Objects::nonNull) // null이 아닌 text 필드만 필터링
                .collect(Collectors.joining("\n"));
        return transcription;
    }
}
