package com.ssafy.data.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chatEntity")
data class ChatEntity(
    @PrimaryKey val id: Int,

    @ColumnInfo(name = "meetings_id") val meetingsId : Int?,
    @ColumnInfo(name = "member_list") val memberList : List<Int>?
)