package com.booking.booking.waitlist.service;

import com.booking.booking.meeting.domain.Meeting;
import com.booking.booking.waitlist.domain.Waitlist;
import com.booking.booking.waitlist.repository.WaitlistRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@Service
public class WaitlistService {
    private final WaitlistRepository waitlistRepository;

    public Mono<Void> enrollMeeting(Meeting meeting, String memberId) {
        // error
        checkDuplicated(meeting, memberId);

        waitlistRepository.save(
                Waitlist.builder()
                        .meeting(meeting)
                        .memberId(memberId)
                        .build()
        );
        return Mono.empty();
    }

    private void checkDuplicated(Meeting meeting, String memberId) {

        // 기존 미팅 참가자 | 대기 목록에 있는 사람이면 에러를 던져야 한다
//        participantService.findAllMemberByMeeting(meeting)


        /*
         existBy -> boolean
         if(exist) throw new MeetingException(Errorcode.XXX)
         */


    }
}
