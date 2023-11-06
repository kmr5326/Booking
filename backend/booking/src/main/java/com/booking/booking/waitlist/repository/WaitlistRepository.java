//package com.booking.booking.waitlist.repository;
//
//import com.booking.booking.meeting.domain.Meeting;
//import com.booking.booking.waitlist.domain.Waitlist;
//import org.springframework.data.jpa.repository.JpaRepository;
//
//import javax.transaction.Transactional;
//import java.util.List;
//
//public interface WaitlistRepository extends JpaRepository<Waitlist, Long> {
//    boolean existsByMeetingAndMemberId(Meeting meeting, Integer memberId);
//    @Transactional
//    void deleteByMeetingAndMemberId(Meeting meeting, Integer memberId);
//    List<Waitlist> findAllByMeetingMeetingId(Long meetingId);
//}
