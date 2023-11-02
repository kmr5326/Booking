package com.booking.booking.meetinginfo.dto.request;

import java.time.LocalDateTime;

public record MeetingInfoRequest(
    long meetingId,
    LocalDateTime date,
    String location,
    Integer fee
) {

}