package com.ssafy.booking.di

import com.ssafy.data.remote.api.BookSearchApi
import com.ssafy.data.remote.api.ChatApi
import com.ssafy.data.remote.api.MemberApi
import com.ssafy.data.remote.api.MyPageApi
import com.ssafy.data.repository.BookSearchRepositoryImpl
import com.ssafy.data.repository.ChatRepositoryImpl
import com.ssafy.data.repository.GoogleRepositoryImpl
import com.ssafy.data.repository.GoogleDataSourceImpl
import com.ssafy.data.repository.MemberRepositoryImpl
import com.ssafy.data.repository.MyPageRepositoryImpl
import com.ssafy.domain.repository.BookSearchRepository
import com.ssafy.domain.repository.ChatRepository
import com.ssafy.domain.repository.GoogleRepository
import com.ssafy.domain.repository.MemberRepository
import com.ssafy.domain.repository.MyPageRepository
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

    @Provides
    @Singleton
    fun provideMyPageRepository(api: MyPageApi): MyPageRepository {
        return MyPageRepositoryImpl(api)
    }

    @Provides
    @Singleton
    fun provideBookSearchRepository(api: BookSearchApi): BookSearchRepository {
        return BookSearchRepositoryImpl(api)
    }

}