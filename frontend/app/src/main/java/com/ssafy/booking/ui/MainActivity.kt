package com.ssafy.booking.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import coil.Coil
import coil.ImageLoader
import coil.imageLoader
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.location.LocationServices
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.util.Utility
import com.ssafy.booking.di.NetworkModule_OkHttpClientWithObjectStorageFactory.okHttpClientWithObjectStorage
import com.ssafy.booking.di.NetworkModule_ProvideObjectStorageInterceptorFactory.provideObjectStorageInterceptor
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

val TAG1 = "getHashKey"

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                // Precise location access granted.
            }
            permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                // Only approximate location access granted.
            } else -> {
                // No location access granted.
            }
        }
    }

    @Inject
    lateinit var googleSignInClient: GoogleSignInClient

    @Inject lateinit var imageLoader: ImageLoader

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        WindowCompat.setDecorFitsSystemWindows(window, false)

        /** KakaoSDK init */
        KakaoSdk.init(this, "c983af9ff87c243a4acecc793d087699")
        var keyHash = Utility.getKeyHash(this)
        Log.v(TAG1, keyHash)
        setContent {
            BookingApp(googleSignInClient)
        }

        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
        ) {
            locationPermissionRequest.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
        fusedLocationClient.lastLocation.addOnSuccessListener { }

        // Coil에 주입된 ImageLoader 설정
        Coil.setImageLoader(imageLoader)
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            // FCM SDK (and your app) can post notifications.
        } else {
            // TODO: Inform user that that your app will not show notifications.
        }
    }
    private fun askNotificationPermission() {
        try {
            // This is only necessary for API level >= 33 (TIRAMISU)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.POST_NOTIFICATIONS
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    // FCM SDK (and your app) can post notifications.
                } else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                    // TODO: Display an educational UI to the user
                } else {
                    // Directly ask for the permission
                    requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            }
        } catch (e: Exception) {
            // Handle the exception
            e.printStackTrace()
            // TODO: Inform the user about the error, possibly retry or log
        }
    }
}
