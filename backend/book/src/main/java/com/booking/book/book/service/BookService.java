package com.booking.book.book.service;

import com.booking.book.book.domain.Book;
import com.booking.book.book.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@Service
public class BookService {

    private final BookRepository bookRepository;

    public Mono<Void> initializeSave(Book book) {
        return bookRepository.save(book).then();
    }
}
