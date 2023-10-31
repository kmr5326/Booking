package com.booking.booking.participant.repository;

import com.booking.booking.participant.domain.Participant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParticipantRepository extends JpaRepository<Participant, Long> {
}
