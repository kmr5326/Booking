package com.booking.booking.post.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table("posts")
public class Post {
    @Id
    private long postId;

    private Long meetingId;

    private Integer memberId;

    private String title;

    private String content;

    private LocalDateTime createdAt;
}
