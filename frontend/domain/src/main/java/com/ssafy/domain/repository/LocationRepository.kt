package com.ssafy.domain.repository

import com.ssafy.domain.model.loacation.AddressResponse
import retrofit2.Response

interface LocationRepository {
    suspend fun getAddress(lng:String,lat:String) : Response<AddressResponse>
}