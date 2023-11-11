package com.ssafy.booking.ui.booking.bookingSetting

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import java.time.LocalDate
import java.time.LocalTime

@Preview
@Composable
fun SetDateTime() {
    var selectedDate by remember { mutableStateOf<LocalDate?>(null) }
    var selectedTime by remember { mutableStateOf<LocalTime?>(null) }

    Column {
        DatePickerComposable(onDateSelected = { date ->
            selectedDate = date
        })

        TimePickerComposable(onTimeSelected = { time ->
            selectedTime = time
        })

        Text("선택된 날짜: ${selectedDate ?: "없음"}")
        Text("선택된 시간: ${selectedTime ?: "없음"}")
    }
}

@Composable
fun DatePickerComposable(onDateSelected: (LocalDate) -> Unit) {
    val context = LocalContext.current
    val currentDate = LocalDate.now()
    val datePickerDialog = remember {
        DatePickerDialog(context, { _, year, month, dayOfMonth ->
            onDateSelected(LocalDate.of(year, month + 1, dayOfMonth))
        }, currentDate.year, currentDate.monthValue - 1, currentDate.dayOfMonth).apply {
            // 오늘과 오늘 이후만 선택가능하게 예외처리
            datePicker.minDate = System.currentTimeMillis() - 1000
        }
    }
    Button(onClick = { datePickerDialog.show() }) {
        Text("날짜 선택")
    }
}

@Composable
fun TimePickerComposable(onTimeSelected: (LocalTime) -> Unit) {
    val context = LocalContext.current
    val timePickerDialog = remember {
        TimePickerDialog(context, { _, hour, minute ->
            onTimeSelected(LocalTime.of(hour, minute))
        }, LocalTime.now().hour, LocalTime.now().minute, true)
    }

    Button(onClick = { timePickerDialog.show() }) {
        Text("시간 선택")
    }
}
