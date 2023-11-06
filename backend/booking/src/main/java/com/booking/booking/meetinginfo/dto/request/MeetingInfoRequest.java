//package com.booking.booking.meetinginfo.dto.request;
//
//import com.booking.booking.meeting.domain.Meeting;
//import com.booking.booking.meetinginfo.domain.MeetingInfo;
//import org.springframework.format.annotation.DateTimeFormat;
//
//import java.time.LocalDateTime;
//
//public record MeetingInfoRequest(
//    long meetingId,
//    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
//    LocalDateTime date,
//    String location,
//    Integer fee
//) {
//    public MeetingInfo toEntity(Meeting meeting) {
//        return MeetingInfo.builder()
//                .meeting(meeting)
//                .date(date)
//                .location(location)
//                .fee(fee)
//                .build();
//    }
//}