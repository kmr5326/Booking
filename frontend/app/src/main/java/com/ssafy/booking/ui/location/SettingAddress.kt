package com.ssafy.booking.ui.location

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
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
        Text(text = "내 동네 설정")
        SearchInput()
        SetCurrentLocation()
    }
    // 제목
}

// 검색버튼
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchInput() {
    var title by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(""))
    }
    OutlinedTextField(
        value = title, // 이 부분을 뷰모델의 상태로 연결하거나 필요에 따라 변경
        onValueChange = { title = it },
        placeholder = { Text("지번, 도로명, 건물명으로 검색", fontSize = 11.sp, color = Color.Gray) },
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
        leadingIcon = { Icon(Icons.Outlined.Search, contentDescription = null, tint = Color(0xFF12BD7E)) }
    )
}

// 현재 위치로 설정
@Composable
fun SetCurrentLocation() {
    val locationViewModel: LocationViewModel = hiltViewModel()
    val isLoading = locationViewModel.isLoading.value
    val addressData = locationViewModel.getAddressResponse.value
    val errorMessage = locationViewModel.errorMessage.value
    val searchListData = locationViewModel.getKakaoSearchResponse.value


    Row(
        modifier = Modifier
            .clickable {
                //    val context = LocalContext.current
//    val tokenDataSource = TokenDataSource(context)
//    val loginId = tokenDataSource.getLoginId()
                // 위랑 아래랑 같은 코드
                val loginId2 = App.prefs.getLoginId()
                val lng = "127.423084873712"
                val lat = "37.0789561558879"
                locationViewModel.getAddress(lng, lat)
                locationViewModel.getSearchList("스타벅스 하남점", 1, 15)
            }
    ) {
        Text(text= searchListData.toString() ?: "검색결과가 없습니다")
        if (isLoading) {
            Text(text = "로딩중")
        } else {
            if (addressData != null) {
//                Text(text = addressData.body()?.documents?.get(0).toString())
                val addressName = addressData.body()?.documents?.firstOrNull()?.address?.addressName
                val region1 = addressData.body()?.documents?.firstOrNull()?.address?.region2DepthName
                val region2 = addressData.body()?.documents?.firstOrNull()?.address?.region3DepthName
                Text(text = addressName ?: "주소 정보가 없습니다.")
                Text(text = region1 ?: "주소 정보가 없습니다.")
                Text(text = region2 ?: "주소 정보가 없습니다.")
            } else {
                Text(text = "주소가 없습니다.")
            }
        }
        if (errorMessage != null) {
            Text(text = errorMessage)
        }
        Icon(Icons.Outlined.LocationOn, contentDescription = null, tint = Color(0xFF12BD7E))
        Text(text = "현재 내 위치로 설정")
        Icon(Icons.Outlined.KeyboardArrowRight, contentDescription = null, tint = Color(0xFF12BD7E))
    }
}
