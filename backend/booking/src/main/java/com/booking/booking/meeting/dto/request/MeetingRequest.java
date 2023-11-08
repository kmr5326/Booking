package com.booking.booking.meeting.dto.request;

import com.booking.booking.global.dto.response.MemberResponse;
import com.booking.booking.meeting.domain.Meeting;
import com.booking.booking.meeting.domain.MeetingState;

import java.util.List;

public record MeetingRequest(
    String bookIsbn,
    String meetingTitle,
    String description,
    Integer maxParticipants,
    List<String> hashtagList
) {
    public Meeting toEntity(MemberResponse member, MeetingState meetingState) {
        return Meeting.builder()
                .leaderId(member.memberPk())
                .lat(member.lat())
                .lgt(member.lgt())
                .bookIsbn(bookIsbn)
                .meetingTitle(meetingTitle)
                .description(description)
                .maxParticipants(maxParticipants)
                .meetingState(meetingState)
                .build();
    }
}
