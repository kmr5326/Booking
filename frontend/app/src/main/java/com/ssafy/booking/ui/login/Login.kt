package com.ssafy.booking.ui.login
import android.app.Activity
import android.app.Application
import android.content.Context
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
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
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
import com.ssafy.domain.model.google.AccountInfo


@Composable
fun Greeting(navController: NavController,
             mainViewModel: MainViewModel,
             appViewModel: AppViewModel,
             googleSignInClient: GoogleSignInClient,
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
            GoogleLoginButton(mainViewModel, googleSignInClient, navController)
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

@Composable
fun GoogleLoginButton(viewModel: MainViewModel, googleSignInClient: GoogleSignInClient, navController:NavController) {

    val accountInfo by viewModel.accountInfo.collectAsState()
    val firebaseAuth by lazy { FirebaseAuth.getInstance() }
    val context = LocalContext.current
    val startForResult = rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) {
        result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            val indent = result.data
            if(indent != null) {
                val task : Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(indent)
                handleSignInResult(context, task, viewModel, firebaseAuth)
            }
        }
    }

    if(accountInfo != null) {
        navController.navigate(AppNavItem.Main.route) {
            popUpTo("login") {inclusive = true}
        }
    } else {
        Button(
            onClick = { startForResult.launch(googleSignInClient.signInIntent) },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFffffff),
                contentColor = Color(0xFF258fff)
            )
        ) {
            Text("Login With Google",
                fontWeight = FontWeight.Bold)
        }
    }
}

private fun handleSignInResult(context: Context, accountTask: Task<GoogleSignInAccount>, viewModel: MainViewModel, firebaseAuth: FirebaseAuth) {
    try {
        val account = accountTask.result ?: return
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener(context as Activity) {task ->
                if(task.isSuccessful) {
                    viewModel.signInGoogle(AccountInfo(account.idToken.orEmpty(), account.displayName.orEmpty(), AccountInfo.Type.GOOGLE))
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

