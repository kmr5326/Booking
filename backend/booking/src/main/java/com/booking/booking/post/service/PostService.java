package com.booking.booking.post.service;

import com.booking.booking.post.domain.Post;
import com.booking.booking.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@Service
public class PostService {
    private final PostRepository postRepository;

    public Mono<Post> createPost(Post post) {
        return postRepository.save(post)
                .onErrorResume(error -> {
                    log.error("[Booking:Post ERROR] createPost : {}", error.getMessage());
                    return Mono.error(new RuntimeException("게시글 저장 실패"));
                });
    }

    public Flux<Post> findAllByMeetingId(Long meetingId) {
        return postRepository.findAllByMeetingId(meetingId)
                .onErrorResume(error -> {
                    log.error("[Booking:Post ERROR] findAllByMeetingId : {}", error.getMessage());
                    return Mono.error(new RuntimeException("게시글 목록 조회 실패"));
                });
    }

    public Mono<Post> findByPostId(Long postId) {
        return postRepository.findByPostId(postId)
                .onErrorResume(error -> {
                    log.error("[Booking:Post ERROR] findByPostId : {}", error.getMessage());
                    return Mono.error(new RuntimeException("게시글 상세 조회 실패"));
                });
    }
    
    public Mono<Post> updateByPostId(Post post) {
        return postRepository.save(post)
                .onErrorResume(error -> {
                    log.error("[Booking:Post ERROR] updateByPostId : {}", error.getMessage());
                    return Mono.error(new RuntimeException("게시글 수정 실패"));
                });
    }
    
    public Mono<Void> deleteByPostId(Long postId) {
        return postRepository.deleteByPostId(postId)
                .onErrorResume(error -> {
                    log.error("[Booking:Post ERROR] deleteByPostId : {}", error.getMessage());
                    return Mono.error(new RuntimeException("게시글 삭제 실패"));
                });
    }
}
