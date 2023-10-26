package com.booking.booking.meeting.service;

import com.booking.booking.meeting.repository.MeetingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class MeetingService {

    private final MeetingRepository meetingRepository;
}
