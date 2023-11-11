package com.ssafy.booking.ui

import BookingCreate
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.ssafy.booking.ui.book.BookDetail
import com.ssafy.booking.ui.book.BookHome
import com.ssafy.booking.ui.booking.BookingDetail
import com.ssafy.booking.ui.booking.Main
import com.ssafy.booking.ui.chat.ChatDetail
import com.ssafy.booking.ui.chat.ChatHome
import com.ssafy.booking.ui.common.SettingPage
import com.ssafy.booking.ui.history.HistoryDetail
import com.ssafy.booking.ui.history.HistoryHome
import com.ssafy.booking.ui.location.SettingAddress
import com.ssafy.booking.ui.login.Greeting
import com.ssafy.booking.ui.login.SignInScreen
import com.ssafy.booking.ui.profile.MyBookDetail
import com.ssafy.booking.ui.profile.MyBookRegister
import com.ssafy.booking.ui.profile.ProfileFollowScreen
import com.ssafy.booking.ui.profile.ProfileHome
import com.ssafy.booking.ui.profile.ProfileModifierScreen
import com.ssafy.booking.ui.theme.BookingTheme
import com.ssafy.booking.viewmodel.AppViewModel
import com.ssafy.booking.viewmodel.ChatViewModel
import com.ssafy.booking.viewmodel.MainViewModel
import com.ssafy.booking.viewmodel.SocketViewModel
import com.ssafy.domain.model.booking.BookingDetail

sealed class AppNavItem(
    val route: String
) {
    object Book : AppNavItem("book/{checkNum}")
    object BookDetail : AppNavItem("bookDetail/{isbn}")
    object History : AppNavItem("history")
    object HistoryDetail : AppNavItem("history/detail")
    object Main : AppNavItem("main")
    object Chat : AppNavItem("chat")
    object ChatDetail : AppNavItem("chatDetail/{chatId}")
    object Profile : AppNavItem("profile/{memberPk}")
    object Login : AppNavItem("login")
    object CreateBooking : AppNavItem("create/booking/{isbn}")
    object SignIn : AppNavItem("signIn/{loginId}/{kakaoNickName}") {
        fun createRoute(loginId: String, kakaoNickName: String): String {
            return "signIn/$loginId/$kakaoNickName"
        }
    }
    object Setting : AppNavItem("setting")
    object ProfileFollow : AppNavItem("profile/follow/{memberPk}")
    object ProfileModifier : AppNavItem("profile/modifier")
    object BookingDetail : AppNavItem("bookingDetail/{meetingId}")
    object MyBookRegister : AppNavItem("profile/book/{isbn}")
    object MyBookDetail : AppNavItem("profile/book/detail/{isbn}")
    object SettingAddress : AppNavItem("setting/address")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingApp(googleSignInClient: GoogleSignInClient) {
    val viewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current)
    val appViewModel = viewModel<AppViewModel>(viewModelStoreOwner)
    val socketViewModel = hiltViewModel<SocketViewModel>(viewModelStoreOwner)

    BookingTheme {
        Scaffold {
            Box(
                modifier = Modifier
                    .padding(it)
            ) {
                Route(googleSignInClient)
            }
        }
    }
}

val LocalNavigation = staticCompositionLocalOf<NavHostController> { error("Not provided") }

@Composable
fun Route(googleSignInClient: GoogleSignInClient) {
    val navController = rememberNavController()
    val viewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current)
    val appViewModel = viewModel<AppViewModel>(viewModelStoreOwner)
    val mainViewModel = hiltViewModel<MainViewModel>(viewModelStoreOwner)
    val socketViewModel = hiltViewModel<SocketViewModel>(viewModelStoreOwner)
    val chatViewModel = hiltViewModel<ChatViewModel>(viewModelStoreOwner)
//    val histroyViewModel = hiltViewModel<HistoryViewModel>(viewModelStoreOwner)

    CompositionLocalProvider(
        LocalNavigation provides navController
    ) {
        NavHost(navController = navController, startDestination = AppNavItem.Login.route) {
            composable("login") {
                val context = LocalContext.current
                Greeting(navController, mainViewModel, appViewModel, context, googleSignInClient)
            }
            composable("book/{checkNum}") { navBackStackEntry ->
                val checkNum = navBackStackEntry.arguments?.getString("checkNum") ?: "0"
                Log.d("test1234","$navBackStackEntry")
                Log.d("test1234","${navBackStackEntry.arguments}")
                Log.d("test123","$checkNum")
                BookHome(navController, appViewModel, checkNum)
            }
            composable("bookDetail/{isbn}") { navBackStackEntry ->
                val isbn = navBackStackEntry.arguments?.getString("isbn")
                isbn?.let {
                    BookDetail(isbn = it)
                }
            }
            composable("history") {
                HistoryHome(navController, appViewModel)
            }
            composable("history/detail") {
                HistoryDetail(navController, appViewModel)
            }
            composable("main") {
                Main(navController, appViewModel)
            }
            composable("chat") {
                ChatHome(navController, appViewModel)
            }
            composable("chatDetail/{chatId}") {
                ChatDetail(navController, socketViewModel)
            }
            composable("profile/{memberPk}") {navBackStackEntry->
                val memberPk = navBackStackEntry.arguments?.getString("memberPk")?.toLong() ?: 0
                ProfileHome(navController, appViewModel, memberPk)
            }
            composable("create/booking/{isbn}") { navBackStackEntry ->
                val isbn = navBackStackEntry.arguments?.getString("isbn")
                if (isbn == "isbn") {
                    BookingCreate(navController, appViewModel, isbn = null)
                } else {
                    BookingCreate(navController, appViewModel, isbn)
                }
            }
            composable("signIn/{loginId}/{kakaoNickName}") { navBackStackEntry ->
                // 여기에서 loginId와 nickName을 추출합니다.
                val loginId = navBackStackEntry.arguments?.getString("loginId") ?: ""
                val kakaoNickName = navBackStackEntry.arguments?.getString("kakaoNickName") ?: ""
                SignInScreen(loginId, kakaoNickName)
            }
            composable("setting") {
                SettingPage()
            }
            composable("profile/follow/{memberPk}") { navBackStackEntry ->
                val memberPk = navBackStackEntry.arguments?.getString("memberPk")?.toLong() ?: 0
                ProfileFollowScreen(memberPk)
            }
            composable("profile/modifier") {
                ProfileModifierScreen()
            }
            // Navigation 컴포즈에서 인자를 전달할 때 모든 인자는 'String' 형식으로 전달됨.
            composable("bookingDetail/{meetingId}") { navBackStackEntry ->
                // String으로 받은 meetingId를 Long으로 변환
                val meetingIdString = navBackStackEntry.arguments?.getString("meetingId") ?: "0"
                val meetingId = meetingIdString.toLongOrNull() ?: 0L // 만약 변환이 실패하면 0L로 대체
                BookingDetail(meetingId = meetingId) // 이제 meetingId는 Long 타입
            }
            composable("profile/book/{isbn}") {navBackStackEntry->
                val isbn = navBackStackEntry.arguments?.getString("isbn") ?: ""
                Log.d("test","$isbn")
                MyBookRegister(isbn)
            }
            composable("profile/book/detail/{isbn}") {navBackStackEntry->
                val isbn = navBackStackEntry.arguments!!.getString("isbn")
                MyBookDetail(isbn)
            }
            composable("setting/address") {
                SettingAddress(navController, appViewModel)
            }
        }
    }
}
