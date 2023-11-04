package com.booking.book.memberbook.dto.response;

import com.booking.book.book.domain.Book;

public record MemberBookListResponse(
    String bookIsbn,
    String title,
    String coverImg
) {
    public MemberBookListResponse(Book book) {
        this(
            book.getIsbn(),
            book.getTitle(),
            book.getCoverImage()
        );
    }
}
