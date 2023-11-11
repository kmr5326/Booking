package com.booking.booking.post.repository;

import com.booking.booking.post.domain.Post;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface PostRepository extends R2dbcRepository<Post, Long> {
}
