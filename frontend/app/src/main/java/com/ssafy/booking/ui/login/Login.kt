package com.ssafy.booking.ui.login

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.ContentValues.TAG
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.ssafy.booking.ui.theme.BookingTheme
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.graphics.BlendMode.Companion.Screen
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.KakaoSdk.keyHash
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.squareup.moshi.Moshi
import com.ssafy.booking.R
import com.ssafy.booking.google.GoogleApiContract
import com.ssafy.booking.google.GoogleUserModel
import com.ssafy.booking.google.SignInGoogleViewModel
import com.ssafy.booking.google.SignInGoogleViewModelFactory
import com.ssafy.booking.ui.AppNavItem
import com.ssafy.booking.viewmodel.AppViewModel
import com.ssafy.booking.viewmodel.MainViewModel
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.ssafy.data.repository.google.PreferenceDataSource
import retrofit2.http.GET

import retrofit2.Callback
import retrofit2.Response

import com.kakao.sdk.common.util.Utility
import com.ssafy.booking.utils.Utils.BASE_URL
import com.google.android.gms.tasks.Task
import com.ssafy.booking.ui.LocalNavigation
import com.ssafy.data.repository.token.TokenDataSource

import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import com.ssafy.domain.model.google.AccountInfo
import dagger.hilt.android.qualifiers.ApplicationContext

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
private fun onLoginSuccess(context: Context, loginId: String, navController: NavController,kakaoNickName:String) {
    val loginInfo = LoginInfo(loginId = loginId) // 실제 로그인 ID로 변경해야 함
//    val loginInfo = LoginInfo(loginId = "kakao_3143286573")
    val call = loginService.login(loginInfo)
    call.enqueue(object : Callback<ResponseBody> {
        override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
            if (response.isSuccessful) {
                // 성공적으로 API 호출 완료, JWT 토큰 처리
                val token = response.body()?.string()
                Log.d("API", "API 호출 성공,AT는: $token")
                val tokenDataSource = TokenDataSource(context)
                // 토큰 넣기
                tokenDataSource.putToken(token)
                // 로그인 아이디 넣기
                tokenDataSource.putLoginId(loginId)



                navController.navigate(AppNavItem.Main.route) {
                    popUpTo("login") { inclusive = true }
                    launchSingleTop = true
                }
            } else {
                // 오류 처리
                val errorCode = response.code()
                Log.d("kakao","$errorCode")
                when (errorCode) {
                    // 에러 코드가 400이면 회원가입이 필요한 상태 -> 회원가입으로 라우트
                    400 -> {
                        navController.navigate(AppNavItem.SignIn.createRoute(loginId, kakaoNickName)) {
                            popUpTo("SignIn") { inclusive = false }
                        }
                    }
                }
            }
        }

        override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
            // 네트워크 오류 등의 이유로 호출 실패
            Log.d(TAG, "API 호출 실패2: ${t.message}")
        }
    })
}


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
    Box(
        modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center
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
                style = TextStyle(fontSize = 40.sp),
            )

            KakaoLoginButton(context, navController)
            TempLoginButton(navController)
//            GoogleLoginButton(mainViewModel, googleSignInClient, navController)
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
    navController: NavController
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
                    handleSignInResult(context, task, viewModel, firebaseAuth)
                }
            }
        }

    if (accountInfo != null) {
        navController.navigate(AppNavItem.Main.route) {
            popUpTo("login")
            { inclusive = true }
            launchSingleTop = true
        }
        Log.i("UserInfo","정보들 : $accountInfo")
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

private fun handleSignInResult(
    context: Context,
    accountTask: Task<GoogleSignInAccount>,
    viewModel: MainViewModel,
    firebaseAuth: FirebaseAuth
) {
    try {
        val account = accountTask.result ?: return
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener(context as Activity) { task ->
                if (task.isSuccessful) {
                    viewModel.signInGoogle(
                        AccountInfo(
                            account.idToken.orEmpty(),
                            account.displayName.orEmpty(),
                            AccountInfo.Type.GOOGLE
                        )
                    )
                } else {
                    viewModel.signOutGoogle()
                    firebaseAuth.signOut()
                }
            }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

@Composable
fun TestButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF00C68E),
            contentColor = Color(0xFFffffff)
        )


    ) {
        Text(
            "테스트 버튼",
            fontWeight = FontWeight.Bold
        )
    }
}

///////

private fun loginCallback(
    context: Context,
    navController: NavController
): (OAuthToken?, Throwable?) -> Unit = { token, error ->
    if (error != null) {
        Log.e(TAG1, "로그인 실패 $error")
    } else if (token != null) {
        Log.e(TAG1, "로그인 성공 ${token.accessToken}")
        // 사용자 정보 요청 (기본)
        UserApiClient.instance.me { user, error ->
            if (error != null) {
                Log.e("asdf", "사용자 정보 요청 실패", error)
            } else if (user != null) {
                val loginId = "kakao_" + user.id.toString()
                onLoginSuccess(context, loginId, navController,user.kakaoAccount?.profile?.nickname.toString())
                Log.i(
                    "loginInfo", "사용자 정보 요청 성공" +
                            "\n회원번호: ${user.id}" +
                            "\n이메일: ${user.kakaoAccount?.email}" +
                            "\n닉네임: ${user.kakaoAccount?.profile?.nickname}" +
                            "\n프로필사진: ${user.kakaoAccount?.profile?.thumbnailImageUrl}"
                )
            }
        }
    }
}


@Composable
fun KakaoLoginButton(context: Context, navController: NavController) {
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
                                callback = loginCallback(context, navController)
                            )
                        }
                    }
                    // 로그인 성공 부분
                    else if (token != null) {
                        Log.e(TAG1, "로그인 성공 ${token.accessToken}")
                        navController.navigate(AppNavItem.Main.route)

                        // 사용자 정보 요청 (기본)
                        UserApiClient.instance.me { user, error ->
                            if (error != null) {
                                Log.e(TAG, "사용자 정보 요청 실패", error)
                            }
                            else if (user != null) {
                                Log.i("UserInfo", "사용자 정보 요청 성공" +
//                                        "\n회원번호: ${user.id}" +
//                                        "\n이메일: ${user.kakaoAccount?.email}" +
                                            "\n닉네임: ${user.kakaoAccount?.profile?.nickname}"
                                )
//                                        "\n프로필사진: ${user.kakaoAccount?.profile?.thumbnailImageUrl}")
                                onLoginSuccess(context, user.id.toString(), navController,user.kakaoAccount?.profile?.nickname.toString())

                            }
                        }

                    }
                }
            } else {
                // 카카오계정으로 로그인
                UserApiClient.instance.loginWithKakaoAccount(
                    context,
                    callback = loginCallback(context, navController)
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

@Composable
fun SignInBtn() {
    val navController = LocalNavigation.current

    Button(onClick = {navController.navigate(AppNavItem.SignIn.route)}) {
        Text(text = "회원가입페이지 연결")
    }
}
////

