import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.*
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.Alignment
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
//import androidx.compose.foundation.gestures.ModifierLocalScrollableContainerProvider.value
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextRange
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.ssafy.booking.viewmodel.AppViewModel
import com.ssafy.booking.viewmodel.BookingViewModel
import com.ssafy.booking.viewmodel.DummyAppViewModel
import com.ssafy.booking.viewmodel.SignInViewModel
import com.ssafy.domain.model.SignInRequest
import com.ssafy.domain.model.booking.BookingCreateRequest
import retrofit2.Response

@Preview(showBackground = true)
@Composable
fun preview() {
    BookingCreate(navController = rememberNavController(),
        appViewModel = DummyAppViewModel()
    )
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingCreate(navController: NavController, appViewModel: AppViewModel) {

    // 상태값들 최상단에 정의
    var meetingTitle by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(""))
    }
    var description by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(""))
    }
//    var hashTagText by rememberSaveable { mutableStateOf("공포, 스릴러") }
    var bookIsbn by remember { mutableStateOf(TextFieldValue("788967351021")) }
    var maxParticipants by remember { mutableStateOf(1) }

    // 뷰모델
    val viewModel: BookingViewModel = hiltViewModel()
    val postCreateBookingResponse by viewModel.postCreateBookingResponse.observeAsState()

    Log.d(
        "모임생성",postCreateBookingResponse?.code().toString()
    )

    Scaffold (
        topBar = {
        },
        bottomBar = {
        }
    ){padingValues->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(padingValues)){
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp)
                    .verticalScroll(rememberScrollState()) // 스크롤을 위한 수정자 추가
            ) {
                TopBar(
                    navController
                )
                BookSearch()
                TextFieldsSection(
                    meetingTitle = meetingTitle,
                    onMeetingTitleChanged = { meetingTitle = it },
                    description = description,
                    onDescriptionChanged = { description = it }
                )
                ParticipantCounter(
                    maxParticipants = maxParticipants,
                    onMaxParticipantsChanged = { newCount -> maxParticipants = newCount }

                )
                CreateBookingButton(
                    bookIsbn = bookIsbn.text,
                    meetingTitle = meetingTitle.text, // TextFieldValue에서 String으로 변환
                    description = description.text,
                    maxParticipants = maxParticipants,
                    // 이거랑 isbn 하드코딩.
                    hashtagList = listOf("공포", "스릴러")
                )
            }
        }

    }
    }


@Composable
fun TopBar(
    navController: NavController
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth() // Row가 부모의 최대 너비를 채우도록 설정합니다.
                    ,
            // 자식 요소들을 수평 방향으로 중앙에 배치합니다.
            verticalAlignment = Alignment.CenterVertically // 자식 요소들을 세로 방향으로 중앙에 배치합니다.
        ) {
            Icon(
                imageVector = Icons.Filled.Close,
                contentDescription = null,
                modifier = Modifier
                    .size(40.dp) // 아이콘의 크기를 설정합니다.
                    .padding(end = 4.dp) // 아이콘과 텍스트 사이의 간격을 설정합니다
                    .clickable {
                        navController.popBackStack() // 뒤로가기 버튼
                    }
            )
            Text(
                text = "독서 모임 생성하기",
                style = MaterialTheme.typography.bodyLarge,
                // 여기에는 weight나 wrapContentWidth를 사용하지 않습니다.

            )

        }
        Divider(
            color = Color.LightGray, thickness = 0.8.dp, modifier = Modifier
                .fillMaxWidth()
        )
    }
    Spacer(modifier = Modifier.height(40.dp))
}
@Composable
fun BookSearch() {
    Column(
        modifier = Modifier
            .background(Color.Transparent)
            .fillMaxWidth()
            .wrapContentHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // 왼쪽의 도서 등록 칸
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .background(Color.LightGray)
                .width(150.dp)
                .height(210.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(80.dp)
            )
        }
        Text(
            text = "인간실격",
            style = MaterialTheme.typography.headlineSmall
        )
        Text(
            text = "다자이 오사무",
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.height(25.dp))
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextFieldsSection(
    meetingTitle: TextFieldValue,
    onMeetingTitleChanged: (TextFieldValue) -> Unit,
    description: TextFieldValue,
    onDescriptionChanged: (TextFieldValue) -> Unit

) {

    Text(text = "모임 제목")
    Spacer(modifier = Modifier.height(12.dp))
    OutlinedTextField(
        value = meetingTitle,
        onValueChange = onMeetingTitleChanged,
        placeholder = { Text("모임 제목") },
        singleLine = true
    )
    Spacer(modifier = Modifier.height(24.dp))

    Text(text = "모임 소개")
    Spacer(modifier = Modifier.height(12.dp))
    OutlinedTextField(
        value = description,
        onValueChange = onDescriptionChanged,
        placeholder = { Text("모임 소개") },
        maxLines = 6,  // 최대 6줄 입력 가능
        modifier = Modifier.height(192.dp)
    )
    Spacer(modifier = Modifier.height(24.dp))
    Text(text = "해시 태그")
    Spacer(modifier = Modifier.height(12.dp))
//    OutlinedTextField(
//        value = hashTagText,
//        onValueChange = { hashTagText = it },
//        placeholder = { Text("해시태그") }
//    )
    Spacer(modifier = Modifier.height(24.dp))
}
@Composable
fun CreateBookingButton(
    bookIsbn : String,
    meetingTitle: String,
    description: String,
    maxParticipants: Int,
    hashtagList: List<String>,
    viewModel: BookingViewModel = hiltViewModel()
) {
    Button(
        onClick = {
            val request = BookingCreateRequest(
                bookIsbn= bookIsbn,
                meetingTitle = meetingTitle,
                description = description,
                maxParticipants = maxParticipants,
                hashtagList = hashtagList
            )
            viewModel.postCreateBooking(request)
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
//            .offset(y = 30.dp)
        ,
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00C68E)),
                shape = RoundedCornerShape(4.dp) // 모서리의 라운드를 4.dp로 설정합니다.
    ) {
        Text(text = "모임 생성하기")
    }
}
@Composable
fun ParticipantCounter(
    maxParticipants: Int,
    onMaxParticipantsChanged: (Int) -> Unit
) {
    // 참가자 수를 추적하는 상태 변수
    // 수평으로 정렬된 구성요소들을 포함하는 Row
    Text(text = "모임 인원 ( 최대 6명 )")
    Spacer(modifier = Modifier.height(12.dp))
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        // 참가자 수를 줄이는 버튼
        Button(
            onClick = { if (maxParticipants > 1)onMaxParticipantsChanged(maxParticipants - 1)},
            shape = CircleShape,
            enabled = maxParticipants > 1, // 1보다 작아질 수 없도록 비활성화,
        ) {
            Text("-")
        }
        // 현재 참가자 수를 보여주는 Text
        Text(
            text = " $maxParticipants 명",
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp)
        )
        // 참가자 수를 늘리는 버튼
        Button(
            onClick = { if (maxParticipants < 6 ) onMaxParticipantsChanged(maxParticipants + 1)  },
            shape = CircleShape,
            enabled = maxParticipants < 6
        ) {
            Text("+")
        }
    }
}