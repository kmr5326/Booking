package com.ssafy.booking.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.ssafy.booking.utils.Utils.BASE_URL
import com.ssafy.data.remote.api.BookSearchApi
import com.ssafy.data.remote.api.BookingApi
import com.ssafy.data.remote.api.ChatApi
import com.ssafy.data.remote.api.FirebaseApi
import com.ssafy.data.remote.api.GoogleApi
import com.ssafy.data.remote.api.LocationApi
import com.ssafy.data.remote.api.MemberApi
import com.ssafy.data.remote.api.MyBookApi
import com.ssafy.data.remote.api.MyPageApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    @Singleton
    fun okHttpClient(interceptor: AppInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .readTimeout(10, TimeUnit.SECONDS)
            .connectTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .addInterceptor(interceptor)
            .addInterceptor(getLoggingInterceptor())
            .addInterceptor(
                HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                }
            )
            .build()
    }

    @Provides
    @Singleton
    fun retrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient(AppInterceptor()))
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    class AppInterceptor : Interceptor {
        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): Response = with(chain) {
            val request = request()
            val newRequestBuilder = request.newBuilder()

            // 카카오 API 요청인 경우, authorization 헤더를 추가하지 않음
            if (!request.url.host.contains("dapi.kakao.com")) {
                val accessToken = App.prefs.getToken() // JWT 토큰을 가져온다
                newRequestBuilder.addHeader("authorization", "Bearer $accessToken") // 헤더에 JWT 추가
            }

            val newRequest = newRequestBuilder.build()
            proceed(newRequest)
        }
    }

    @Provides
    @Singleton
    fun provideConverterFactory(): GsonConverterFactory {
        val gson: Gson = GsonBuilder()
            .setLenient()
            .create()

        return GsonConverterFactory.create(gson)
    }

    @Provides
    @Singleton
    fun provideGoogleApiService(retrofit: Retrofit): GoogleApi {
        return retrofit.create(GoogleApi::class.java)
    }

    @Provides
    @Singleton
    fun provideChatApi(retrofit: Retrofit): ChatApi {
        return retrofit.create(ChatApi::class.java)
    }

    @Provides
    @Singleton
    fun provideMemberApi(retrofit: Retrofit): MemberApi {
        return retrofit.create(MemberApi::class.java)
    }

    @Provides
    @Singleton
    fun provideMyPageApi(retrofit: Retrofit): MyPageApi {
        return retrofit.create(MyPageApi::class.java)
    }

    @Provides
    @Singleton
    fun provideBookSearchApi(retrofit: Retrofit): BookSearchApi {
        return retrofit.create(BookSearchApi::class.java)
    }

    @Provides
    @Singleton
    fun provideBookingApi(retrofit: Retrofit): BookingApi {
        return retrofit.create(BookingApi::class.java)
    }

    @Provides
    @Singleton
    fun provideDeviceTokenApi(retrofit: Retrofit): FirebaseApi {
        return retrofit.create(FirebaseApi::class.java)
    }

    @Provides
    @Singleton
    fun provideMyBookApi(retrofit: Retrofit) : MyBookApi {
        return retrofit.create(MyBookApi::class.java)
    }

    @Provides
    @Singleton
    fun provideLocationApi(retrofit: Retrofit) : LocationApi {
        return retrofit.create(LocationApi::class.java)
    }

//    @Provides
//    @Singleton
//    fun providePostChatCreateApi(retrofit: Retrofit): PostChatCreateApi {
//        return retrofit.create(PostChatCreateApi::class.java)
//    }
//
//    @Provides
//    @Singleton
//    fun providePostChatJoinApi(retrofit: Retrofit): PostChatJoinApi {
//        return retrofit.create(PostChatJoinApi::class.java)
//    }
//
//    @Provides
//    @Singleton
//    fun providePostChatExitApi(retrofit: Retrofit): PostChatExitApi {
//        return retrofit.create(PostChatExitApi::class.java)
//    }

    private fun getLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
}
