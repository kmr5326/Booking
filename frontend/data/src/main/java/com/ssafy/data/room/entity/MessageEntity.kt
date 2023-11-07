package com.ssafy.data.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.google.gson.Gson
import java.sql.Timestamp
import java.time.LocalDateTime

@Entity(tableName = "messageEntity")
data class MessageEntity (
    @PrimaryKey(autoGenerate = true) val id: Int = 0,

    @ColumnInfo(name = "chat_id") val chatId : Int?,
    @ColumnInfo(name = "sender_id") val senderId : Long?,
    @ColumnInfo(name = "send_time") val sendTime : LocalDateTime?,
    @ColumnInfo(name = "content") val content : String?,
    @ColumnInfo(name = "sender_name") val senderName : String?,

)