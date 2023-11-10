package com.ssafy.data.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ssafy.data.room.entity.MessageEntity

@Dao
interface MessageDao {
    @Query("SELECT * FROM messageEntity WHERE chatroom_id = :chatroomId ORDER BY time_stamp")
    fun getLatestMessage(chatroomId: Int): LiveData<List<MessageEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(vararg messages: MessageEntity)

    @Query("SELECT * FROM messageEntity WHERE chatroom_id = :chatroomId AND message_id = :messageId")
    fun getMessageByChatIdAndMessageId(chatroomId: Int?, messageId: Int?): MessageEntity?
}