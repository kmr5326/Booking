package com.booking.booking.meetinginfo.dto.request;

import com.booking.booking.meetinginfo.domain.MeetingInfo;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

public record MeetingInfoRequest(
        // TODO 모임 장소에 해당하는 장소이름, 주소명, 위도,경도
        long meetingId,
        @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime date,
        String location,
        String address,
        Double lat,
        Double lgt,
        Integer fee
) {
    public MeetingInfo toEntity() {
        return MeetingInfo.builder()
                .meetingId(meetingId)
                .date(date)
                .location(location)
                .address(address)
                .lat(lat)
                .lgt(lgt)
                .fee(fee)
                .build();
    }
}