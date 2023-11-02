package com.ssafy.booking.di

import com.ssafy.domain.repository.GoogleRepository
import com.ssafy.domain.repository.MemberRepository
import com.ssafy.domain.usecase.GetTokenRepoUseCase
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
    fun provideGetTokenRepoUseCase(repository : GoogleRepository) = GetTokenRepoUseCase(repository)

    @Provides
    @Singleton
    fun provideSignInUseCase(repository : MemberRepository) = SignInUseCase(repository)
}