package com.ssafy.booking.di

import com.ssafy.data.remote.api.GetChatListApi
import com.ssafy.data.remote.api.MemberApi
import com.ssafy.data.remote.api.PostChatCreateApi
import com.ssafy.data.repository.GetChatRepositoryImpl
import com.ssafy.data.repository.GoogleRepositoryImpl
import com.ssafy.data.repository.GoogleDataSourceImpl
import com.ssafy.data.repository.MemberRepositoryImpl
import com.ssafy.data.repository.PostChatCreateRepositoryImpl
import com.ssafy.domain.repository.GetChatListRepository
import com.ssafy.domain.repository.GoogleRepository
import com.ssafy.domain.repository.MemberRepository
import com.ssafy.domain.repository.PostChatCreateRepository
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
    fun provideGetChatListRepository(api: GetChatListApi): GetChatListRepository {
        return GetChatRepositoryImpl(api)
    }

    @Provides
    @Singleton
    fun providePostChatCreateRepository(api: PostChatCreateApi): PostChatCreateRepository {
        return PostChatCreateRepositoryImpl(api)
    }
}