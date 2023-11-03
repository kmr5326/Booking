package com.ssafy.booking.di

import com.ssafy.data.remote.api.ChatApi
import com.ssafy.data.remote.api.MemberApi
import com.ssafy.data.repository.ChatRepositoryImpl
import com.ssafy.data.repository.GoogleRepositoryImpl
import com.ssafy.data.repository.GoogleDataSourceImpl
import com.ssafy.data.repository.MemberRepositoryImpl
import com.ssafy.domain.repository.ChatRepository
import com.ssafy.domain.repository.GoogleRepository
import com.ssafy.domain.repository.MemberRepository
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
    fun provideMemberRepository(api: MemberApi): MemberRepository {
        return MemberRepositoryImpl(api)
    }

    @Provides
    @Singleton
    fun provideChatRepository(api: ChatApi): ChatRepository {
        return ChatRepositoryImpl(api)
    }

}