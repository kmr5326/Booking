package com.booking.book.book.repository;

import com.booking.book.book.domain.Book;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface BookRepository extends ReactiveMongoRepository<Book, String> {

    @Query(value = "{ '$text': { $search: ?0 } }",
        sort = "{ score: { $meta: 'textScore' } }")
    Flux<Book> findByTitleContaining(String title, Pageable pageable);
}
