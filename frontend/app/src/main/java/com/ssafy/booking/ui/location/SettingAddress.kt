package com.ssafy.booking.ui.location

import android.Manifest
import android.content.pm.PackageManager
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.core.app.ActivityCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.ssafy.booking.R
import com.ssafy.booking.di.App
import com.ssafy.booking.viewmodel.AppViewModel
import com.ssafy.booking.viewmodel.BookingViewModel
import com.ssafy.booking.viewmodel.LocationViewModel
import com.ssafy.booking.viewmodel.MyPageViewModel
import com.ssafy.data.repository.token.TokenDataSource
import com.ssafy.domain.model.SignInRequest

// 최상단 컴포저블
@Composable
fun SettingAddress(
    navController: NavController,
    appViewModel: AppViewModel
)
{
    Column {
        Text(text = "내 위치 설정하기")
//        SearchInput()
        ReadLocation()
    }
    // 제목
}

// 현재 위치로 설정
@Composable
fun ReadLocation() {
    val locationViewModel: LocationViewModel = hiltViewModel()
    val isLoading = locationViewModel.isLoading.value
//    val addressData = locationViewModel.getAddressResponse.value
    val addressData by locationViewModel.getAddressResponse.observeAsState()
    val errorMessage = locationViewModel.errorMessage.value

    LaunchedEffect(Unit) {
        val lat = App.prefs.getLat().toString()
        val lgt = App.prefs.getLgt().toString()
        locationViewModel.getAddress(lat, lgt)
        Log.d("위치",addressData.toString())
    }
    Row() {
        if (isLoading) {
            Text(text = "주소 정보를 불러오는 중")
        } else {
            if (addressData != null) {
//                Text(text = addressData.body()?.documents?.get(0).toString())
                val addressName = addressData!!.body()?.documents?.firstOrNull()?.address?.addressName
//                val region1 = addressData.body()?.documents?.firstOrNull()?.address?.region2DepthName
//                val region2 = addressData.body()?.documents?.firstOrNull()?.address?.region3DepthName
                Text(text = addressName ?: "주소 정보가 없습니다.")

            } else {
                Text(text = "주소가 없습니다.")
            }
        }
        if (errorMessage != null) {
            Text(text = errorMessage)
        }

    }
    SetCurrentLocation()
}

@Composable
fun SetCurrentLocation() {
    val context = LocalContext.current
    var permissionsGranted by remember { mutableStateOf(false) }
    val requestPermissionsLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        permissionsGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true
    }

    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    val myPageViewModel : MyPageViewModel = hiltViewModel()
    val (myLocation, setMyLocation) = remember { mutableStateOf("0") }

    // 주기적인 위치 업데이트를 위한 LocationRequest 객체 생성
    val locationRequest = LocationRequest.create().apply {
        interval = 10000 // 10초마다 위치 업데이트
        fastestInterval = 5000 // 가장 빠른 간격은 5초
        priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }

    // 위치 업데이트 콜백 정의
    val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            locationResult ?: return
            for (location in locationResult.locations) {
                // 위치 업데이트 시마다 로그 출력
                Log.i("LocationUpdate", location.toString())
                setMyLocation(location.toString())
            }
        }
    }

    Log.d("lastLocation", "??? : $permissionsGranted")
    // 2. Launch the permissions request when needed
    if (permissionsGranted) {
        LaunchedEffect(Unit) {
            Log.d("lastLocation", "????????")
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                fusedLocationClient.requestLocationUpdates(
                    locationRequest,
                    locationCallback,
                    Looper.getMainLooper()
                )

                Handler(Looper.getMainLooper()).postDelayed({
                    fusedLocationClient.removeLocationUpdates(locationCallback)
                }, 10000) // 10초 후에 위치 업데이트 중지
            }
        }

    }
    Text(text=myLocation)
    LaunchedEffect(myLocation) {
        Log.d("위치",myLocation)
    }

    Button(
        onClick = {
            requestPermissionsLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
//            myPageViewModel.patchUserAddress(myLocation)

                  },
        colors = ButtonDefaults.buttonColors(
            containerColor = colorResource(id = R.color.booking_1),
            contentColor = colorResource(id = R.color.font_color)
        )
    ) {
        Text(text = "위치 인증 체크")
    }

    Button(
        onClick = {
            Log.d("위치",myLocation)
        },
        ) {
        Text(text="내 위치 수정하기")
}

}
//
//Row(
//modifier = Modifier
//.clickable {
//    fetchLocation()
//}
//) {
//    Icon(Icons.Outlined.LocationOn, contentDescription = null, tint = Color(0xFF12BD7E))
//    Text(text = "현재 내 위치로 설정")
//    Icon(Icons.Outlined.KeyboardArrowRight, contentDescription = null, tint = Color(0xFF12BD7E))
//}