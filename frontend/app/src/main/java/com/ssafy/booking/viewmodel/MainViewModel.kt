package com.ssafy.booking.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.booking.utils.ScreenState
import com.ssafy.booking.utils.SingleLiveEvent
import com.ssafy.domain.model.GoogleResponse
import com.ssafy.domain.model.google.AccountInfo
import com.ssafy.domain.usecase.GetTokenRepoUseCase
import com.ssafy.domain.usecase.google.AccountUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private val accountUseCase: AccountUseCase
) : ViewModel() {

    val accountInfo = accountUseCase.getAccountInfo()

    fun signInGoogle(accountInfo: AccountInfo) {
        viewModelScope.launch {
            accountUseCase.signInGoogle(accountInfo)
        }
    }
    fun signOutGoogle() {
        viewModelScope.launch {
            accountUseCase.signOutGoogle()
        }
    }

}