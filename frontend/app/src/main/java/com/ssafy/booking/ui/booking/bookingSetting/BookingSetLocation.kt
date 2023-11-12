package com.ssafy.booking.ui.booking.bookingSetting

import androidx.compose.foundation.background
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
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ssafy.booking.viewmodel.BookingViewModel
import com.ssafy.booking.viewmodel.LocationViewModel

@Preview
@Composable
fun SetLocation() {
    SetLocationSearch()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetLocationSearch() {
    // 검색 창
    var location by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(""))
    }
    val viewModel: LocationViewModel = hiltViewModel()
    val getKakaoSearchResponse by viewModel.getKakaoSearchResponse.observeAsState()

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
                viewModel.getSearchList(location.text, 1, 15)
            }
        )
    )
    val response = getKakaoSearchResponse?.body()?.documents

    if (response != null && response.isNotEmpty()) {
        LazyColumn {
            items(response) { item ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .shadow(4.dp, RoundedCornerShape(4.dp)),

                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text(text = "주소: ${item.addressName ?: "정보 없음"}")
                        Text(text = "URL: ${item.placeUrl ?: "정보 없음"}")
                        Text(text = "X 좌표: ${item.x ?: "정보 없음"}")
                        Text(text = "Y 좌표: ${item.y ?: "정보 없음"}")
                    }
                }
            }
        }
    } else {
        Text(text = "검색 결과가 없습니다.")
    }


}

