package com.booking.book.elastic.controller;

import com.booking.book.book.dto.response.BookResponse;
import com.booking.book.elastic.service.DataSynchronizationService;
import com.booking.book.elastic.service.ElasticsearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/book/elastic")
@RestController
public class ElasticsearchController {

    private final ElasticsearchService elasticsearchService;
    private final DataSynchronizationService dataSynchronizationService;


    @GetMapping("/init")
    public Mono<Void> dataInitializer() {
        return dataSynchronizationService.synchronize();
    }

    @GetMapping("")
    public Flux<BookResponse> searchBookListByTitleUseElastic(@RequestParam("title") String title) {
        log.info(" search request for the book {} ", title);
        return elasticsearchService.findByTitleUseElasticsearch(title)
                                   .map(BookResponse::new);
    }

}
