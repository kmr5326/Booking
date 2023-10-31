package com.ssafy.booking.ui.login
import android.app.Application
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.common.api.ApiException
import com.ssafy.booking.R
import com.ssafy.booking.google.GoogleApiContract
import com.ssafy.booking.google.GoogleUserModel
import com.ssafy.booking.google.SignInGoogleViewModel
import com.ssafy.booking.google.SignInGoogleViewModelFactory
import com.ssafy.booking.ui.AppNavItem
import com.ssafy.booking.viewmodel.AppViewModel
import com.ssafy.booking.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthScreen(
    navController: NavController,
) {
    val signInRequestCode = 1
    val context = LocalContext.current

    val mSignInViewModel: SignInGoogleViewModel = viewModel(
        factory = SignInGoogleViewModelFactory(context.applicationContext as Application)
    )

    val state = mSignInViewModel.googleUser.observeAsState()
    state.value?.let {
        Log.d("DEBUG", "User email: ${it.email}, User name: ${it.name}")
    } ?: run {
        Log.d("DEBUG", "User is null")
    }
    val user = state.value

    val isError = rememberSaveable { mutableStateOf(false) }

    val authResultLauncher =
        rememberLauncherForActivityResult(contract = GoogleApiContract()) { task ->
            try {
                val gsa = task?.getResult(ApiException::class.java)

                gsa?.let {
                    Log.d("DEBUG", "Google account email: ${it.email}, Display name: ${it.displayName}")
                } ?: run {
                    Log.d("DEBUG", "Google account is null")
                }

                if (gsa != null) {
                    mSignInViewModel.fetchSignInUser(gsa.email, gsa.displayName)
                } else {
                    isError.value = true
                }
            } catch (e: ApiException) {
                Log.d("Error in AuthScreen%s", e.toString())
            }
        }

    AuthView(
        onClick = { authResultLauncher.launch(signInRequestCode) },
        isError = isError.value,
        mSignInViewModel
    )
    // Strange issue after upgrading to latest version
    if (mSignInViewModel.googleUser.value != null) {
        LaunchedEffect(key1 = Unit) {
//            mSignInViewModel.hideLoading()


            navController.navigate(AppNavItem.Main.route) {
                launchSingleTop = true
                popUpTo(AppNavItem.Main.route)
            }
//            navController.navigate(
//                HomeViewDestination(
//                    GoogleUserModel(
//                        email = user?.email,
//                        name = user?.name,
//                    )
//                )
//            ) {
//                popUpTo(route = AuthScreenDestination.routeId) {
//                    inclusive = true
//                }
//            }
        }
    }
}


@Composable
fun Greeting(navController: NavController,
             mainViewModel: MainViewModel,
             appViewModel: AppViewModel,
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
            AuthScreen(navController)
            TempLoginButton {}
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

//@Composable
//fun GoogleLoginButton(mainViewModel: MainViewModel) {
//    Button(
//        onClick = { mainViewModel.getUserRepo() },
//        colors = ButtonDefaults.buttonColors(
//            containerColor = Color(0xFFffffff),
//            contentColor = Color(0xFF258fff)
//        )
//    ) {
//        Text("Login With Google",
//            fontWeight = FontWeight.Bold)
//    }
//}


@ExperimentalMaterial3Api
@Composable
private fun AuthView(
    onClick: () -> Unit,
    isError: Boolean = false,
    mSignInViewModel: SignInGoogleViewModel
) {
    val state = mSignInViewModel.loading.observeAsState()
    val isLoading = state.value

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Button(onClick = {
                mSignInViewModel.showLoading()
                onClick()
            }) {
                Text(text = "Login with Google")
            }
            Text(text = "${mSignInViewModel.googleUser.value}")

            when {
                isError -> {
                    isError.let {
                        Text(
                            "에러",
                        )
                        mSignInViewModel.hideLoading()
                    }
                }
            }
        }
    }




@Composable
fun TempLoginButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF00C68E),
            contentColor = Color(0xFFffffff)
        )


    ) {
        Text("임시 로그인 버튼",
            fontWeight = FontWeight.Bold)
    }
}

