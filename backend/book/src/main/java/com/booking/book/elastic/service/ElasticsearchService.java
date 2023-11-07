package com.booking.book.elastic.service;

import com.booking.book.book.domain.Book;
import com.booking.book.book.service.BookService;
import com.booking.book.elastic.domain.ElasticBook;
import lombok.RequiredArgsConstructor;
import org.springframework.data.elasticsearch.core.ReactiveElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@RequiredArgsConstructor
@Service
public class ElasticsearchService {

    private final ReactiveElasticsearchOperations reactiveElasticsearchOperations;
    private final BookService bookService;

    public Flux<Book> findByTitleUseElasticsearch(String title) {
        Criteria criteria = new Criteria("title").contains(title);
        Query searchQuery = new CriteriaQuery(criteria);

        return reactiveElasticsearchOperations.search(searchQuery, ElasticBook.class)
                                                                           .map(SearchHit::getContent)
                                                                           .flatMap(elasticBook -> bookService.findByIsbn(elasticBook.getId()));
    }

}
