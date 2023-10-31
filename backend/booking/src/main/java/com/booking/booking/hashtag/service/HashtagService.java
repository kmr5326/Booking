package com.booking.booking.hashtag.service;

import com.booking.booking.hashtag.domain.Hashtag;
import com.booking.booking.hashtag.repository.HashtagRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class HashtagService {
    private final HashtagRepository hashtagRepository;

    public Optional<Hashtag> findByContent(String content) {
        return hashtagRepository.findByContent(content);
    }

    public Hashtag save(String content) {
        return hashtagRepository.save(
                Hashtag.builder()
                        .content(content)
                        .build());
    }
}
