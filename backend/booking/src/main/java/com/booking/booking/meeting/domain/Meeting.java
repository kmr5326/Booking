package com.booking.booking.meeting.domain;

import com.booking.booking.hashtagmeeting.domain.HashtagMeeting;
import com.booking.booking.meetinginfo.domain.MeetingInfo;
import com.booking.booking.participant.domain.Participant;
import com.booking.booking.post.domain.Post;
import com.booking.booking.waitlist.domain.Waitlist;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "meetings")
public class Meeting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long meetingId;

    private String leaderId;

    private String address;

    private String bookIsbn;

    @Column(length = 32)
    private String meetingTitle;

    private String description;

    @Min(2)
    @Max(6)
    private Integer maxParticipants;

    @Enumerated(EnumType.STRING)
    private MeetingState meetingState;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "meeting")
    private List<MeetingInfo> meetingInfoList;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "meeting")
    private List<Waitlist> waitList;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "meeting")
    private List<Participant> participantsList;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "meeting")
    private List<Post> postList;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "meeting")
    private List<HashtagMeeting> hashtagMeetingList;
}
