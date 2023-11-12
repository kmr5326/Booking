package com.ssafy.booking.di

import android.content.Context
import coil.ImageLoader
import coil.request.CachePolicy
import coil.util.DebugLogger
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.ssafy.booking.utils.ObjectStorageInterceptor
import com.ssafy.booking.utils.Utils.BASE_URL
import com.ssafy.booking.utils.Utils.NAVER_CLOUD_URL
import com.ssafy.data.remote.api.BookSearchApi
import com.ssafy.data.remote.api.BookingApi
import com.ssafy.data.remote.api.ChatApi
import com.ssafy.data.remote.api.FirebaseApi
import com.ssafy.data.remote.api.GoogleApi
import com.ssafy.data.remote.api.MemberApi
import com.ssafy.data.remote.api.MyBookApi
import com.ssafy.data.remote.api.MyPageApi
import com.ssafy.data.remote.api.NaverCloudApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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
import javax.inject.Named
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
    @Named("defaultRetrofit")
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
            val accessToken = App.prefs.getToken() // ViewModel에서 지정한 key로 JWT 토큰을 가져온다.
            val newRequest = request().newBuilder()
                .addHeader("authorization", "Bearer $accessToken") // 헤더에 authorization라는 key로 JWT 를 넣어준다.
                .build()
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
    fun provideGoogleApiService(@Named("defaultRetrofit") retrofit: Retrofit): GoogleApi {
        return retrofit.create(GoogleApi::class.java)
    }

    @Provides
    @Singleton
    fun provideChatApi(@Named("defaultRetrofit") retrofit: Retrofit): ChatApi {
        return retrofit.create(ChatApi::class.java)
    }

    @Provides
    @Singleton
    fun provideMemberApi(@Named("defaultRetrofit") retrofit: Retrofit): MemberApi {
        return retrofit.create(MemberApi::class.java)
    }

    @Provides
    @Singleton
    fun provideMyPageApi(@Named("defaultRetrofit") retrofit: Retrofit): MyPageApi {
        return retrofit.create(MyPageApi::class.java)
    }

    @Provides
    @Singleton
    fun provideBookSearchApi(@Named("defaultRetrofit") retrofit: Retrofit): BookSearchApi {
        return retrofit.create(BookSearchApi::class.java)
    }

    @Provides
    @Singleton
    fun provideBookingApi(@Named("defaultRetrofit") retrofit: Retrofit): BookingApi {
        return retrofit.create(BookingApi::class.java)
    }

    @Provides
    @Singleton
    fun provideDeviceTokenApi(@Named("defaultRetrofit") retrofit: Retrofit): FirebaseApi {
        return retrofit.create(FirebaseApi::class.java)
    }

    @Provides
    @Singleton
    fun provideMyBookApi(@Named("defaultRetrofit") retrofit: Retrofit) : MyBookApi {
        return retrofit.create(MyBookApi::class.java)
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


    // Naver cloud 활용 network module 부분
    @Provides
    @Singleton
    fun provideObjectStorageInterceptor(): ObjectStorageInterceptor {
        val accessKey = "64tVP74TUGmd6PDzjQ04"
        val secretKey = "1E11TfvJcy7OVnSSm3rV0Vph24CLUO4Tiehd5PtZ"
        val region = "kr-standard"
        return ObjectStorageInterceptor(accessKey, secretKey, region)
    }

    @Provides
    @Singleton
    fun okHttpClientWithObjectStorage(objectStorageInterceptor: ObjectStorageInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .readTimeout(10, TimeUnit.SECONDS)
            .connectTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .addInterceptor(objectStorageInterceptor) // ObjectStorageInterceptor 추가
            .build()
    }

    // 기존 Retrofit 제공 함수를 ObjectStorageInterceptor를 포함하도록 수정
    @Provides
    @Singleton
    @Named("objectStorageRetrofit")
    fun retrofitWithObjectStorage(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(NAVER_CLOUD_URL)
            .client(okHttpClientWithObjectStorage(provideObjectStorageInterceptor()))
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideNaverStorageApi(@Named("objectStorageRetrofit") retrofitWithObjectStorage: Retrofit): NaverCloudApi {
        return retrofitWithObjectStorage.create(NaverCloudApi::class.java)
    }

    @Provides
    @Singleton
    fun provideImageLoader(@ApplicationContext context: Context): ImageLoader {
        val objectStorageInterceptor = provideObjectStorageInterceptor()

        // OkHttp 클라이언트를 구성합니다. 이 때, 로깅 인터셉터와 ObjectStorageInterceptor를 추가합니다.
        val naverOkHttpClient = OkHttpClient.Builder()
            .addInterceptor(objectStorageInterceptor)
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()

        return ImageLoader.Builder(context)
            .logger(DebugLogger())
            .diskCachePolicy(CachePolicy.DISABLED)
            .okHttpClient(naverOkHttpClient)
            .build()
    }

}
