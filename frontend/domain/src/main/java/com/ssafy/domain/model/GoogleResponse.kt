package com.ssafy.domain.model

import com.google.gson.annotations.SerializedName

data class GoogleResponse (

    @SerializedName("token")
    val token: String
)