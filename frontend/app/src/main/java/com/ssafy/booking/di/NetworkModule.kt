package com.ssafy.booking.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.ssafy.booking.utils.Utils.BASE_URL
import com.ssafy.data.remote.api.ChatListApi
import com.ssafy.data.remote.api.GoogleApi
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
//        val hostname = "k9c206.p.ssafy.io" // 변경해야 합니다. 서버의 호스트 이름을 사용하세요.
//        val certificatePinner = CertificatePinner.Builder()
//            .add(hostname, "sha256/IDF9aOeOleD2EgHReEz2+dm0In+48lIxbdCiESFCRSo=") // 앞서 얻은 해시 값을 여기에 넣으세요.
//            .build()
//
//        return OkHttpClient.Builder()
//            .readTimeout(10, TimeUnit.SECONDS)
//            .connectTimeout(10, TimeUnit.SECONDS)
//            .writeTimeout(15, TimeUnit.SECONDS)
//            .addInterceptor(getLoggingInterceptor())
//            .certificatePinner(certificatePinner)
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

    private fun getLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
}