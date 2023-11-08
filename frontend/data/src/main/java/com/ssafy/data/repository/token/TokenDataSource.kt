package com.ssafy.data.repository.token

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.ssafy.domain.model.DeviceToken
import com.ssafy.domain.model.google.AccountInfo
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class TokenDataSource @Inject constructor(
    @ApplicationContext context: Context,
) {
    companion object {
        private const val ACCESS_TOKEN = "access_token"
        private const val LOGIN_ID = "login_id"
        private const val DEVICE_TOKEN = "device_token"
    }
    private fun getTokenPreference(context: Context) : SharedPreferences {
        return context.getSharedPreferences(ACCESS_TOKEN, Context.MODE_PRIVATE)
    }
    private val prefs by lazy { getTokenPreference(context) }
    private val editor by lazy { prefs.edit() }
    private val gson = Gson()

    private fun putString(key: String, data: String?) {
        editor.putString(key, data)
        editor.apply()
    }

    private fun putBoolean(key: String, data: Boolean) {
        editor.putBoolean(key, data)
        editor.apply()
    }

    private fun putInt(key: String, data: Int) {
        editor.putInt(key, data)
        editor.apply()
    }

    private fun getString(key: String, defValue: String? = null) : String? {
        return prefs.getString(key, defValue)
    }

    private fun getBoolean(key: String, defValue: Boolean = false) : Boolean {
        return prefs.getBoolean(key, defValue)
    }

    private fun getInt(key: String, defValue: Int = 0) : Int {
        return prefs.getInt(key, defValue)
    }

    // 토큰 부분
    fun putToken(token: String?) {
        putString(ACCESS_TOKEN, token)
    }
    fun getToken(): String? {
        return getString(ACCESS_TOKEN)
    }
    fun removeToken() {
        putString(ACCESS_TOKEN, null)
    }

    fun putLoginId(loginId: String?) {
        putString(LOGIN_ID, loginId)
    }

    fun getLoginId() : String? {
        return getString(LOGIN_ID)
    }

    fun removeLoginId() {
        putString(LOGIN_ID, null)
    }

    fun putDeviceToken(s: String) {
        putString(DEVICE_TOKEN, s)
    }
    fun getDeviceToken() : String? {
        return getString(DEVICE_TOKEN)
    }
    fun removeDeviceToken() {
        return putString(DEVICE_TOKEN, null)
    }

}