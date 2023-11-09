package com.ssafy.booking.di

import com.ssafy.domain.repository.BookSearchRepository
import com.ssafy.domain.repository.BookingRepository
import com.ssafy.domain.repository.ChatRepository
import com.ssafy.domain.repository.GoogleRepository
import com.ssafy.domain.repository.MemberRepository
import com.ssafy.domain.repository.MyBookRepository
import com.ssafy.domain.repository.MyPageRepository
import com.ssafy.domain.usecase.BookSearchUseCase
import com.ssafy.domain.usecase.BookingUseCase
import com.ssafy.domain.usecase.ChatUseCase
import com.ssafy.domain.usecase.GetTokenRepoUseCase
import com.ssafy.domain.usecase.MyBookUseCase
import com.ssafy.domain.usecase.MyPageUseCase
import com.ssafy.domain.usecase.SignInUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class UseCaseModule {

    @Provides
    @Singleton
    fun provideGetTokenRepoUseCase(repository: GoogleRepository) = GetTokenRepoUseCase(repository)

    @Provides
    @Singleton
    fun provideSignInUseCase(repository: MemberRepository) = SignInUseCase(repository)

    @Provides
    @Singleton
    fun provideChatUseCase(repository: ChatRepository) = ChatUseCase(repository)

    @Provides
    @Singleton
    fun provideMyPageUseCase(repository: MyPageRepository) = MyPageUseCase(repository)

    @Provides
    @Singleton
    fun provideBookSearchUseCase(repository: BookSearchRepository) = BookSearchUseCase(repository)

    @Provides
    @Singleton
    fun provideBookingUseCase(repository: BookingRepository) = BookingUseCase(repository)

    @Provides
    @Singleton
    fun provideMyBookUseCase(repository: MyBookRepository) = MyBookUseCase(repository)
}
