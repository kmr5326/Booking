package com.booking.booking.waitlist.repository;

import com.booking.booking.waitlist.domain.Waitlist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WaitlistRepository extends JpaRepository<Waitlist, Long> {
}
