package com.ssafy.booking.ui.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily.Companion.Serif
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.ssafy.booking.R
import com.ssafy.booking.ui.AppNavItem
import com.ssafy.booking.viewmodel.AppViewModel


import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.DateRange

import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material.icons.outlined.Send
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Send
import androidx.compose.material3.BottomAppBarDefaults.containerColor
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color

enum class NavItem(val route: String, val title: String) {
    Book("book","book"),
    History("history", "History"),
    Main("main", "Home"),
    Chat("chat", "Chat"),
    Profile("profile", "Profile")
}
@Composable
fun BottomNav(
    navController: NavController,
    appViewModel: AppViewModel
) {
    // 현재 백 스택 엔트리를 관찰합니다.
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    // 현재 라우트를 결정합니다.
    val currentRoute = navBackStackEntry?.destination?.route
    // 아이템 목록을 정의합니다.
    val items = listOf(
        AppNavItem.Book,
        AppNavItem.History,
        AppNavItem.Main,
        AppNavItem.Chat,
        AppNavItem.Profile
    )
    // 선택된 아이템과 선택되지 않은 아이템의 색상을 정의
    val selectedColor = Color(0xFF00C68E)
    val unselectedColor = Color.DarkGray
    NavigationBar(containerColor = Color.White) {

        items.forEach { item ->
            NavigationBarItem(
                icon = {
                    val icon = when (item) {
                        AppNavItem.Book -> Icons.Outlined.FavoriteBorder
                        AppNavItem.History -> Icons.Outlined.DateRange
                        AppNavItem.Main -> Icons.Outlined.Home
                        AppNavItem.Chat -> Icons.Outlined.Send
                        AppNavItem.Profile -> Icons.Outlined.AccountCircle
                        else -> {
                            Icons.Filled.Info
                        }
                    }
                    Icon(icon, contentDescription = null)
                },
                label = {
                    val label = when (item) {
                        AppNavItem.Book -> "도서"
                        AppNavItem.History -> "이전 모임"
                        AppNavItem.Main -> "홈"
                        AppNavItem.Chat -> "채팅"
                        AppNavItem.Profile -> "프로필"
                        else -> {
                            "기타"
                        }
                    }
                    Text(label)
                },
                selected = currentRoute == item.route,
                onClick = {
                    if (currentRoute != item.route) {
                        val route = when(item) {
                            AppNavItem.Book -> "book/true"
                            else -> item.route
                        }

                        navController.navigate(route) {
                            launchSingleTop = true // 현재 탑 레벨 데스티네이션을 재시작하지 않음.
                            restoreState = true // 상태를 복원합니다 (예: 스크롤 위치).
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true // 데스티네이션을 pop할 때 상태를 저장
                            }
                        }
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color.White, // 인디케이터 색상을 투명하게 설정합니다.
                    selectedIconColor = selectedColor,
                    unselectedIconColor = unselectedColor,
                    selectedTextColor = selectedColor,
                    unselectedTextColor = unselectedColor,
                )
            )
        }
    }
}


//@Composable
//fun BottomNav(
//    navController: NavController, appViewModel: AppViewModel
//) {
//    Btn(navController, appViewModel)
//}
//
//@Composable
//fun Btn(
//    navController: NavController,
//    appViewModel: AppViewModel,
//) {
//    val imgLength = 30
//    val horPadVal = 20
//    Row(
//        modifier = Modifier
//            .fillMaxWidth()
//            .height(80.dp)
//            .padding(bottom = 20.dp)
//            .background(color = colorResource(id = R.color.background_color)),
//        horizontalArrangement = Arrangement.Center,
//        verticalAlignment = Alignment.Bottom,
//    ) {
//        Box(
//            contentAlignment = Alignment.Center, modifier = Modifier.padding(end = horPadVal.dp)
//        ) {
//            val btnBook = painterResource(R.drawable.btn_book)
//            Image(painter = btnBook, contentDescription = null, modifier = Modifier
//                .clickable(interactionSource = remember { MutableInteractionSource() },
//                    indication = null,
//                    onClick = {
//                        navController.navigate(AppNavItem.Book.route) {
//                            launchSingleTop = true
//                            popUpTo(AppNavItem.Main.route)
//                        }
//                    })
//                .width(imgLength.dp)
//                .height(imgLength.dp))
//        }
//        Box(
//            contentAlignment = Alignment.Center,
//            modifier = Modifier.padding(horizontal = horPadVal.dp)
//        ) {
//            val btnHistory = painterResource(R.drawable.btn_history)
//            Image(painter = btnHistory, contentDescription = null, modifier = Modifier
//                .clickable(interactionSource = remember { MutableInteractionSource() },
//                    indication = null,
//                    onClick = {
//                        navController.navigate(AppNavItem.History.route) {
//                            launchSingleTop = true
//                            popUpTo(AppNavItem.Main.route)
//                        }
//                    })
//                .width(imgLength.dp)
//                .height(imgLength.dp))
//        }
//        Box(
//            contentAlignment = Alignment.Center,
//            modifier = Modifier.padding(horizontal = horPadVal.dp)
//        ) {
//            val btnHome = painterResource(R.drawable.btn_home)
//            Image(painter = btnHome, contentDescription = null, modifier = Modifier
//                .clickable(interactionSource = remember { MutableInteractionSource() },
//                    indication = null,
//                    onClick = {
//                        navController.navigate(AppNavItem.Main.route) {
//                            launchSingleTop = true
//                            popUpTo(AppNavItem.Main.route)
//                        }
//                    })
//                .width(55.dp)
//                .height(55.dp))
//        }
//        Box(
//            contentAlignment = Alignment.Center,
//            modifier = Modifier.padding(horizontal = horPadVal.dp)
//        ) {
//            val btnChat = painterResource(R.drawable.btn_chat)
//            Image(painter = btnChat, contentDescription = null, modifier = Modifier
//                .clickable(interactionSource = remember { MutableInteractionSource() },
//                    indication = null,
//                    onClick = {
//                        navController.navigate(AppNavItem.Chat.route) {
//                            launchSingleTop = true
//                            popUpTo(AppNavItem.Main.route)
//                        }
//                    })
//                .width(imgLength.dp)
//                .height(imgLength.dp))
//        }
//        Box(
//            contentAlignment = Alignment.Center, modifier = Modifier.padding(start = horPadVal.dp)
//        ) {
//            val btnProfile = painterResource(R.drawable.btn_profile)
//            Image(painter = btnProfile, contentDescription = null, modifier = Modifier
//                .clickable(interactionSource = remember { MutableInteractionSource() },
//                    indication = null,
//                    onClick = {
//                        navController.navigate(AppNavItem.Profile.route) {
//                            launchSingleTop = true
//                            popUpTo(AppNavItem.Main.route)
//                        }
//                    })
//                .width(imgLength.dp)
//                .height(imgLength.dp))
//        }
//    }
//}

//
//// 홈 스크린
//@Composable
//fun HomeScreen() {
//    Box(
//        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
//    ) {
//        Text(
//            text = "Home Screen", fontFamily = Serif, fontSize = 22.sp
//        )
//    }
//}
//
//// 프로필
//@Composable
//fun ProfileScreen() {
//    Box(
//        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
//    ) {
//        Text(
//            text = "Profile Screen", fontFamily = Serif, fontSize = 22.sp
//        )
//    }
//}
//
//// 설정
//@Composable
//fun SettingScreen() {
//    Box(
//        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
//    ) {
//        Text(
//            text = "Setting Screen", fontFamily = Serif, fontSize = 22.sp
//        )
//    }
//}
