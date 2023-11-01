package com.ssafy.booking.di

import android.app.Application
import com.kakao.sdk.common.KakaoSdk
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class App : Application() {
    companion object {
        private lateinit var application: App
        fun getInstance() : App = application
    }

    override fun onCreate() {
        super.onCreate()
        KakaoSdk.init(this, "c983af9ff87c243a4acecc793d087699")
        application = this
    }
}