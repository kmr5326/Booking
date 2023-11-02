package com.booking.book.book.service;

import com.booking.book.book.domain.Book;
import com.booking.book.book.dto.response.BookResponse;
import com.booking.book.book.repository.BookRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.index.TextIndexDefinition;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@Service
public class BookService {

    private final BookRepository bookRepository;
    private final ReactiveMongoTemplate reactiveMongoTemplate;
    public Mono<Void> initializeSave(List<Book> bookList) {
        return bookRepository.saveAll(bookList).then();
    }

    public Mono<Boolean> initializeCheck() {
        return bookRepository.existsById("9788967351021");
    }

    public Mono<String> ensureTextIndexOnTitle() {
        TextIndexDefinition textIndex = new TextIndexDefinition.TextIndexDefinitionBuilder()
            .onField("title")
            .build();

        return reactiveMongoTemplate.indexOps(Book.class)
                                    .ensureIndex(textIndex)
                                    .doOnSuccess(indexName -> log.info("Text index created on title with name: {}", indexName))
                                    .doOnError(e -> log.error("Error creating text index on title", e));
    }

    public Flux<BookResponse> searchBookListByTitleAndRelevance(String title) {
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Direction.DESC, "score"));

        return bookRepository.findByTitleContaining(title, pageable)
                             .map(BookResponse::new);
    }

    public Flux<BookResponse> searchBookListByTitleRegex(String title) {
        String regex = ".*%s.*".formatted(title);
        return bookRepository.findByTitleRegex(regex)
                             .map(BookResponse::new);
    }
}
