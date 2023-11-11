package com.ssafy.booking.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.data.repository.FirebaseRepositoryImpl
import com.ssafy.domain.model.booking.BookingCreateRequest
import com.ssafy.domain.model.loacation.AddressResponse
import com.ssafy.domain.usecase.BookingUseCase
import com.ssafy.domain.usecase.LocationUseCase
import com.ssafy.domain.usecase.MyPageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class LocationViewMedel @Inject constructor(
    private val locationUseCase: LocationUseCase
):ViewModel(){
    // GET - 위경도 주소 변환
    private val _getAddressResponse = MutableLiveData<Response<AddressResponse>>()
    val getAddressResponse: LiveData<Response<AddressResponse>> get() = _getAddressResponse
    fun getAddress(lng: String, lat: String) =
        viewModelScope.launch {
            val response = locationUseCase.getAddress(lng, lat)
            _getAddressResponse.value = response
        }
}
