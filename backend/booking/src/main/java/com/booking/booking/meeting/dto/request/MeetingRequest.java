package com.booking.booking.meeting.dto.request;

import com.booking.booking.meeting.domain.Meeting;
import com.booking.booking.meeting.domain.MeetingState;
import com.booking.booking.global.dto.MemberInfoResponse;

import java.util.List;

public record MeetingRequest(
    String bookIsbn,
    String meetingTitle,
    String description,
    Integer maxParticipants,
    List<String> hashtagList
) {
    public Meeting toEntity(MemberInfoResponse memberInfo, MeetingState meetingState) {
        return Meeting.builder()
                .leaderId(memberInfo.memberPk())
                .lat(memberInfo.lat())
                .lgt(memberInfo.lgt())
                .bookIsbn(bookIsbn)
                .meetingTitle(meetingTitle)
                .description(description)
                .maxParticipants(maxParticipants)
                .meetingState(meetingState)
                .build();
    }
}
