package com.ssafy.booking.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RecordViewModel @Inject constructor(

) : ViewModel() {
    private val _sliderPosition = MutableLiveData(0)
    val sliderPosition: LiveData<Int> = _sliderPosition

    override fun onCleared() {
        super.onCleared()
        _sliderPosition.value = 0
    }

    fun updateSliderPosition(newPosition: Int) {
        _sliderPosition.value = newPosition
    }


}