package com.booking.book.book.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.TextScore;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "book")
public class Book {

    @Id @Field("isbn")
    private String isbn;

    @TextIndexed @Field("title")
    private String title;

    @Field("author")
    private String author;

    @Field("coverImage")
    private String coverImage;

    @Field("genre")
    private String genre;

    @Field("content")
    private String content;

    @TextScore private Float score;

}
