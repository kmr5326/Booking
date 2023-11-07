package com.booking.book.elastic.service;

import com.booking.book.book.service.BookService;
import com.booking.book.elastic.domain.ElasticBook;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.data.elasticsearch.core.ReactiveElasticsearchOperations;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

@RequiredArgsConstructor
@Service
public class DataSynchronizationService {

    private final BookService bookService;
    private final ReactiveElasticsearchOperations reactiveElasticsearchOperations;
    private static final int BATCH_SIZE = 1000;
    private static final int MAX_CONCURRENCY = 5;
    private static final int MAX_RETRIES = 3;
    private static final long RETRY_BACKOFF_SECONDS = 5;

    public Mono<Void> synchronize() {
        return bookService.findAll()
                          .map(book -> new ElasticBook(book.getIsbn(), book.getTitle()))
                          .buffer(BATCH_SIZE)
                          .flatMap(bookBatch ->
                                  Flux.fromIterable(bookBatch)

                                      .flatMap(reactiveElasticsearchOperations::save, MAX_CONCURRENCY)
                                      .then()

                                      .retryWhen(Retry.backoff(MAX_RETRIES, Duration.ofSeconds(RETRY_BACKOFF_SECONDS))
                                                      .doBeforeRetry(retrySignal ->
                                                          System.out.println("Retrying due to failure: " + retrySignal.failure()))
                                      )
                                      .onErrorResume(e -> {
                                          System.err.println("Failed to save a batch: " + e.getMessage());
                                          return Mono.empty();
                                      }),
                              MAX_CONCURRENCY
                          )
                          .then();
    }
}
