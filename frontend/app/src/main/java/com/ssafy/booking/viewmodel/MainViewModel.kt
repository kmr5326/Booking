package com.ssafy.booking.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.booking.utils.ScreenState
import com.ssafy.booking.utils.SingleLiveEvent
import com.ssafy.domain.model.GoogleResponse
import com.ssafy.domain.usecase.GetTokenRepoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private val getTokenRepoUseCase: GetTokenRepoUseCase
) : ViewModel() {

    val eventUserRepo: MutableLiveData<List<GoogleResponse>?> get() = _eventUserRepo
    private val _eventUserRepo = MutableLiveData<List<GoogleResponse>?>()

    fun getUserRepo() = viewModelScope.launch {
        try {
            val response = getTokenRepoUseCase.execute()
            if (response == null) {
                Log.e("getUserRepo", "Response is null")
            } else {
                // 필요한 로직 처리
                _eventUserRepo.postValue(response)
            }
        } catch (e: Exception) {
            Log.e("getUserRepo", "Error occurred: ${e.message}", e)
        }
    }

}