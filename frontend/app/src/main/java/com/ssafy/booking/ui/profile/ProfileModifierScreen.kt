package com.ssafy.booking.ui.profile

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.ssafy.booking.R
import com.ssafy.booking.model.UserInfoChangeResult
import com.ssafy.booking.ui.LocalNavigation
import com.ssafy.booking.ui.common.BackTopBar
import com.ssafy.booking.viewmodel.MyPageViewModel
import com.ssafy.data.repository.token.TokenDataSource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileModifierScreen() {
    val navController = LocalNavigation.current

    // 닉네임, 프로필이미지 가져오기
    val context = LocalContext.current
    val tokenDataSource = TokenDataSource(context)
    val nickname: String? = tokenDataSource.getNickName()
    val profileImg: String? = tokenDataSource.getProfileImage()
    val loginId: String? = tokenDataSource.getLoginId()
    val memberPk: Long = tokenDataSource.getMemberPk()

    var nick by remember { mutableStateOf("$nickname") }
    var pImg by remember { mutableStateOf("$profileImg") }
    var isError by remember { mutableStateOf(false) }
    var isErrorNick by remember { mutableStateOf(false) }

    var imageUri by remember { mutableStateOf<Uri?>(null) }

    LaunchedEffect(Unit) {
        Log.d("test", "$nick, $pImg, $nickname")
    }

    // 갤러리 접근
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        // 선택한 이미지의 URI를 처리
        pImg = uri.toString()
        imageUri = uri
    }

//    suspend fun uploadImageToNaverCloudOS(context: Context, imageUri: Uri): String? {
//        return withContext(Dispatchers.IO) {
//            val inputStream = context.contentResolver.openInputStream(imageUri)
//            val url = URL("https://kr.object.ncloudstorage.com/booking-bucket")
//
//            // HTTP 연결 설정
//            val connection = url.openConnection() as HttpURLConnection
//            connection.requestMethod = "PUT"
//            connection.setRequestProperty("Content-Type", "image/jpeg")
//            connection.setRequestProperty("x-ncloud-authorization", "1E11TfvJcy7OVnSSm3rV0Vph24CLUO4Tiehd5PtZ")
//
//            // 이미지 데이터를 OutputStream으로 전송
//            inputStream?.copyTo(connection.outputStream)
//
//            val responseCode = connection.responseCode
//            if (responseCode == HttpURLConnection.HTTP_OK) {
//                // 업로드된 이미지의 URL을 반환
//                "https://kr.object.ncloudstorage.com/booking-bucket/${imageUri.lastPathSegment}"
//            } else {
//                null // 업로드 실패 시 null 반환
//            }
//        }
//    }

    // 뷰모델 연결
    val myPageViewModel: MyPageViewModel = hiltViewModel()
    val patchUserInfoResponse by myPageViewModel.patchUserInfoResponse.observeAsState()

//    fun userInfoChange(nick : String, pImg : String, loginId: String) {
//        // 바뀐 정보로 patchUserInfo 요청 날리기
//        val requestInfo = UserModifyRequest(
//            loginId = loginId,
//            nickname = nick,
//            profileImage = pImg
//        )
//        myPageViewModel.patchUserinfo(requestInfo)
//        // patchUserInfoResponse 검사하고 isSuccessful 일때만 진행 아니면 중복임을 알려주기
//        patchUserInfoResponse?.let {response->
//            if (response.isSuccessful) {
//                tokenDataSource.putNickName(nick)
//                tokenDataSource.putProfileImage(pImg)
//                navController.navigate("profile")
//            } else {
//                isErrorNick = true
//            }
//        }
//    }
    // UI 컴포넌트 (예: Fragment) 내부
    myPageViewModel.userInfoChangeResult.collectAsState().value?.let { result ->
        when (result) {
            is UserInfoChangeResult.Success -> {
                // 성공 상태 처리
                tokenDataSource.putNickName(result.nick)
                tokenDataSource.putProfileImage(result.pImg)
                navController.navigate(result.destination)
            }
            is UserInfoChangeResult.Error -> {
                // 실패 상태 처리
                if (result.isErrorNick) {
                    // 중복 닉네임 에러 처리
                }
            }
        }
    }

    Scaffold(
        topBar = { BackTopBar("회원 정보 수정") },
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            )

            // 프로필 이미지 변경
            if (pImg != "") {
                AsyncImage(
                    model = "$pImg",
                    contentDescription = "프로필 이미지 변경",
                    contentScale = ContentScale.Crop,
                    placeholder = ColorPainter(Color.LightGray),
                    modifier = Modifier
                        .size(150.dp)
                        .clip(CircleShape)
                        .border(1.dp, Color.Gray, RoundedCornerShape(20.dp))
                        .clickable { launcher.launch("image/*") }
                )
            } else {
                AsyncImage(
                    model = R.drawable.basic_profile,
                    contentDescription = "프로필 이미지 변경",
                    contentScale = ContentScale.Crop,
                    placeholder = ColorPainter(Color.LightGray),
                    modifier = Modifier
                        .size(180.dp)
                        .clip(CircleShape)
                        .border(1.dp, Color.Gray, RoundedCornerShape(20.dp))
                        .clickable { launcher.launch("image/*") }
                )
            }

            Spacer(modifier = Modifier.padding(24.dp))

            // 닉네임 변경
            if (isErrorNick) {
                Text("중복된 닉네임 입니다.", color = Color.Red)
            }
            OutlinedTextField(
                value = nick,
                onValueChange = {
                    nick = it
                    isError = it.isEmpty()
                    isErrorNick = false
                },
                label = { Text("*닉네임") },
                singleLine = true,
                isError = isError
            )
//            Text("$nick")

            Spacer(modifier = Modifier.padding(24.dp))

            // 회원 정보 수정 완료 버튼
            Button(
                modifier = Modifier.width(280.dp),
                // 정보 수정 api 요청
                // 정보 수정 후 sharedPreference 정보 변경
                // 이후 profile 화면으로 이동 시키기
                onClick = { myPageViewModel.userInfoChange(nick, pImg, loginId!!, memberPk) },
                enabled = !isError
            ) {
                Text(text = "회원 정보 수정")
            }
        }
    }
}
