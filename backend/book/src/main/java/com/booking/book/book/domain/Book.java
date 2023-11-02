package com.booking.book.book.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "book")
public class Book {

    @Id private String isbn;
    private String title;
    private String author;
    private String coverImage;
    private String genre;


}
