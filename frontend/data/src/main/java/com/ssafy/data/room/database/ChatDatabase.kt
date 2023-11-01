package com.ssafy.data.room.database

import androidx.room.Database
import com.ssafy.data.room.dao.ChatDao
import com.ssafy.data.room.entity.ChatEntity

@Database(entities = [ChatEntity::class], version = 1)
abstract class ChatDatabase {
    abstract fun chatDao(): ChatDao
}