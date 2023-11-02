package com.ssafy.booking.ui.login

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSizeIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.ssafy.booking.ui.common.TopBar


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignInScreen(
    loginId:String = "",
    kakaoNickName:String = ""
) {
    val context = LocalContext.current
    var permissionsGranted by remember { mutableStateOf(false) }

    val requestPermissionsLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        permissionsGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true
    }

    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

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

    var email :String by remember { mutableStateOf("") }
    var nickName :String by remember { mutableStateOf(kakaoNickName) }
    var name :String by remember { mutableStateOf("") }
    var isError by remember { mutableStateOf(false) }

    val years = (2013 downTo 1900).map {it.toString()}
    val months = (1..12).map {it.toString()}
    val days = (1..31).map {it.toString()}

    val genderOptions = listOf("여성", "남성")

    // 여성, 남성 선택을 위한 state
    val (selectedGender, setSelectedGender) = remember { mutableStateOf(genderOptions[0]) }

    // 년, 월, 일 선택을 위한 state
    val (selectedYear, setSelectedYear) = remember { mutableStateOf(years[0]) }
    val (selectedMonth, setSelectedMonth) = remember { mutableStateOf(months[0]) }
    val (selectedDay, setSelectedDay) = remember { mutableStateOf(days[0]) }

    // 드롭다운을 위한 state
    var isDropdownExpandedYear by remember { mutableStateOf(false) }
    var isDropdownExpandedMonth by remember { mutableStateOf(false) }
    var isDropdownExpandedDay by remember { mutableStateOf(false) }
    var (flag, setFlag) = remember { mutableStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp)
    ) {
        Text(text = "회원가입")
        Text(text = "회원이 되어 다양한 혜택을 경험해 보세요.")

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = email,
            label = {
                Text("이메일")
            },
            onValueChange = {email = it},
            maxLines = 1,
            singleLine = true
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = nickName,
            label = {
                Text("*닉네임")
            },
            onValueChange = {
                nickName = it
                isError = it.isEmpty()
                            },
            maxLines = 1,
            singleLine = true,
            isError = isError
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = name,
            label = {
                Text("이름")
            },
            onValueChange = {name = it},
            maxLines = 1,
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 여성, 남성 선택
        Row() {
            genderOptions.forEach { gender ->
                Row(
                    Modifier
                        .selectable(
                            selected = (gender == selectedGender),
                            onClick = { setSelectedGender(gender) }
                        )
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically, // 중앙 수직 정렬
                    horizontalArrangement = Arrangement.spacedBy(10.dp) // 요소 사이의 간격 설정
                ) {
                    RadioButton(
                        selected = (gender == selectedGender),
                        onClick = { setSelectedGender(gender) }
                    )
                    Text(text = gender)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 년, 월, 일 선택
        Row() {
            Box(modifier = Modifier.padding(16.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically, // 중앙 수직 정렬
                    horizontalArrangement = Arrangement.spacedBy(10.dp) // 요소 사이의 간격 설정
                ) {
                    Text(text = selectedYear, modifier = Modifier.clickable { isDropdownExpandedYear = true })

                    DropdownMenu(
                        expanded = isDropdownExpandedYear,
                        onDismissRequest = { isDropdownExpandedYear = false },
                        modifier = Modifier.requiredSizeIn(maxHeight = 300.dp)
                    ) {
                        years.forEach { year ->
                            DropdownMenuItem(
                                text = {
                                    Text(text = year)
                                },
                                onClick = {
                                setSelectedYear(year)
                                isDropdownExpandedYear = false
                            })
                        }
                    }
                    Text(text = "년")
                }
            }

            Box(modifier = Modifier.padding(16.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically, // 중앙 수직 정렬
                    horizontalArrangement = Arrangement.spacedBy(10.dp) // 요소 사이의 간격 설정
                ) {
                    Text(
                        text = selectedMonth,
                        modifier = Modifier.clickable { isDropdownExpandedMonth = true })

                    DropdownMenu(
                        expanded = isDropdownExpandedMonth,
                        onDismissRequest = { isDropdownExpandedMonth = false },
                        modifier = Modifier.requiredSizeIn(maxHeight = 300.dp)
                    ) {
                        months.forEach { month ->
                            DropdownMenuItem(
                                text = {
                                    Text(text = month)
                                },
                                onClick = {
                                    setSelectedMonth(month)
                                    isDropdownExpandedMonth = false
                                })
                        }
                    }

                Text(text = "월")
                }
            }

            Box(modifier = Modifier.padding(16.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically, // 중앙 수직 정렬
                    horizontalArrangement = Arrangement.spacedBy(8.dp) // 요소 사이의 간격 설정
                ) {
                    Text(text = selectedDay, modifier = Modifier.clickable { isDropdownExpandedDay = true })

                    DropdownMenu(
                        expanded = isDropdownExpandedDay,
                        onDismissRequest = { isDropdownExpandedDay = false },
                        modifier = Modifier.requiredSizeIn(maxHeight = 300.dp)
                    ) {
                        days.forEach { day ->
                            DropdownMenuItem(
                                text = {
                                    Text(text = day)
                                },
                                onClick = {
                                    setSelectedDay(day)
                                    isDropdownExpandedDay = false
                                })
                        }
                    }
                Text(text = "일")
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Log.d("lastLocation","??? : $permissionsGranted")
        // 2. Launch the permissions request when needed
        if (permissionsGranted) {

            LaunchedEffect(Unit) {
                Log.d("lastLocation","????????")
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

            if (myLocation !== "0") {
                Text(text = "위치 인증 성공")

                Spacer(modifier = Modifier.height(16.dp))

                if (!isError) {
                    Button(onClick = { /*TODO: handle click event*/ }) {
                        Text(text = "확인")
                    }
                }
            }
        } else {
            Button(onClick = {
                requestPermissionsLauncher.launch(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                )
            }) {
                Text(text = "위치 인증 체크")
            }
        }
    }
}


@Preview
@Composable
fun PreviewComponent() {
    SignInScreen()
}
