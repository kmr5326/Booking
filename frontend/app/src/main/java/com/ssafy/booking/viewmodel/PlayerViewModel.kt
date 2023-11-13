package com.ssafy.booking.viewmodel

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

// 플레이어 상태 관리
@HiltViewModel
class PlayerViewModel @Inject constructor(

) : ViewModel() {
    private val mediaPlayer = MediaPlayer()

    private val _sliderPosition = MutableLiveData(0)
    val sliderPosition: LiveData<Int> = _sliderPosition
    private val _totalDuration = MutableLiveData(0)
    val totalDuration: LiveData<Int> = _totalDuration

    init {
        // MediaPlayer 설정
        mediaPlayer.setOnPreparedListener {
            setTotalDuration(mediaPlayer)
        }
        mediaPlayer.setOnCompletionListener {
            // 재생 완료 시 처리
        }
    }

    fun playAudio(context: Context, audioFile: Uri) {
        mediaPlayer.reset()
        mediaPlayer.setDataSource(context, audioFile)
        mediaPlayer.prepare()
        mediaPlayer.start()
        startUpdatingPlaybackPosition()
    }

    private fun startUpdatingPlaybackPosition() {
        val handler = android.os.Handler(Looper.getMainLooper())
        val updateTask = object : Runnable {
            override fun run() {
                _sliderPosition.value = mediaPlayer.currentPosition
                handler.postDelayed(this, 1000)
            }
        }
        handler.postDelayed(updateTask, 1000)
    }
    fun updateSliderPosition(newPosition: Int) {
        _sliderPosition.value = newPosition
        mediaPlayer.seekTo(newPosition)
    }

    fun convertMillisToTimeFormat(millis: Int): String {
        val totalSeconds = millis / 1000
        val minutes = totalSeconds / 60
        val seconds = totalSeconds % 60
        return String.format("%02d:%02d", minutes, seconds)
    }

    fun setTotalDuration(mediaPlayer: MediaPlayer) {
        val durationMillis = mediaPlayer.duration
        _totalDuration.value = durationMillis
    }


    override fun onCleared() {
        super.onCleared()
        _sliderPosition.value = 0
        _totalDuration.value = 0
    }
}