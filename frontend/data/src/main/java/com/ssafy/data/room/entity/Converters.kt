package com.ssafy.data.room.entity

import androidx.room.TypeConverter
import com.google.gson.Gson
import java.sql.Timestamp

class Converters {
    @TypeConverter
    fun ListToJson(value: List<Int>): String {
        return Gson().toJson(value)
    }
    @TypeConverter
    fun jsonToList(value: String): List<Int> {
        return Gson().fromJson(value, Array<Int>::class.java).toList()
    }

    @TypeConverter
    fun dateToTimestamp(timestamp: Timestamp?): Long? {
        return timestamp?.time
    }

    @TypeConverter
    fun fromTimestamp(value: Long?): Timestamp? {
        return value?.let { Timestamp(it) }
    }

}