package com.booking.booking.post.dto.response;

import com.booking.booking.global.dto.response.MemberResponse;
import com.booking.booking.post.domain.Post;

import java.time.LocalDateTime;

public record PostResponse(
    Long meetingId,
    Integer memberId,
    String nickname,
    String profileImage,
    String title,
    String content,
    LocalDateTime createdAt
) {
    public PostResponse(Post post, MemberResponse member) {
        this(post.getMeetingId(), member.memberPk(), member.nickname(), member.profileImage(),
                post.getTitle(), post.getContent(), post.getCreatedAt());
    }
}
