package com.ssafy.data.room.database

import androidx.room.Database
import com.ssafy.data.room.dao.MessageDao
import com.ssafy.data.room.entity.MessageEntity

@Database(entities = [MessageEntity::class], version = 1)
abstract class MessageDatabase {
    abstract fun messageDao(): MessageDao
}