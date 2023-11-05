package com.booking.booking.waitlist.dto.response;

import com.booking.booking.waitlist.domain.Waitlist;

public record WaitlistResponse(
    Integer memberPk
) {
    public WaitlistResponse(Waitlist waitlist) {
        this(waitlist.getMemberId());
    }
}
