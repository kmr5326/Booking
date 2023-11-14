package com.ssafy.booking.ui.history

import android.net.Uri
import android.provider.OpenableColumns
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ssafy.booking.R
import com.ssafy.booking.di.App
import com.ssafy.booking.viewmodel.UploaderViewModel
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

@Composable
fun FileUploader(
    meetingInfoId: String?
) {
    val context = LocalContext.current
    val uploaderViewModel: UploaderViewModel = hiltViewModel()
    var recordUri by remember { mutableStateOf<RequestBody?>(null) }
    var downloadUri by remember { mutableStateOf<Uri?>(null) }
    var fileName by remember { mutableStateOf<String>("") }
    val loginId = App.prefs.getLoginId()

    val responseState by uploaderViewModel.naverCloudGetResponse.observeAsState()
    responseState?.let { response ->
        if (response.isSuccessful) {
            response.body()?.string()?.let {
                Log.d("STT", it)
            }
        } else {
            // 실패 처리
            response.errorBody()?.string()?.let {
                Log.d("STT", it)
            }
        }
    }

    LaunchedEffect(Unit) {
        uploaderViewModel.GetToNaverCloud(meetingInfoId)
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            downloadUri = uri

            val contentResolver = context.contentResolver

            // 실제 파일 이름을 가져오기
            val cursor = contentResolver.query(uri, null, null, null, null)
            val nameIndex = cursor?.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            fileName = if (cursor != null && nameIndex != null && cursor.moveToFirst()) {
                cursor.getString(nameIndex)
            } else {
                "Unknown"
            }
            cursor?.close()

            val inputStream = contentResolver.openInputStream(uri)
            recordUri = inputStream?.readBytes()?.toRequestBody("audio/m4a".toMediaTypeOrNull())
        }
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            IconButton(
                onClick = {
                    launcher.launch("audio/*")
                },
                modifier = Modifier
                    .size(100.dp),
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_upload_file_24),
                    contentDescription = "녹음 파일 업로드",
                    tint = Color(0xFF00C68E)
                )
            }
            downloadUri?.let { uri ->
                Text(
                    text = "${fileName}",
                    modifier = Modifier
                        .padding(16.dp)
                )
                Button(onClick = {
                    if (loginId != null) {
                        uploaderViewModel.enrollRecordFile(
                            loginId,
                            meetingInfoId.toString(),
                            recordUri
                        )
                    }
                }){
                    Text(text="POST NAVER CLOUD")
                }
            } ?: run {
                Text(
                    text = "오디오 파일을 선택해주세요",
                    modifier = Modifier
                        .padding(16.dp)
                )
            }
        }
    }
}