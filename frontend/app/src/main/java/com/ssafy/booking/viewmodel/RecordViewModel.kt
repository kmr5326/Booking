package com.ssafy.booking.viewmodel

import android.media.MediaRecorder
import android.os.Build
import android.os.Environment
import android.provider.MediaStore.Audio.Media
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

enum class RecordingState {
    STARTED, PAUSED, RESUMED, STOPPED
}

@HiltViewModel
class RecordViewModel @Inject constructor(

) : ViewModel() {
    private var mediaRecorder: MediaRecorder? = null
    private val _recordingState = MutableLiveData<RecordingState>(RecordingState.STOPPED)
    val recordingState: LiveData<RecordingState> = _recordingState
    private var recordingFilePath: String? = null

    init {
        setupRecordingFilePath()
    }

    private fun setupRecordingFilePath() {
        val downloadDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val bookingDir = File(downloadDir, "Booking")
        if (!bookingDir.exists()) {
            bookingDir.mkdirs() // "booking" 폴더가 없으면 생성
        }
        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val recordingFileName = "booking_$timestamp.m4a"
        val recordingFile = File(bookingDir, recordingFileName)
        recordingFilePath = recordingFile.absolutePath
    }

    fun startRecording() {
        mediaRecorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            setOutputFile(recordingFilePath)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                setAudioEncodingBitRate(128000)
                setAudioSamplingRate(44100)
            }
            prepare()
        }
        mediaRecorder?.start()
        _recordingState.value = RecordingState.STARTED
    }

    fun pauseRecording() {
        mediaRecorder?.pause()
        _recordingState.value = RecordingState.PAUSED
    }

    fun resumeRecording() {
        mediaRecorder?.resume()
        _recordingState.value = RecordingState.RESUMED
    }

    fun stopRecording() {
        mediaRecorder?.stop()
        mediaRecorder?.release()
        mediaRecorder = null
        _recordingState.value = RecordingState.STOPPED
        if (recordingFilePath != null && checkFileExists(recordingFilePath)) {
            // 파일이 존재하는 경우, 다운로드 가능
        } else {
            // 파일이 없는 경우, 다운로드 불가능 알림
        }
    }

    private fun checkFileExists(filePath: String?): Boolean {
        return filePath != null && File(filePath).exists()
    }

    override fun onCleared() {
        super.onCleared()
        mediaRecorder?.release()
    }
}