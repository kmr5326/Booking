//package com.booking.booking.hashtag.domain;
//
//import com.booking.booking.hashtagmeeting.domain.HashtagMeeting;
//import lombok.AccessLevel;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//
//import javax.persistence.Entity;
//import javax.persistence.FetchType;
//import javax.persistence.GeneratedValue;
//import javax.persistence.GenerationType;
//import javax.persistence.Id;
//import javax.persistence.OneToMany;
//import javax.persistence.Table;
//import java.util.List;
//
//@Entity
//@Getter
//@Builder
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
//@AllArgsConstructor
//@Table(name = "hashtags")
//public class Hashtag {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private long hashtagId;
//
//    private String content;
//
//    @OneToMany(fetch = FetchType.LAZY, mappedBy = "hashtag")
//    private List<HashtagMeeting> hashtagMeetingList;
//}
