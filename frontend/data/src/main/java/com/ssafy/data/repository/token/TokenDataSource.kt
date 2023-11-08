package com.ssafy.data.repository.token

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.ssafy.domain.model.google.AccountInfo
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class TokenDataSource @Inject constructor(
    @ApplicationContext context: Context,
) {
    companion object {
        private const val ACCESS_TOKEN = "access_token"
        private const val LOGIN_ID = "login_id"
        private const val NICKNAME = "nickname"
        private const val PROFILE_IMAGE = "profile_image"
        private const val LAT = "lat"
        private const val LGT = "lgt"
        private const val MEMBER_PK = "member_pk"

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

    private fun putFloat(key: String, data: Float) {
        editor.putFloat(key, data)
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

    private fun getFloat(key: String, defValue: Float = 0f) : Float {
        return prefs.getFloat(key, defValue)
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

    //
    fun putNickName(nickname: String?) {
        putString(NICKNAME, nickname)
    }

    fun getNickName() : String? {
        return getString(NICKNAME)
    }

    fun removeNickName() {
        putString(NICKNAME, null)
    }

    //

    fun putProfileImage(profileImage: String?) {
        putString(PROFILE_IMAGE, profileImage)
    }

    fun getProfileImage() : String? {
        return getString(PROFILE_IMAGE)
    }

    fun removeProfileImage() {
        putString(PROFILE_IMAGE, null)
    }


    // 위도,경도,멤버pk

//    fun putLat(lat: Float) {
//        putFloat(LAT, lat)
//    }
//
//    fun getLat() : Float {
//        return getFloat(LAT)
//    }
//
//    fun removeLat() {
//        putFloat(LAT, 0f)
//    }
//
//    //
//
//    fun putLgt(lgt: Float) {
//        putFloat(LGT, lgt)
//    }
//
//    fun getLgt() : Float {
//        return getFloat(LGT)
//    }
//
//    fun removeLgt() {
//        putFloat(LGT, 0f)
//    }
//
//    //
//
//    fun putMemberPk(memberPk: Int) {
//        putInt(MEMBER_PK, memberPk)
//    }
//
//    fun getMemberPk() : Int {
//        return getInt(MEMBER_PK)
//    }
//
//    fun removeMemberPk() {
//        putInt(MEMBER_PK, 0)
//    }

}