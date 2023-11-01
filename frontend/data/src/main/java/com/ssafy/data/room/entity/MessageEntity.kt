package com.ssafy.data.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Timestamp

@Entity(tableName = "messageEntity")
data class MessageEntity (
    @PrimaryKey val id: Int,

    @ColumnInfo(name = "chat_id") val chatId : Int?,
    @ColumnInfo(name = "sender_id") val senderId : Int?,
    @ColumnInfo(name = "send_time") val sendTime : Timestamp,
    @ColumnInfo(name = "content") val content : String?
)