package com.ssafy.booking.google

import com.google.gson.annotations.SerializedName

data class GoogleUserModel(

    @SerializedName("name")
    val name: String?,
    @SerializedName("email")
    val email: String?
)