package com.booking.booking.stt.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "transcriptions")
public class Summary {
    @Id
    @Field("summary_id")
    private String id;

    @Field
    private String text;

    @Field("transcription_id")
    private String transcriptionId;

    public Summary of(String text,String transcriptionId){
        return new Summary(text,transcriptionId);
    }

    Summary (String text,String transcriptionId){
        this.text=text;
        this.transcriptionId=transcriptionId;
    }
}
