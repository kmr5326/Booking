package com.ssafy.booking.ui.location

import androidx.compose.foundation.background
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Preview
@Composable
fun PreviewSettingLocation() {
    SettingLocation()
}

// 최상단 컴포저블
@Composable
fun SettingLocation() {
    Column {
        Text(text = "내 동네 설정")
        SearchInput()
        SetCurrentLocation()
        // 이 밑에 지도 띄우고 현재 위치 띄우면 되지 않을까?
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
    Row {
        Icon(Icons.Outlined.LocationOn, contentDescription = null, tint = Color(0xFF12BD7E))
        Text(text = "현재 내 위치로 설정")
        Icon(Icons.Outlined.KeyboardArrowRight, contentDescription = null, tint = Color(0xFF12BD7E))
    }
}
