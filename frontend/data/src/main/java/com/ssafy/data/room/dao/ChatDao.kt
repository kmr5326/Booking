package com.ssafy.data.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.ssafy.data.room.entity.ChatEntity

@Dao
interface ChatDao {
    @Query("SELECT * FROM chatEntity")
    fun getAll() : List<ChatEntity>

    @Insert
    fun insert(chatEntity: ChatEntity)

    @Delete
    fun delete(chatEntity: ChatEntity)
}