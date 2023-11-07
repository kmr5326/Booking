package com.booking.booking.meeting.repository;

import com.booking.booking.meeting.domain.Meeting;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;
import reactor.core.publisher.Flux;

public interface MeetingRepository extends R2dbcRepository<Meeting, Long> {
    @Query(value = "SELECT *, " +
            "( 6371 * acos( cos( radians(:lat) ) * cos( radians( lat ) ) * cos( radians( lgt ) " +
            "- radians(:lgt) ) + sin( radians(:lat) ) * sin( radians( lat ) ) ) ) AS distance " +
            "FROM meetings GROUP BY meeting_id HAVING distance <= :radius ORDER BY distance ASC")
    Flux<Meeting> findAllByRadius(@Param("lat") double lat, @Param("lgt") double lgt, @Param("radius") double radius);
}
