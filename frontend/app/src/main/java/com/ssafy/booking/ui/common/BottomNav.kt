package com.ssafy.booking.ui.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ssafy.booking.R
import com.ssafy.booking.ui.AppNavItem
import com.ssafy.booking.viewmodel.AppViewModel
@Composable
fun BottomNav (
    navController: NavController,
    appViewModel: AppViewModel
) {
    Btn(navController, appViewModel)
}

@Composable
fun Btn(
    navController: NavController,
    appViewModel: AppViewModel,
){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(bottom = 20.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.Bottom
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.padding(horizontal = 10.dp)
        ) {
            val btnBook = painterResource(R.drawable.btn_book)
            Image(painter = btnBook, contentDescription = null,
                modifier = Modifier
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = {
                            navController.navigate(AppNavItem.Book.route) {
                                launchSingleTop = true
                                popUpTo(AppNavItem.Main.route)
                            }
                        }
                    )
                    .width(50.dp)
                    .height(50.dp)
            )
        }
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.padding(horizontal = 10.dp)
        ) {
            val btnHistory = painterResource(R.drawable.btn_history)
            Image(painter = btnHistory, contentDescription = null,
                modifier = Modifier
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = {
                            navController.navigate(AppNavItem.History.route) {
                                launchSingleTop = true
                                popUpTo(AppNavItem.Main.route)
                            }
                        }
                    )
                    .width(50.dp)
                    .height(50.dp)
            )
        }
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.padding(horizontal = 10.dp)
        ) {
            val btnHome = painterResource(R.drawable.btn_home)
            Image(painter = btnHome, contentDescription = null,
                modifier = Modifier
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = {
                            navController.navigate(AppNavItem.Main.route) {
                                launchSingleTop = true
                                popUpTo(AppNavItem.Main.route)
                            }
                        }
                    )
                    .width(50.dp)
                    .height(50.dp)
            )
        }
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.padding(horizontal = 10.dp)
        ) {
            val btnChat = painterResource(R.drawable.btn_chat)
            Image(painter = btnChat, contentDescription = null,
                modifier = Modifier
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = {
                            navController.navigate(AppNavItem.Chat.route) {
                                launchSingleTop = true
                                popUpTo(AppNavItem.Main.route)
                            }
                        }
                    )
                    .width(50.dp)
                    .height(50.dp)
            )
        }
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.padding(horizontal = 10.dp)
        ) {
            val btnProfile = painterResource(R.drawable.btn_profile)
            Image(painter = btnProfile, contentDescription = null,
                modifier = Modifier
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = {
                            navController.navigate(AppNavItem.Profile.route) {
                                launchSingleTop = true
                                popUpTo(AppNavItem.Main.route)
                            }
                        }
                    )
                    .width(50.dp)
                    .height(50.dp)
            )
        }
    }
}