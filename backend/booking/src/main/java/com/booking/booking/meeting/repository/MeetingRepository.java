package com.booking.booking.meeting.repository;

import com.booking.booking.meeting.domain.Meeting;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface MeetingRepository extends R2dbcRepository<Meeting, Long> {
    @Query("SELECT *, " +
            "( 6371 * acos( cos( radians(:lat) ) * cos( radians( lat ) ) * cos( radians( lgt ) " +
            "- radians(:lgt) ) + sin( radians(:lat) ) * sin( radians( lat ) ) ) ) AS distance " +
            "FROM meetings GROUP BY meeting_id HAVING distance <= :radius ORDER BY distance ASC")
    Flux<Meeting> findAllByRadius(@Param("lat") double lat, @Param("lgt") double lgt, @Param("radius") double radius);

    @Query("SELECT m.*, " +
            "( 6371 * acos( cos( radians(:lat) ) * cos( radians( m.lat ) ) * cos( radians( m.lgt ) " +
            "- radians(:lgt) ) + sin( radians(:lat) ) * sin( radians( m.lat ) ) ) ) AS distance " +
            "FROM meetings m INNER JOIN hashtag_meeting hm ON m.meeting_id = hm.meeting_id " +
            "WHERE hm.hashtag_id = :hashtagId " +
            "GROUP BY m.meeting_id HAVING distance <= :radius ORDER BY distance ASC")
    Flux<Meeting> findAllByHashtagId(@Param("lat") double lat, @Param("lgt") double lgt,
                                     @Param("radius") double radius, @Param("hashtagId") long hashtagId);

    Mono<Meeting> findByMeetingId(Long meetingId);

    @Query("SELECT * " +
            "FROM meetings m JOIN participants p ON m.meeting_id = p.meeting_id " +
            "WHERE p.member_id = :memberId AND m.meeting_state IN ('PREPARING', 'ONGOING')")
    Flux<Meeting> findOngoingByMemberId(@Param("memberId") Integer memberId);

    @Query("SELECT * " +
            "FROM meetings m JOIN participants p ON m.meeting_id = p.meeting_id " +
            "WHERE p.member_id = :memberId AND m.meeting_state = 'FINISH'")
    Flux<Meeting> findFinishByMemberId(@Param("memberId") Integer memberId);
}
