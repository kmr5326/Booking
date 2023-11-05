package com.ssafy.data.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ssafy.data.room.entity.MessageEntity

@Dao
interface MessageDao {
    @Query("SELECT * FROM messageEntity WHERE chat_id = :chatId")
    fun getAll(chatId: Int): LiveData<List<MessageEntity>>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(messageEntity: MessageEntity)
}