package com.ssafy.booking.ui

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.util.Utility
import com.ssafy.booking.R
import com.ssafy.booking.ui.BookingApp
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

val TAG1 = "getHashKey"
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /** KakaoSDK init */
        KakaoSdk.init(this, "c983af9ff87c243a4acecc793d087699")
//        KakaoSdk.init(this, "3e6cef2682dd6c48c4d563071d71388c")

        var keyHash = Utility.getKeyHash(this)
        Log.v(TAG1, keyHash)
        setContent {
            BookingApp(googleSignInClient)
        }
    }
}


