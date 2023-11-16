package com.ssafy.booking.ui.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.ssafy.booking.R
import com.ssafy.booking.ui.LocalNavigation

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyBookingList(
    data: ProfileData
) {
    Scaffold(

    ) {paddingValues->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(32.dp)
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            Text("북킹")
        }
    }
}

@Composable
fun MyBookingFloatingActionButton() {
    val navController = LocalNavigation.current

    FloatingActionButton(
        onClick = { navController.navigate("create/booking/isbn") },
        modifier = Modifier
            .padding(end = 16.dp, bottom = 10.dp)
            .size(65.dp),
        containerColor = colorResource(id = R.color.booking_1),
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
