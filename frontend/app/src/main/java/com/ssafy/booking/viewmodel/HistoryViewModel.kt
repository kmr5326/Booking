package com.ssafy.booking.viewmodel
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.domain.model.RecordFileNameRequest
import com.ssafy.domain.model.history.SttResponseDto
import com.ssafy.domain.usecase.HistoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
 class HistoryViewModel @Inject constructor(
     private val historyUseCase: HistoryUseCase
 ) : ViewModel() {

    var errorMessage = mutableStateOf("")
    private val _SpeakToTextInfo = MutableLiveData<SttResponseDto>()
    val SpeakToTextInfo: LiveData<SttResponseDto> = _SpeakToTextInfo

    fun loadTransaction(meetingInfoId : Long) {
//        val recordFileName = "${meetingInfoId}_recording.m4a"

            viewModelScope.launch {
            try {
                val transaction = historyUseCase.getSpeakToText(meetingInfoId)
                _SpeakToTextInfo.value = transaction
                Log.d("STT_TEST", "1HVM STTINFO $SpeakToTextInfo")
                Log.d("STT_TEST", "2HVM STTINFO ${SpeakToTextInfo.value}")
                Log.d("STT_TEST", "3HVM STTINFO ${_SpeakToTextInfo.value}")
            } catch (e: HttpException) {
                errorMessage.value = "HVM STTINFO 네트워크 에러: ${e.code()} ${e.message}"
                Log.d("STT_TEST", "$errorMessage.value")
            } catch (e: IOException) {
                errorMessage.value = "HVM STTINFO 네트워크 연결을 확인해 주세요."
                Log.d("STT_TEST", "$errorMessage.value")
            } catch (e: Exception) {
                errorMessage.value = "HVM STTINFO 알 수 없는 에러 발생: ${e.message}"
                Log.d("STT_TEST", "$errorMessage.value")
            }
        }
    }

 }
