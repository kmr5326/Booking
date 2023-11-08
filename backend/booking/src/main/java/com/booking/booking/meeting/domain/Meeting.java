package com.booking.booking.meeting.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "meetings")
public class Meeting {
    @Id
    private long meetingId;

    private Integer leaderId;

    private Double lat;

    private Double lgt;

    private String bookIsbn;

    private String meetingTitle;

    private String description;

    private Integer maxParticipants;

    private MeetingState meetingState;

    public Meeting updateState(MeetingState state) {
        return Meeting.builder()
                .meetingId(meetingId)
                .leaderId(leaderId)
                .lat(lat)
                .lgt(lgt)
                .bookIsbn(bookIsbn)
                .meetingTitle(meetingTitle)
                .description(description)
                .maxParticipants(maxParticipants)
                .meetingState(state)
                .build();
    }
}
