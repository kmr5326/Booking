package com.ssafy.booking.di

import com.ssafy.domain.repository.GoogleRepository
import com.ssafy.domain.repository.MemberRepository
import com.ssafy.domain.repository.PostChatCreateRepository
import com.ssafy.domain.repository.PostChatExitRepository
import com.ssafy.domain.repository.PostChatJoinRepository
import com.ssafy.domain.usecase.chat.ChatCreateUseCase
import com.ssafy.domain.usecase.GetTokenRepoUseCase
import com.ssafy.domain.usecase.SignInUseCase
import com.ssafy.domain.usecase.chat.ChatExitUseCase
import com.ssafy.domain.usecase.chat.ChatJoinUseCase
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
    fun provideGetTokenRepoUseCase(repository : GoogleRepository) = GetTokenRepoUseCase(repository)

    @Provides
    @Singleton
    fun provideSignInUseCase(repository : MemberRepository) = SignInUseCase(repository)

    @Provides
    @Singleton
    fun providePostChatCreateUseCase(repository: PostChatCreateRepository) = ChatCreateUseCase(repository)

    @Provides
    @Singleton
    fun providePostChatJoinUseCase(repository: PostChatJoinRepository) = ChatJoinUseCase(repository)

    @Provides
    @Singleton
    fun providePostChatExitUseCase(repository: PostChatExitRepository) = ChatExitUseCase(repository)
}