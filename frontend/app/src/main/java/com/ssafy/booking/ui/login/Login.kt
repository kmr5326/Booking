package com.ssafy.booking.ui.login
import android.content.ContentValues.TAG
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.ui.graphics.BlendMode.Companion.Screen
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.KakaoSdk.keyHash
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import com.ssafy.booking.ui.AppNavItem
import com.ssafy.booking.viewmodel.AppViewModel
import com.ssafy.booking.viewmodel.MainViewModel
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
//
//import retrofit2.Retrofit
//import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
//import kotlin.coroutines.jvm.internal.CompletedContinuation.context

//import retrofit2.http.GET
//import okhttp3.ResponseBody
import com.kakao.sdk.common.util.Utility





val TAG1 ="KakaoLogin"
@Composable
fun Greeting(navController: NavController,
             mainViewModel: MainViewModel,
             appViewModel: AppViewModel,
             context: Context,
             modifier: Modifier = Modifier)
{
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
                modifier = modifier.padding(bottom=24.dp),
                color = Color(0xFFffffff),
                style = TextStyle(fontSize = 40.sp),
            )
            KakaoLoginButton(navController)
            GoogleLoginButton(mainViewModel)
            TempLoginButton {
            }
            NewButton(context,navController)
        }
    }
}

@Composable
fun KakaoLoginButton(navController: NavController) {
    Button(
        onClick = {
            navController.navigate(AppNavItem.Main.route) {
                popUpTo("login") {inclusive = true}
            }
        },
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFFfae100),
            contentColor = Color(0xFFffffff)
        )
    ) {
        Text("카카오로 로그인하기",
            fontWeight = FontWeight.Bold)
    }
}

@Composable
fun GoogleLoginButton(mainViewModel: MainViewModel) {
    Button(
        onClick = { mainViewModel.getUserRepo() },
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFFffffff),
            contentColor = Color(0xFF258fff)
        )
    ) {
        Text("Login With Google",
            fontWeight = FontWeight.Bold)
    }
}
@Composable
fun TempLoginButton(onClick: () -> Unit) {
    Button(
        onClick = {
            Log.d("재주","keyHash: $keyHash") },
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF00C68E),
            contentColor = Color(0xFFffffff)
        )


    ) {
        Text("임시 로그인 버튼",
            fontWeight = FontWeight.Bold)
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
        Text("테스트 버튼",
            fontWeight = FontWeight.Bold)
    }
}

///////


private val mCallback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
    if (error != null) {
        Log.e(TAG1, "로그인 실패 $error")
    } else if (token != null) {
        Log.e(TAG1, "로그인 성공 ${token.accessToken}")
    }
}


@Composable
fun NewButton(context: Context,navController: NavController) {
    Button(
        onClick = {
            // 카카오톡이 설치되어 있으면 카카오톡으로 로그인, 아니면 카카오계정으로 로그인
            if (UserApiClient.instance.isKakaoTalkLoginAvailable(context)) {
                // 카카오톡 로그인
                UserApiClient.instance.loginWithKakaoTalk(context) { token, error ->
                    // 로그인 실패 부분
                    if (error != null) {
                        Log.e(TAG1, "로그인 실패 $error")
                        // 사용자가 취소
                        if (error is ClientError && error.reason == ClientErrorCause.Cancelled ) {
                            return@loginWithKakaoTalk
                        }
                        // 다른 오류
                        else {
                            UserApiClient.instance.loginWithKakaoAccount(context, callback = mCallback) // 카카오 이메일 로그인
                        }
                    }
                    // 로그인 성공 부분
                    else if (token != null) {
                        Log.e(TAG1, "로그인 성공 ${token.accessToken}")
                        navController.navigate(AppNavItem.Main.route)

                    }
                }
            } else {
                UserApiClient.instance.loginWithKakaoAccount(context, callback = mCallback) // 카카오 이메일 로그인
            }
        },
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF00C68E),
            contentColor = Color(0xFFffffff)
        )
    ) {
        Text("새로운 로그인 버튼",
            fontWeight = FontWeight.Bold)
    }
}

////

