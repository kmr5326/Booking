package com.ssafy.booking.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ssafy.booking.ui.book.BookHome
import com.ssafy.booking.ui.chat.ChatHome
import com.ssafy.booking.ui.history.HistoryHome
import com.ssafy.booking.ui.theme.BookingTheme
import com.ssafy.booking.viewmodel.AppViewModel
import com.ssafy.booking.ui.main.Main
import com.ssafy.booking.ui.profile.ProfileHome

sealed class AppNavItem(
    val route: String
) {
    object Book: AppNavItem("book")
    object History: AppNavItem("history")
    object Main: AppNavItem("main")
    object Chat: AppNavItem("chat")
    object Profile: AppNavItem("profile")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingApp() {
    val viewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current)
    val appViewModel = viewModel<AppViewModel>(viewModelStoreOwner)

    BookingTheme {
        Scaffold {
            Box(Modifier.padding(it)) {
                AppBar()
            }
        }
    }
}

@Composable
fun AppBar() {
    val navController = rememberNavController()
    val appViewModel = viewModel<AppViewModel>()

    NavHost(navController = navController, startDestination = AppNavItem.Main.route) {
        composable("book") {
            BookHome(navController, appViewModel)
        }
        composable("history") {
            HistoryHome(navController, appViewModel)
        }
        composable("main") {
            Main(navController, appViewModel)
        }
        composable("chat") {
            ChatHome(navController, appViewModel)
        }
        composable("profile") {
            ProfileHome(navController, appViewModel)
        }
    }
}
