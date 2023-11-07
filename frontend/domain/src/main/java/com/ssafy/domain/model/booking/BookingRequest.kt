package com.ssafy.domain.model.booking

// 보낼 떄는 상관없는데, 받을 때는 serializeName이 필요함. -> gson의 매칭을 위해서.
data class BookingCreateRequest (
    val bookIsbn : String,
    val meetingTitle: String,
    val description : String,
    val maxParticipants : Number,
    val hashtagList : List<String>,
)