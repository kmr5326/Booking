package com.booking.book.book.dto.response;

import com.booking.book.book.domain.Book;

public record BookResponse (
    String title,
    String author,
    String coverImage,
    String genre,
    String content
) {

    public BookResponse(Book book) {
        this(
            book.getTitle(),
            book.getAuthor(),
            book.getCoverImage(),
            book.getGenre(),
            book.getContent()
        );
    }
}
