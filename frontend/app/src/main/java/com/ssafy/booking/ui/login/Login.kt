package com.ssafy.booking.ui.login
import android.os.Bundle
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.ssafy.booking.ui.AppNavItem
import com.ssafy.booking.viewmodel.AppViewModel


@Composable
fun Greeting(navController: NavController,
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
            GoogleLoginButton {
            }
            TempLoginButton {
            }
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
fun GoogleLoginButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
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

