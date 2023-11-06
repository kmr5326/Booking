package com.booking.booking.meetinginfo.repository;

import com.booking.booking.meetinginfo.domain.MeetingInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MeetingInfoRepository extends JpaRepository<MeetingInfo, Long> {
}
