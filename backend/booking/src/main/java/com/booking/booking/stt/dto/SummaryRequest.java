package com.booking.booking.stt.dto;

import lombok.Data;

@Data
public class SummaryRequest {
    private SummaryDocument document;
    private SummaryOption option;

    public SummaryRequest(String content) {
        this.document=new SummaryDocument(content);
        this.option=new SummaryOption();
    }
}
