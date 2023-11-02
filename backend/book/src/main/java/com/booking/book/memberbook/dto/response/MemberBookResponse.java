package com.booking.book.memberbook.dto.response;

import com.booking.book.memberbook.domain.MemberBook;
import com.booking.book.memberbook.domain.Note;
import java.time.LocalDateTime;
import java.util.List;

public record MemberBookResponse(
    Long memberId,
    Long bookIsbn,
    List<Note> notes,
    LocalDateTime createdAt
) {

    public MemberBookResponse(MemberBook memberBook) {
        this(
            memberBook.getMemberId(),
            memberBook.getBookIsbn(),
            memberBook.getNotes(),
            memberBook.getCreatedAt()
        );
    }

}
