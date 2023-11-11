package com.ssafy.domain.usecase

import com.ssafy.domain.model.booking.BookingCreateRequest
import com.ssafy.domain.model.loacation.AddressResponse
import com.ssafy.domain.repository.LocationRepository
import retrofit2.Response
import javax.inject.Inject

class LocationUseCase @Inject constructor(private val repository: LocationRepository) {
    suspend fun getAddress(lng:String,lat:String): Response<AddressResponse> {
        return repository.getAddress(lng,lat)
    }
}
