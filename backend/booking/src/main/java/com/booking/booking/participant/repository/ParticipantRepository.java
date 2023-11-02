package com.booking.booking.participant.repository;

import com.booking.booking.meeting.domain.Meeting;
import com.booking.booking.participant.domain.Participant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ParticipantRepository extends JpaRepository<Participant, Long> {
    List<Participant> findAllByMeeting(Meeting meeting);
    boolean existsParticipantByMeetingAndMemberId(Meeting meeting, String memberId);
}
