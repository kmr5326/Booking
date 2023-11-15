package com.ssafy.domain.model

import com.google.gson.annotations.SerializedName

data class RecordFileNameRequest (
    @SerializedName("fileName")
    val fileName : String,
    @SerializedName("meetingInfoId")
    val meetingInfoId : String,
)

data class CreateSummaryRequest(
    var content: String,
    val transcriptionId: String
)