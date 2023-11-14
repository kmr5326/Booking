package com.booking.booking.post.service;

import com.booking.booking.global.utils.MemberUtil;
import com.booking.booking.post.domain.Post;
import com.booking.booking.post.dto.response.PostListResponse;
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
    private final MemberUtil memberUtil;
    public Mono<Post> createPost(Post post) {
        return postRepository.save(post)
                .onErrorResume(error -> {
                    log.error("[Booking:Post ERROR] createPost : {}", error.getMessage());
                    return Mono.error(new RuntimeException("게시글 저장 실패"));
                });
    }

    public Flux<PostListResponse> findAllByMeetingId(Long meetingId) {
        return postRepository.findAllByMeetingId(meetingId)
                .flatMap(post -> memberUtil.getMemberInfoByPk(post.getMemberId())
                        .flatMap(member -> Mono.just(new PostListResponse(post, member))))
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
    
    public Mono<Post> updatePost(Post post) {
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

    public Mono<Void> deleteAllByMeetingId(Long meetingId) {
        return postRepository.deleteAllByMeetingId(meetingId)
                .onErrorResume(error -> {
                    log.error("[Booking:Post ERROR] deleteAllByMeetingId : {}", error.getMessage());
                    return Mono.error(new RuntimeException("게시글 삭제 실패"));
                });
    }
}
