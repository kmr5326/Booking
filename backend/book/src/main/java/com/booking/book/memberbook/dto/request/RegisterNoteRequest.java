package com.booking.book.memberbook.dto.request;

public record RegisterNoteRequest(
        String nickname,
        String isbn,
        String content
) {
    public String toString() {
        return "{ nickname: "+this.nickname+", isbn: "+this.isbn+", content: "+this.content+" }";
    }
}
