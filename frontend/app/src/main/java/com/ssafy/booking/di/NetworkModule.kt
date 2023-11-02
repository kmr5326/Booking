package com.ssafy.booking.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.ssafy.booking.utils.Utils.BASE_URL
import com.ssafy.data.remote.api.ChatListApi
import com.ssafy.data.remote.api.GoogleApi
import com.ssafy.data.remote.api.MemberApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.CertificatePinner
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

//    @Provides
//    @Singleton
//    fun provideHttpClient() : OkHttpClient {
//        return OkHttpClient.Builder()
//            .readTimeout(10, TimeUnit.SECONDS)
//            .connectTimeout(10, TimeUnit.SECONDS)
//            .writeTimeout(15, TimeUnit.SECONDS)
//            .addInterceptor(getLoggingInterceptor())
//            .build()
//    }

    @Provides
    @Singleton
    fun provideRetrofitInstance(
        okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
//            .client(provideHttpClient())
            .addConverterFactory(gsonConverterFactory)
            .build()
    }


    @Provides
    @Singleton
    fun provideConverterFactory() : GsonConverterFactory {
        val gson : Gson = GsonBuilder()
            .setLenient()
            .create()

        return GsonConverterFactory.create(gson)
    }

    @Provides
    @Singleton
    fun provideGoogleApiService(retrofit: Retrofit) : GoogleApi {
        return retrofit.create(GoogleApi::class.java)
    }

    @Provides
    @Singleton
    fun provideChatListApi(retrofit: Retrofit): ChatListApi {
        return retrofit.create(ChatListApi::class.java)
    }

    @Provides
    @Singleton
    fun provideMemberApi(retrofit: Retrofit): MemberApi {
        return retrofit.create(MemberApi::class.java)
    }


    private fun getLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
}