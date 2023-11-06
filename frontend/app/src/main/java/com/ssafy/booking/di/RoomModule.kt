package com.ssafy.booking.di

import android.content.Context
import androidx.room.Room
import com.ssafy.data.room.dao.MessageDao
import com.ssafy.data.room.database.MessageDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): MessageDatabase {
        return Room.databaseBuilder(
            appContext,
            MessageDatabase::class.java,
            "message_database"
        ).build()
    }

    @Provides
    fun provideMessageDao(database: MessageDatabase): MessageDao {
        return database.messageDao()
    }
}
