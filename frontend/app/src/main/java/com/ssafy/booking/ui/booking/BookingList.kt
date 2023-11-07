package com.ssafy.booking.ui.booking

import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ssafy.booking.ui.common.BottomNav
import com.ssafy.booking.viewmodel.AppViewModel
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import com.ssafy.booking.R
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.ssafy.booking.ui.AppNavItem
import com.ssafy.booking.ui.common.TopBar
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.BaselineShift


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Main(
    navController: NavController, appViewModel: AppViewModel
) {
    Scaffold (
        topBar = {
            HomeTopBar(navController,  appViewModel )
//            TopBar("하남동")
        },
        bottomBar = {
            BottomNav(navController, appViewModel)
        },
        floatingActionButton = {
            MyFloatingActionButton(navController,appViewModel)
        },
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .fillMaxHeight()
        ) {
            BookList(navController, appViewModel)
        }
    }
}

@Composable
fun BookList(navController: NavController, appViewModel: AppViewModel) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .fillMaxHeight()
            .padding(horizontal = 8.dp, vertical = 15.dp)
    ) {
        items(bookItemsList) { book ->
            BookItem(book)
        }
    }
}
@Composable
fun BookItem(book: Book) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
    ) {
        Image(
            painter = painterResource(id = book.imageResId),
            contentDescription = "Book Image",
            modifier = Modifier
                .size(80.dp, 100.dp)
                .clip(RoundedCornerShape(10.dp))
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(text = book.bookName, fontWeight = FontWeight.ExtraBold, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = book.title,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight.Medium,
                fontSize = 12.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically)

            {
                Icon(Icons.Outlined.LocationOn, contentDescription = "locate",modifier = Modifier.size(12.dp), tint = Color.Gray)
                Text(
                    text = book.locate,
                    fontWeight = FontWeight.Medium,
                    color = Color.Gray,
                    fontSize = 12.sp
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.align(Alignment.End) // Align to the end of the column
            ) {
                Icon(
                    imageVector = Icons.Outlined.Person, // Use the appropriate person icon
                    contentDescription = "Participants Icon",
                    modifier = Modifier.size(12.dp),
                    tint = Color.Gray
                )
                Text(
                    text = "${book.currentPeople}/${book.maxPeople}명",
                    fontWeight = FontWeight.Normal,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(start = 2.dp), color = Color.Gray
                )
            }
            }
        }
        Divider(color = Color.LightGray, thickness = 0.4.dp, modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp))
    }


// 모임 생성 버튼
@Composable
fun MyFloatingActionButton(navController: NavController, appViewModel: AppViewModel) {
    FloatingActionButton(
        onClick = {navController.navigate(AppNavItem.CreateBooking.route)},
        modifier = Modifier
            .padding(end = 16.dp, bottom = 10.dp)
            .size(65.dp)
        , containerColor = Color(0xFF12BD7E),
        shape = CircleShape
        // 그냥 동그라미할지, + 모임생성할지 고민.

    ) {
        Icon(
            Icons.Filled.Add,
            contentDescription = "Localized description",
            modifier = Modifier.size(40.dp),
            tint = Color.White

        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopBar(navController: NavController, appViewModel: AppViewModel) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = Color(0xFF12BD7E),
                shape = RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp)
            ) // 배경색과 모서리를 둥글게 설정
    ) {
        // 상단의 하남동과 설정 아이콘
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(top = 10.dp)
            ) {
                Text(text = "하남동", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFFffffff))
                Icon(Icons.Filled.ArrowDropDown, contentDescription = null, tint = Color(0xFFffffff))
            }
            Icon(
                Icons.Rounded.Settings,
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                ,
                tint = Color(0xFFffffff)
            )

        }
        // 검색 창
        var title by rememberSaveable(stateSaver = TextFieldValue.Saver) {
            mutableStateOf(TextFieldValue(""))
        }
        OutlinedTextField(
            value = title, // 이 부분을 뷰모델의 상태로 연결하거나 필요에 따라 변경
            onValueChange = { title = it },
            placeholder = {Text("찾는 도서가 있나요?",fontSize = 11.sp,color = Color.Gray)},
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
                unfocusedBorderColor = Color.White,
            ),
            textStyle = TextStyle(color=Color.Gray,fontSize = 11.sp, baselineShift = BaselineShift.None),
            leadingIcon = { Icon(Icons.Outlined.Search, contentDescription = null, tint = Color(0xFF12BD7E) )}
        )
    }
}


data class Book(val imageResId: Int, val bookName: String, val title: String, val locate:String, val currentPeople:Int, val maxPeople:Int )

val bookItemsList = listOf(
    Book(R.drawable.book1, "데미안", "데미안에 대해 읽고 같이 토론하실 분","하남동",3,6),
    Book(R.drawable.book2, "인간실격", "인간실격에 대해 읽고 같이 토론하실 분","장덕동",2,2),
    Book(R.drawable.book5, "불편한 편의점", "불편한 편의점 대해 읽고 같이 토론하실 분","수완동",3,3),
    Book(R.drawable.book4, "나미야 잡화점의 기적", "데미안에 대해 읽고 같이 토론하실 분","하남동",1,5),
    Book(R.drawable.book5, "불편한 편의점", "데미안에 대해 읽고 같이 토론하실 분","하남동",5,6),
    Book(R.drawable.book1, "데미안", "데미안에 대해 읽고 같이 토론하실 분","장덕동",2,6),
    Book(R.drawable.book7, "불편한 편의점2", "데미안에 대해 읽고 같이 토론하실 분","하남동",1,6),
    Book(R.drawable.book5, "불편한 편의점1", "데미안에 대해 읽고 같이 토론하실 분","하남동",1,6),
    Book(R.drawable.book2, "인간실격", "데미안에 대해 읽고 같이 토론하실 분","하남동",4,6),
    Book(R.drawable.book4, "나미야 잡화점의 기적", "데미안에 대해 읽고 같이 토론하실 분","하남동",3,6),
    Book(R.drawable.book5, "데미안", "데미안에 대해 읽고 같이 토론하실 분","하남동",6,6),


)




