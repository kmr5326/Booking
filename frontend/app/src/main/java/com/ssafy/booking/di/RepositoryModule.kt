package com.ssafy.booking.di

import com.ssafy.data.remote.api.ChatListApi
import com.ssafy.data.repository.ChatRepositoryImpl
import com.ssafy.data.repository.GoogleRepositoryImpl
import com.ssafy.data.repository.GoogleDataSourceImpl
import com.ssafy.domain.repository.ChatRepository
import com.ssafy.domain.repository.GoogleRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Provides
    @Singleton
    fun provideMainRepository(
        googleDataSourceImpl : GoogleDataSourceImpl
    ) : GoogleRepository {
        return GoogleRepositoryImpl(
            googleDataSourceImpl
        )
    }

    @Provides
    @Singleton
    fun provideChatRepository(
        chatListApi: ChatListApi
    ): ChatRepository {
        return ChatRepositoryImpl(
            chatListApi
        )
    }
}