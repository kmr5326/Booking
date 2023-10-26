package com.booking.booking.meeting.domain;


import com.booking.booking.chatroom.domain.Chatroom;
import com.booking.booking.hashtag.domain.Hashtag;
import com.booking.booking.meetinginfo.domain.MeetingInfo;
import com.booking.booking.participant.domain.Participant;
import com.booking.booking.post.domain.Post;
import com.booking.booking.waitlist.domain.Waitlist;
import javax.persistence.EnumType;
import javax.persistence.FetchType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Size;
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

    private long leaderId;

    private String address;

    private String bookIsbn;

    @OneToMany(fetch = FetchType.LAZY)
    private List<MeetingInfo> meetingInfoList;

    @OneToOne(fetch = FetchType.LAZY)
    private Chatroom chatroom;

    @OneToMany(fetch = FetchType.LAZY)
    private List<Waitlist> waitList;

    @OneToMany(fetch = FetchType.LAZY)
    private List<Participant> participantsList;

    @OneToMany(fetch = FetchType.LAZY)
    private List<Post> postList;

    @OneToMany(fetch = FetchType.LAZY)
    private List<Hashtag> hashTagList;

    @Column(length = 32)
    private String meetingTitle;

    private String description;

    @Size(min = 2, max = 6)
    private Integer maxParticipants;

    @Enumerated(EnumType.STRING)
    private MeetingState meetingState;
}
