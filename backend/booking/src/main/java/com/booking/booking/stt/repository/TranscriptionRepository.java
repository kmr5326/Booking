package com.booking.booking.stt.repository;

import com.booking.booking.stt.domain.Transcription;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface TranscriptionRepository extends ReactiveMongoRepository<Transcription,String> {

}
