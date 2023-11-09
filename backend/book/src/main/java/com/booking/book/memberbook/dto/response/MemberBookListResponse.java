package com.booking.book.memberbook.dto.response;

import com.booking.book.book.domain.Book;
import com.booking.book.book.dto.response.BookResponse;

import java.time.LocalDate;

public record MemberBookListResponse(
        BookResponse bookInfo,
        String memberNickname
) {
    public MemberBookListResponse(Book book,String nickname) {
        this(
                new BookResponse(book),
                nickname
        );
    }
}
