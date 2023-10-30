package com.ssafy.booking.di

import com.ssafy.data.repository.GoogleRepositoryImpl
import com.ssafy.data.repository.GoogleDataSourceImpl
import com.ssafy.domain.repository.GoogleRepository
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
}