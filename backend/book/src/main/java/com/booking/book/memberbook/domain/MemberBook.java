package com.booking.book.memberbook.domain;

import com.booking.book.memberbook.dto.request.MemberBookRegistRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "member_books")
public class MemberBook {

    @Transient
    public static final String SEQUENCE_NAME = "memberbook_sequence";

    @Id
    private Long _id;

    private Long memberId;

    private String bookIsbn;

    private List<Note> notes;

    @CreatedDate
    private LocalDateTime createdAt;

    public static MemberBook from(MemberBookRegistRequest memberBookRegistRequest) {

       return MemberBook.builder()
           .bookIsbn(memberBookRegistRequest.bookIsbn())
           .memberId(memberBookRegistRequest.memberId())
           .notes(new ArrayList<>())
           .build();
    }

    public void setAutoIncrementId(Long id) {
        this._id = id;
    }
}
