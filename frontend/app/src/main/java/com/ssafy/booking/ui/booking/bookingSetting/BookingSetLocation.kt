package com.ssafy.booking.ui.booking.bookingSetting

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ssafy.booking.di.App
import com.ssafy.booking.ui.AppNavItem
import com.ssafy.booking.ui.LocalNavigation
import com.ssafy.booking.viewmodel.BookingViewModel
import com.ssafy.booking.viewmodel.LocationViewModel
import com.ssafy.domain.model.loacation.KakaoSearchResponse

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun SetLocation() {
    val locationViewModel: LocationViewModel = hiltViewModel()
    val bookingViewModel: BookingViewModel = hiltViewModel()
    val getKakaoSearchResponse by locationViewModel.getKakaoSearchResponse.observeAsState()
    val locationState by bookingViewModel.location.observeAsState()
    val latState by bookingViewModel.lat.observeAsState()
    val lgtState by bookingViewModel.lgt.observeAsState()
    val placeNameState by bookingViewModel.placeName.observeAsState()
    val showSearchResults = remember { mutableStateOf(true) }
    Scaffold(
        bottomBar = {
            // 바텀 버튼을 Scaffold의 bottomBar로 설정합니다.
            SetLocationBottomButton(locationState, placeNameState)
        }
    )

    { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            SetLocationSearch(locationViewModel,showSearchResults)
            SelectedLocation(bookingViewModel, locationState, placeNameState)
            SearchResult(bookingViewModel, locationViewModel,showSearchResults)
        }
    }

}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetLocationSearch(viewModel:LocationViewModel,showSearchReults: MutableState<Boolean>) {
    // 검색 창
    var location by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(""))
    }
    val lat = App.prefs.getLat().toString()
    val lgt = App.prefs.getLgt().toString()
    OutlinedTextField(
        value = location, // 이 부분을 뷰모델의 상태로 연결하거나 필요에 따라 변경
        onValueChange = { location = it },
        placeholder = { Text("모임 위치를 검색해주세요.", fontSize = 11.sp, color = Color.Gray) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(top = 4.dp)
            .padding(bottom = 16.dp)
            .height(50.dp)
            .background(Color.White, shape = RoundedCornerShape(3.dp)),
        singleLine = true,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = Color(0xFF12BD7E),
            unfocusedBorderColor = Color.White
        ),
        textStyle = TextStyle(color = Color.Gray, fontSize = 11.sp, baselineShift = BaselineShift.None),
        leadingIcon = { Icon(Icons.Outlined.Search, contentDescription = null, tint = Color(0xFF12BD7E)) },
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                // 사용자 설정 위치에서 반경 20km 안에서만 검색
                viewModel.getSearchList(location.text, 10, 15,lat,lgt,20000)
                showSearchReults.value = true
            }
        )
    )
}
@Composable
fun SelectedLocation(bookingViewModel: BookingViewModel,locationState:String?,placeNameState:String?) {
    Text(text="선택된 모임 장소: ${placeNameState ?: "정보 없음"}")
    Text(text="선택된 모임 위치: ${locationState ?: "정보 없음"}")
}
@Composable
fun SearchResult(bookingViewModel: BookingViewModel,locationViewModel: LocationViewModel,showSearchResults: MutableState<Boolean>) {
    val getKakaoSearchResponse by locationViewModel.getKakaoSearchResponse.observeAsState()
    // 클릭시 검색결과 사라지게


        val response = getKakaoSearchResponse?.body()?.documents
        if (showSearchResults.value && response != null && response.isNotEmpty()) {
            LazyColumn {
                items(response) { item ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .shadow(4.dp, RoundedCornerShape(2.dp))
                            .clickable {
                                // Card 클릭 시 ViewModel의 상태 업데이트
                                bookingViewModel.location.value = item.addressName
                                bookingViewModel.lat.value = item.y
                                bookingViewModel.lgt.value = item.x
                                bookingViewModel.placeName.value = item.placeName
                                showSearchResults.value = false
                                App.prefs.putMeetingLat(item.y.toFloat())
                                App.prefs.putMeetingLgt(item.x.toFloat())
                                App.prefs.putMeetingAddress(item.addressName)
                                App.prefs.putMeetingLocation(item.placeName)
                            },
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Text(text = "플레이스네임: ${item.placeName ?: "정보 없음"}")
                            Text(text = "주소: ${item.addressName ?: "정보 없음"}")
                            Text(text = "거리: ${item.distance ?: "정보 없음"}")
                            Text(text = "거리: ${item.categoryGroupName ?: "정보 없음"}")
                        }
                    }
                }
            }
        } else {
            Text(text = "검색 결과가 없습니다.")
        }
    }

@Composable
fun SetLocationBottomButton(
    locationState: String?,
    placeNameState: String?
) {
    // 현재 Composable 함수와 연관된 Context 가져오기
    val context = LocalContext.current
    val navController = LocalNavigation.current

    Box(
        contentAlignment = Alignment.BottomCenter,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Button(
            onClick = {
                if (locationState.isNullOrEmpty() || placeNameState.isNullOrEmpty()) {
                    // 제목 또는 내용이 비어있을 경우 Toast 메시지 표시
                    Toast.makeText(context, "독서모임을 진행할 장소를 선택해주세요.", Toast.LENGTH_LONG).show()
                } else {
                    // 모두 입력된 경우 네비게이션
                    navController.navigate(AppNavItem.BookingSetDateAndFee.route)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(3.dp)
        ) {
            androidx.wear.compose.material.Text("다음", style = MaterialTheme.typography.bodyMedium)
        }
    }
}