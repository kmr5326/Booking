package com.booking.booking.stt.dto;

import lombok.Data;

import java.util.List;

@Data
public class GptResponseDto {
    private String id;
//    private String object;
    private long created;
//    private String model;
//    private String systemFingerprint;
    private List<ChoiceDTO> choices;

    @Data
    public static class ChoiceDTO {
        private int index;
        private MessageDto message;
        private String finishReason;

    }

}
