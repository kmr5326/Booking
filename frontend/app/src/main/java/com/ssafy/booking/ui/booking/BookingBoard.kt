package com.ssafy.booking.ui.booking

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.outlined.Create
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ssafy.booking.ui.LocalNavigation
import com.ssafy.booking.ui.profile.MyBookFloatingActionButton
import com.ssafy.booking.viewmodel.AppViewModel
import com.ssafy.booking.viewmodel.BookingBoardViewModel
import com.ssafy.domain.model.booking.BookingBoardReadListResponse

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingBoard(meetingId : Long,
                 memberRole : String,
                 meetingState : String) {
    val viewModel : BookingBoardViewModel = hiltViewModel()
    val boardList = viewModel.getBookingBoardReadListResponse.observeAsState()

    LaunchedEffect(Unit) {
        viewModel.getBookingBoardReadList(meetingId)
    }

    Scaffold(
        floatingActionButton = {
            BoardCreateButton(meetingId)
        }
    ) {paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            boardList.value?.let {
                it.body()?.let {
                    items(it.size) {idx->
                        boardItem(it[idx])
                    }
                }
            }
        }
    }
}

@Composable
fun boardItem(board: BookingBoardReadListResponse){
    Row(
        modifier = Modifier.fillMaxWidth()
            .padding(10.dp)
    ) {
        Text(text = "${board.postId}")
        Spacer(modifier = Modifier.size(10.dp))
        Text(text = "${board.title}")
        Spacer(modifier = Modifier.size(10.dp))
        Text(text = "${board.nickname}")
        Spacer(modifier = Modifier.size(10.dp))
        Text(text = "${board.createdAt}")
    }
}

@Composable
fun BoardCreateButton(
    meetingId: Long
) {
    val navController = LocalNavigation.current

    FloatingActionButton(
        onClick = { navController.navigate("booking/board/create/$meetingId") },
        modifier = Modifier
            .padding(end = 16.dp, bottom = 10.dp)
            .size(65.dp),
        containerColor = Color(0xFF12BD7E),
        shape = CircleShape
    ) {
        Icon(
            Icons.Outlined.Create,
            contentDescription = "Localized description",
            modifier = Modifier.size(40.dp),
            tint = Color.White

        )
    }
}