package com.booking.book.memberbook.dto.request;

public record MemberBookRegistRequest(
    Long memberId,
    String bookIsbn
) {

}
