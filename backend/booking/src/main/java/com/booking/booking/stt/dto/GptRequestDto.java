package com.booking.booking.stt.dto;

import lombok.Data;

import java.util.List;

@Data
public class GptRequestDto {
    private String model;
    private List<MessageDto> messages;

}
