package com.booking.booking.meeting.repository;

import com.booking.booking.meeting.domain.Meeting;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MeetingRepository extends JpaRepository<Meeting, Long> {
    Optional<Meeting> findById(Long id);

//    SELECT *, ( 6371 * acos( cos( radians(-1.0) ) * cos( radians( `lat` ) ) * cos( radians( `lgt` ) - radians(-1.0) ) + sin( radians(-1.0) ) * sin( radians( `lat` ) ) ) ) AS distance
//    FROM `meetings`
//    GROUP BY meeting_id
//    HAVING distance <= 10
//    ORDER BY distance ASC

//    @Query(value =
//            "SELECT *, " +
//            "( 6371 * acos( cos( radians(:lat) ) * cos( radians( lat ) ) * cos( radians( lgt ) - radians(:lgt) ) " +
//            "+ sin( radians(:lat) ) * sin( radians( lat ) ) ) ) AS distance " +
//            "FROM meetings GROUP BY meeting_id HAVING distance <= :radius ORDER BY distance ASC",
//            nativeQuery = true)
//    List<Meeting> findMeetingsWithinRadius
//        (@Param("lat") double lat, @Param("lgt") double lgt, @Param("radius") double radius);
}
