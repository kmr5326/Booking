package com.booking.booking.meeting.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

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

    @Min(2)
    @Max(6)
    private Integer maxParticipants;

//    @Enumerated(EnumType.STRING)
    private MeetingState meetingState;
//
//    @OneToMany(fetch = FetchType.LAZY, mappedBy = "meeting")
//    private List<MeetingInfo> meetingInfoList;
//
//    @OneToMany(fetch = FetchType.LAZY, mappedBy = "meeting")
//    private List<Waitlist> waitList;
//
//    @OneToMany(fetch = FetchType.LAZY, mappedBy = "meeting")
//    private List<Participant> participantsList;
//
//    @OneToMany(fetch = FetchType.LAZY, mappedBy = "meeting")
//    private List<Post> postList;
//
//    @OneToMany(fetch = FetchType.EAGER, mappedBy = "meeting")
//    private List<HashtagMeeting> hashtagMeetingList;
}
