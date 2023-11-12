package com.ssafy.booking.ui.location

import android.Manifest
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowRight
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.ssafy.booking.di.App
import com.ssafy.booking.viewmodel.AppViewModel
import com.ssafy.booking.viewmodel.BookingViewModel
import com.ssafy.booking.viewmodel.LocationViewModel
import com.ssafy.data.repository.token.TokenDataSource

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
    val addressData = locationViewModel.getAddressResponse.value
    val errorMessage = locationViewModel.errorMessage.value

    LaunchedEffect(Unit) {
        val lat = App.prefs.getLat().toString()
        val lgt = App.prefs.getLgt().toString()
        locationViewModel.getAddress(lat, lgt)
    }
    Row() {
        if (isLoading) {
            Text(text = "주소 정보를 불러오는 중")
        } else {
            if (addressData != null) {
//                Text(text = addressData.body()?.documents?.get(0).toString())
                val addressName = addressData.body()?.documents?.firstOrNull()?.address?.addressName
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
}

@Composable
fun SetCurrentLocation() {
//    val context = LocalContext.current
//    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
//    val locationResult = remember { mutableStateOf<List?>(null) }
//
//    fun fetchLocation() {
//        val locationTask = fusedLocationClient.lastLocation
//        locationTask.addOnSuccessListener { location ->
//            if (location != null) {
//                locationResult.value = location
//                // 위치 데이터를 String 형태로 변환하여 저장하는 로직
//                // 예: SharedPreferences에 저장
//            }
//        }
//    }
//
//    Row(
//        modifier = Modifier
//            .clickable {
//                fetchLocation()
//            }
//    ) {
//        Icon(Icons.Outlined.LocationOn, contentDescription = null, tint = Color(0xFF12BD7E))
//        Text(text = "현재 내 위치로 설정")
//        Icon(Icons.Outlined.KeyboardArrowRight, contentDescription = null, tint = Color(0xFF12BD7E))
//    }
//
//    // 위치 정보 확인 및 사용
//    locationResult.value?.let { location ->
//        // 여기에 위치 정보를 사용하는 코드를 작성
//        // 예: 위치 정보를 String으로 변환하여 저장
//    }
}
