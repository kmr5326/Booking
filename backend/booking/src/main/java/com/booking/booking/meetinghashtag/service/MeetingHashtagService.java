package com.booking.booking.meetinghashtag.service;

import com.booking.booking.hashtag.domain.Hashtag;
import com.booking.booking.hashtag.service.HashtagService;
import com.booking.booking.meeting.domain.Meeting;
import com.booking.booking.meetinghashtag.domain.MeetingHashtag;
import com.booking.booking.meetinghashtag.repository.MeetingHashtagRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class MeetingHashtagService {
    private final MeetingHashtagRepository meetingHashtagRepository;
    private final HashtagService hashtagService;

    public void saveHashtags(Meeting meeting, List<String> hashtagList) {
        hashtagList.stream()
                .map(content -> hashtagService.findByContent(content)
                        .orElseGet(() -> hashtagService.save(content)))
                .forEach(hashtag -> mapHashtagToMeeting(meeting, hashtag));
    }

    private Long mapHashtagToMeeting(Meeting meeting, Hashtag hashtag) {
        return meetingHashtagRepository.save(
                MeetingHashtag.builder()
                        .meeting(meeting)
                        .hashtag(hashtag)
                        .build()
        ).getMeetingHashtagId();
    }

}
