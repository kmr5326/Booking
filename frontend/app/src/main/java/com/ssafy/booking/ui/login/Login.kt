package com.ssafy.booking.ui.login

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import com.ssafy.booking.ui.AppNavItem
import com.ssafy.booking.ui.LocalNavigation
import com.ssafy.booking.utils.MyFirebaseMessagingService
import com.ssafy.booking.viewmodel.AppViewModel
import com.ssafy.booking.viewmodel.LoginViewModel
import com.ssafy.booking.viewmodel.MainViewModel
import com.ssafy.data.repository.token.TokenDataSource
import com.ssafy.domain.model.google.AccountInfo
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

val TAG1 = "KakaoLogin"

// 로그인 인터페이스
interface LoginService {
    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST("/api/members/login")
    fun login(@Body loginInfo: LoginInfo): Call<ResponseBody>
}

// 데이터 정의
data class LoginInfo(val loginId: String)

// Retrofit 인스턴스 생성
val retrofit = Retrofit.Builder()
    .baseUrl("https://k9c206.p.ssafy.io:9999") // 실제 서버 URL로 변경해야 함
    .addConverterFactory(GsonConverterFactory.create())
    .build()

val loginService = retrofit.create(LoginService::class.java)

// 로그인 API 호출


@Composable
fun Greeting(
    navController: NavController,
    mainViewModel: MainViewModel,
    appViewModel: AppViewModel,
    context: Context,
    googleSignInClient: GoogleSignInClient,
    modifier: Modifier = Modifier
) {
    val navController = navController
    val loginViewModel = LoginViewModel()

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "BOOKING",
                fontWeight = FontWeight.SemiBold,
                modifier = modifier.padding(bottom = 24.dp),
                color = Color(0xFF00C68E),
                style = TextStyle(fontSize = 40.sp)

            )

            KakaoLoginButton(context, navController, loginViewModel)
//            TempLoginButton(navController)
//            GoogleLoginButton(mainViewModel, googleSignInClient, navController, loginViewModel)
//            SignInBtn()
        }
    }
}

@Composable
fun TempLoginButton(navController: NavController) {
    Button(
        modifier = Modifier.width(200.dp).clip(RoundedCornerShape(5.dp)),
        shape = RoundedCornerShape(5.dp),
        onClick = {
            navController.navigate(AppNavItem.Main.route) {
                popUpTo("login") { inclusive = true }
            }
        },
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Gray,
            contentColor = Color(0xFFffffff)
        )
    ) {
        Text(
            "게스트로 로그인하기",
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun GoogleLoginButton(
    viewModel: MainViewModel,
    googleSignInClient: GoogleSignInClient,
    navController: NavController,
    loginViewModel: LoginViewModel
) {
    val accountInfo by viewModel.accountInfo.collectAsState()
    val firebaseAuth by lazy { FirebaseAuth.getInstance() }
    val context = LocalContext.current
    val startForResult =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val indent = result.data
                if (indent != null) {
                    val task: Task<GoogleSignInAccount> =
                        GoogleSignIn.getSignedInAccountFromIntent(indent)
                    loginViewModel.handleSignInResult(context, task, viewModel, firebaseAuth)
                }
            }
        }

    if (accountInfo != null) {
        navController.navigate(AppNavItem.Main.route) {
            popUpTo("login") { inclusive = true }
            launchSingleTop = true
        }
        Log.i("UserInfo", "정보들 : $accountInfo")
    } else {
        Button(
            onClick = { startForResult.launch(googleSignInClient.signInIntent) },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFffffff),
                contentColor = Color(0xFF258fff)
            )
        ) {
            Text(
                "Login With Google",
                fontWeight = FontWeight.Bold
            )
        }
    }
}


@Composable
fun KakaoLoginButton(
    context: Context,
    navController: NavController,
    loginViewModel: LoginViewModel
) {
    Button(
        modifier = Modifier.width(200.dp).clip(RoundedCornerShape(5.dp)),
        shape = RoundedCornerShape(5.dp),
        onClick = {
            // 카카오톡이 설치되어 있으면 카카오톡으로 로그인, 아니면 카카오계정으로 로그인
            if (UserApiClient.instance.isKakaoTalkLoginAvailable(context)) {
                // 카카오톡 로그인
                UserApiClient.instance.loginWithKakaoTalk(context) { token, error ->
                    // 로그인 실패 부분
                    if (error != null) {
                        Log.e(TAG1, "로그인 실패 $error")
                        // 사용자가 취소
                        if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                            return@loginWithKakaoTalk
                        }
                        // 다른 오류
                        else {
                            // 카카오계정으로 로그인
                            UserApiClient.instance.loginWithKakaoAccount(
                                context,
                                callback = loginViewModel.loginCallback(context, navController)
                            )
                        }
                    }
                    // 로그인 성공 부분
                    else if (token != null) {
                        Log.e(TAG1, "로그인 성공 ${token.accessToken}")
//                        navController.navigate(AppNavItem.Main.route)

                        // 사용자 정보 요청 (기본)
                        UserApiClient.instance.me { user, error ->
                            if (error != null) {
                                Log.e(TAG, "사용자 정보 요청 실패", error)
                            } else if (user != null) {
                                Log.i(
                                    "UserInfo",
                                    "사용자 정보 요청 성공" +
//                                        "\n회원번호: ${user.id}" +
//                                        "\n이메일: ${user.kakaoAccount?.email}" +
                                        "\n닉네임: ${user.kakaoAccount?.profile?.nickname}"
                                )
//                                        "\n프로필사진: ${user.kakaoAccount?.profile?.thumbnailImageUrl}")
                                loginViewModel.onLoginSuccess(context, user.id.toString(), navController, user.kakaoAccount?.profile?.nickname.toString())
                            }
                        }
                    }
                }
            } else {
                // 카카오계정으로 로그인
                UserApiClient.instance.loginWithKakaoAccount(
                    context,
                    callback = loginViewModel.loginCallback(context, navController)
                )
            }
        },
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFFFEE500),
            contentColor = Color(0xFFffffff)
        )
    ) {
        Text(
            "카카오로 로그인하기",
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
    }
}