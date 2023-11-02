package com.booking.book.book.service;

import com.booking.book.book.domain.Book;
import com.booking.book.book.repository.BookRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@Service
public class BookService {

    private final BookRepository bookRepository;

    public Mono<Void> initializeSave(List<Book> bookList) {
        return bookRepository.saveAll(bookList).then();
    }

    public Mono<Boolean> initializeCheck() {
        return bookRepository.existsById("9788967351021");
    }
}
