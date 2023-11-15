import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.*
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
// import androidx.compose.foundation.gestures.ModifierLocalScrollableContainerProvider.value
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.ssafy.booking.di.App
import com.ssafy.booking.ui.AppNavItem
import com.ssafy.booking.ui.LocalNavigation
import com.ssafy.booking.ui.common.TopBar
import com.ssafy.booking.viewmodel.AppViewModel
import com.ssafy.booking.viewmodel.BookSearchViewModel
import com.ssafy.booking.viewmodel.BookingViewModel
import com.ssafy.booking.viewmodel.LocationViewModel
import com.ssafy.domain.model.booking.BookingCreateRequest
import com.ssafy.domain.model.booksearch.BookSearchResponse
import retrofit2.Response

// @Preview(showBackground = true)
// @Composable
// fun preview() {
//    BookingCreate(navController = rememberNavController(),
//        appViewModel = DummyAppViewModel()
//    )
// }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingCreate(navController: NavController, appViewModel: AppViewModel, isbn: String?) {
    // 상태값들 최상단에 정의
    var meetingTitle by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(""))
    }
    var description by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(""))
    }
    var hashTagText by remember { mutableStateOf(listOf<String>()) }

//    var bookIsbn by remember { mutableStateOf(TextFieldValue(isbn)) }
    var maxParticipants by remember { mutableStateOf(2) }



    // 뷰모델
    val viewModel: BookingViewModel = hiltViewModel()
    val locationViewModel : LocationViewModel = hiltViewModel()
    val postCreateBookingResponse by viewModel.postCreateBookingResponse.observeAsState()
    val createBookingSuccess by viewModel.createBookingSuccess.observeAsState()
    // isbn 으로 데이터 불러오기
    val bookSearchViewModel: BookSearchViewModel = hiltViewModel()
    val getBookSearchByIsbnResponse by bookSearchViewModel.getBookSearchByIsbnResponse.observeAsState()
    val address = App.prefs.getShortUserAddress()



    LaunchedEffect(Unit){
        isbn?.let {
            bookSearchViewModel.getBookSearchByIsbn(isbn)
        }
    }

    LaunchedEffect(createBookingSuccess) {
        createBookingSuccess?.let { success ->
            if (success) {
                navController.navigate(AppNavItem.Main.route) {
                    popUpTo("login") { inclusive = true }
                    launchSingleTop = true
                }
                // 상태를 리셋하여 중복 네비게이션을 방지합니다.
                viewModel.resetCreateBookingSuccess()
            }
        }
    }

    Log.d(
        "모임생성",
        postCreateBookingResponse?.code().toString()
    )

    Scaffold(
        topBar = {
        },
        bottomBar = {
        }
    ) { padingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp)
                    .verticalScroll(rememberScrollState()) // 스크롤을 위한 수정자 추가
            ) {
                TopBar(
                    navController
                )
                BookSearch(getBookSearchByIsbnResponse)
                TextFieldsSection(
                    meetingTitle = meetingTitle,
                    onMeetingTitleChanged = { meetingTitle = it },
                    description = description,
                    onDescriptionChanged = { description = it },
                    hashTagText = hashTagText,
                    onAddHashTag = { tag ->
                        if (!hashTagText.contains(tag)) {
                            hashTagText = hashTagText + tag
                        }
                    },
                    onRemoveHashTag = { tag ->
                        hashTagText = hashTagText.filter { it != tag }
                    }
                )
                ParticipantCounter(
                    maxParticipants = maxParticipants,
                    onMaxParticipantsChanged = { newCount -> maxParticipants = newCount }

                )
                CreateBookingButton(
                    bookIsbn = "$isbn",
                    meetingTitle = meetingTitle.text, // TextFieldValue에서 String으로 변환
                    description = description.text,
                    maxParticipants = maxParticipants,
                    hashtagList = hashTagText,
                    address = address?:"",
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
                .fillMaxWidth(), // Row가 부모의 최대 너비를 채우도록 설정합니다.
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
                        navController.navigate("main") {
                            popUpTo("main") { inclusive = true }
                        } // 뒤로가기 버튼
                    }
            )
            Text(
                text = "독서 모임 생성하기",
                style = MaterialTheme.typography.bodyLarge
                // 여기에는 weight나 wrapContentWidth를 사용하지 않습니다.

            )
        }
        Divider(
            color = Color.LightGray,
            thickness = 0.8.dp,
            modifier = Modifier
                .fillMaxWidth()
        )
    }
    Spacer(modifier = Modifier.height(40.dp))
}

@Composable
fun BookSearch(
    getBookSearchByIsbnResponse: Response<BookSearchResponse>?
) {
    val navController = LocalNavigation.current

    Column(
        modifier = Modifier
            .background(Color.Transparent)
            .fillMaxWidth()
            .wrapContentHeight(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .background(Color.LightGray)
                .width(150.dp)
                .height(210.dp)
                .clickable {
                    navController.navigate("book/1")
                }
        ) {
            // 왼쪽의 도서 등록 칸
            getBookSearchByIsbnResponse?.let {
                val book = it.body()
                AsyncImage(
                    model = book!!.coverImage,
                    contentDescription = "북 커버",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                )
            } ?: run {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(80.dp)
                )
            }
        }
        getBookSearchByIsbnResponse?.let {
            val book = it.body()
            Text(
                text = "${book!!.title}",
                style = MaterialTheme.typography.headlineSmall
            )
            Text(
                text = "${book!!.author}",
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Spacer(modifier = Modifier.height(25.dp))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextFieldsSection(
    meetingTitle: TextFieldValue,
    onMeetingTitleChanged: (TextFieldValue) -> Unit,
    description: TextFieldValue,
    onDescriptionChanged: (TextFieldValue) -> Unit,
    hashTagText: List<String>,
    onAddHashTag: (String) -> Unit,
    onRemoveHashTag: (String) -> Unit

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
        maxLines = 6, // 최대 6줄 입력 가능
        modifier = Modifier.height(192.dp)
    )
    Spacer(modifier = Modifier.height(24.dp))
    Text(text = "해시 태그")
    Spacer(modifier = Modifier.height(12.dp))
    HashTagEditor(
        hashTagText = hashTagText,
        onAddHashTag = onAddHashTag,
        onRemoveHashTag = onRemoveHashTag
    )

    Spacer(modifier = Modifier.height(24.dp))
}

@Composable
fun CreateBookingButton(
    bookIsbn: String,
    meetingTitle: String,
    description: String,
    maxParticipants: Int,
    hashtagList: List<String>,
    address: String,
    viewModel: BookingViewModel = hiltViewModel()
) {
    Button(
        onClick = {
            val request = BookingCreateRequest(
                bookIsbn = bookIsbn,
                meetingTitle = meetingTitle,
                description = description,
                maxParticipants = maxParticipants,
                hashtagList = hashtagList,
                address = address
            )
            viewModel.postCreateBooking(request)
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
//            .offset(y = 30.dp)
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00C68E)),
        shape = RoundedCornerShape(4.dp)
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
            onClick = { if (maxParticipants > 1)onMaxParticipantsChanged(maxParticipants - 1) },
            shape = CircleShape,
            enabled = maxParticipants > 2, // 1보다 작아질 수 없도록 비활성화,
            colors = ButtonDefaults.buttonColors(Color(0xFf00C68E))
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
            onClick = { if (maxParticipants < 6) onMaxParticipantsChanged(maxParticipants + 1) },
            shape = CircleShape,
            enabled = maxParticipants < 6,
            colors = ButtonDefaults.buttonColors(Color(0xFf00C68E))
        ) {
            Text("+")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HashTagEditor(
    hashTagText: List<String>,
    onAddHashTag: (String) -> Unit,
    onRemoveHashTag: (String) -> Unit
) {
    var text by remember { mutableStateOf(TextFieldValue("")) }
    val keyboardController = LocalSoftwareKeyboardController.current

    Column {
        OutlinedTextField(
            value = text,
            onValueChange = { newText ->
                if (newText.text.length <= 5) {
                    text = newText
                }
            },
            singleLine = true,
            label = { Text("해시태그") },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = {
                    if (text.text.isNotBlank() && text.text.length <= 4) {
                        // '완료(Done)' 버튼이 눌렸을 때 할 작업
                        onAddHashTag(text.text.trim())
                        text = TextFieldValue("")
                        keyboardController?.hide()
                    }
                }
            ),
            modifier = Modifier
                .fillMaxWidth()
                .onKeyEvent { keyEvent ->
                    if (keyEvent.key == Key.Enter && text.text.isNotBlank() && text.text.length <= 5) {
                        onAddHashTag(text.text.trim())
                        text = TextFieldValue("") // Reset text field
                        keyboardController?.hide()
                        true // Event consumed
                    } else {
                        false
                    }
                }
        )
        Row(
            Modifier
                .padding(top = 10.dp)
        ) {
            hashTagText.forEach { tag ->
                Button(
                    modifier = Modifier.padding(end = 10.dp),
                    shape = RoundedCornerShape(4.dp),
                    onClick = { onRemoveHashTag(tag) }
                ) {
                    Text(text = "#$tag")
                }
            }
        }
    }
}
