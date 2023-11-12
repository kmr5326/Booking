package com.booking.booking.meetinginfo.dto.response;

import com.booking.booking.meetinginfo.domain.MeetingInfo;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

public record MeetingInfoResponse(
        // TODO stt 가져오기
        @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime date,
        String location,
        String address,
        Double lat,
        Double lgt,
        Integer fee
) {
    public MeetingInfoResponse(MeetingInfo meetingInfo) {
        this(meetingInfo.getDate(), meetingInfo.getLocation(), meetingInfo.getAddress(),
                meetingInfo.getLat(), meetingInfo.getLgt(), meetingInfo.getFee());
    }
}
