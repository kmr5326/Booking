package com.booking.book.memberbook.dto.response;

public record MemberBookListResponse(
    Long bookIsbn,
    String title,
    String coverImg
) {

}
