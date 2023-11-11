package com.booking.booking.post.service;

import com.booking.booking.post.domain.Post;
import com.booking.booking.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@Service
public class PostService {
    private final PostRepository postRepository;

    public Mono<Post> createPost(Post post) {
        return postRepository.save(post);
    }
}
