package com.booking.book.memberbook.dto.response;

import com.booking.book.book.domain.Book;
import com.booking.book.book.dto.response.BookResponse;
import com.booking.book.memberbook.domain.MemberBook;
import com.booking.book.memberbook.domain.Note;
import java.time.LocalDateTime;
import java.util.List;

public record MemberBookResponse(
    String memberNickname,
    BookResponse bookInfo,
    List<Note> notes,
    LocalDateTime createdAt
) {
    public MemberBookResponse(MemberBook memberBook, Book book) {
        this(
            memberBook.getMemberNickname(),
            new BookResponse(book),
            memberBook.getNotes(),
            memberBook.getCreatedAt()
        );
    }
}
