//package com.booking.booking.hashtagmeeting.domain;
//
//import com.booking.booking.hashtag.domain.Hashtag;
//import com.booking.booking.meeting.domain.Meeting;
//import lombok.AccessLevel;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//
//import javax.persistence.Entity;
//import javax.persistence.GeneratedValue;
//import javax.persistence.GenerationType;
//import javax.persistence.Id;
//import javax.persistence.ManyToOne;
//import javax.persistence.Table;
//
//@Entity
//@Getter
//@Builder
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
//@AllArgsConstructor
//@Table(name = "hashtag_meeting")
//public class HashtagMeeting {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private long meetingHashtagId;
//
//    @ManyToOne
//    private Meeting meeting;
//
//    @ManyToOne
//    private Hashtag hashtag;
//}
