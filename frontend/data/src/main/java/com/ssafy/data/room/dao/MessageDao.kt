package com.ssafy.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.ssafy.data.room.entity.MessageEntity

@Dao
interface MessageDao {
    @Query("SELECT * FROM messageEntity")
    fun getAll() : List<MessageEntity>

    @Insert
    fun insert(messageEntity: MessageEntity)
}