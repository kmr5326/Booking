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
//import androidx.compose.foundation.gestures.ModifierLocalScrollableContainerProvider.value
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.text.TextRange
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.ssafy.booking.viewmodel.AppViewModel
import com.ssafy.booking.viewmodel.DummyAppViewModel

@Preview(showBackground = true)
@Composable
fun preview() {
    BookingCreate(navController = rememberNavController(),
        appViewModel = DummyAppViewModel()
    )
}

@Composable
fun BookingCreate(navController: NavController, appViewModel: AppViewModel) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
//            horizontalAlignment = Alignment.CenterHorizontally,
//            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxSize().padding(16.dp)
        ) {
            TopBar()
            BookSearch()
            TextFieldsSection()
            CreateBookingButton()
        }
    }
}

@Composable
fun TopBar() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start
    ) {
        Text(text = "독서 모임 생성하기")
    }
}

@Composable
fun BookSearch() {
    Row(
        modifier = Modifier
            .background(Color.Transparent)
            .fillMaxWidth()
            .height(210.dp)
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
        
        // 도서추가와 텍스트 사이의 간격
        Spacer(modifier = Modifier.width(16.dp))  

        // 오른쪽에 텍스트
        Column(

            verticalArrangement = Arrangement.Top

        ) {
            Text(
                text = "인간실격",
                style = MaterialTheme.typography.headlineMedium
            )
            Text(
                text = "다자이 오사무 / 글쓴이",
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "세상에서 가장 아름다운, 39세의 젊은 남자에게 최고의 예의를 다하는 다자이 오스무의 작품 <인간 실격>이 출간되었다. 우울 소설하면 그만한..",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextFieldsSection() {
    var text by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue("", TextRange(0, 7)))
    }

    Text(text = "모임 제목")
    OutlinedTextField(
        value = text,
        onValueChange = { text = it },
        label = { Text("모임 제목") }
    )
    Text(text = "모임 소개")

    OutlinedTextField(
        value = text,
        onValueChange = { text = it },
        label = { Text("모임 소개") }
    )
    Text(text = "해시 태그")

    OutlinedTextField(
        value = text,
        onValueChange = { text = it },
        label = { Text("해시태그") }
    )
}
@Composable
fun CreateBookingButton() {
    Button(
        onClick = {},
        modifier = Modifier.fillMaxWidth().height(50.dp)
            .offset(y=30.dp)
        ,

        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00C68E))
    ) {
        Text(text = "생성하기")
    }
}

