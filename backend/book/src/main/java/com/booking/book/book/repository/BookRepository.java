package com.booking.book.book.repository;

import com.booking.book.book.domain.Book;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface BookRepository extends ReactiveMongoRepository<Book, String> {

}
