package com.ssafy.data.repository.token

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.ssafy.domain.model.DeviceToken
import com.ssafy.domain.model.booking.HashtagResponse
import com.ssafy.domain.model.google.AccountInfo
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.internal.cache2.Relay.Companion.edit
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
        private const val TITLE = "title"
        private const val DESCRIPTION = "description"
        private const val BOOK_IMAGE = "book_image"
        private const val DEVICE_TOKEN = "device_token"
        private const val BOOK_TITLE = "book_title"
        private const val BOOK_AUTHOR = "book_author"
        private const val MEETING_ID = "meeting_id"
        private const val MAX_PARTICIPANTS = "max_participants"
        private const val HASHTAG_LIST = "hashtag_list"
        private const val MEETING_LGT = "meeting_lgt"
        private const val MEETING_LAT = "meeting_lat"
        private const val MEETING_ADDRESS = "meeting_address"
        private const val MEETING_LOCATION = "meeting_location"
        private const val MEETING_LEADERID = "meeting_leaderId"

    }
    private fun getTokenPreference(context: Context) : SharedPreferences {
        return context.getSharedPreferences(ACCESS_TOKEN, Context.MODE_PRIVATE)
    }
    private val prefs by lazy { getTokenPreference(context) }
    private val editor by lazy { prefs.edit() }
    private val gson = Gson()

    fun putString(key: String, data: String?) {
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

    private fun putLong(key: String, data: Long) {
        editor.putLong(key, data)
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

    private fun getLong(key: String, defValue: Long = 0) : Long {
        return prefs.getLong(key, defValue)
    }

    private fun getObject(key: String, defValue: Any) : Any {
        val json = getString(key, null)
        return if (json == null) {
            defValue
        } else {
            gson.fromJson(json, defValue::class.java)
        }
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

    fun putLat(lat: Float) {
        putFloat(LAT, lat)
    }

    fun getLat() : Float {
        return getFloat(LAT)
    }

    fun removeLat() {
        putFloat(LAT, 0f)
    }

    //

    fun putLgt(lgt: Float) {
        putFloat(LGT, lgt)
    }

    fun getLgt() : Float {
        return getFloat(LGT)
    }

    fun removeLgt() {
        putFloat(LGT, 0f)
    }
//
//    //
//
    fun putMemberPk(memberPk: Long) {
        putLong(MEMBER_PK, memberPk)
    }

    fun getMemberPk() : Long {
        return getLong(MEMBER_PK)
    }

    fun removeMemberPk() {
        putInt(MEMBER_PK, 0)
    }

    fun getTitle() : String? {
        return getString(TITLE)
    }
    fun putTitle(title: String?) {
        putString(TITLE, title)
    }
    fun removeTitle() {
        putString(TITLE, null)
    }
    fun getDescription() : String? {
        return getString(DESCRIPTION)
    }
    fun putDescription(description: String?) {
        putString(DESCRIPTION, description)
    }
    fun removeDescription() {
        putString(DESCRIPTION, null)
    }
    fun getBookImage() : String? {
        return getString(BOOK_IMAGE)
    }
    fun putBookImage(bookImage: String?) {
        putString(BOOK_IMAGE, bookImage)
    }
    fun removeBookImage() {
        putString(BOOK_IMAGE, null)
    }

    fun getBookTitle() : String? {
        return getString(BOOK_TITLE)
    }
    fun putBookTitle(bookTitle: String?) {
        putString(BOOK_TITLE, bookTitle)
    }
    fun removeBookTitle() {
        putString(BOOK_TITLE, null)
    }
    fun getBookAuthor() : String? {
        return getString(BOOK_AUTHOR)
    }
    fun putBookAuthor(bookAuthor: String?) {
        putString(BOOK_AUTHOR, bookAuthor)
    }
    fun removeBookAuthor() {
        putString(BOOK_AUTHOR, null)
    }

    fun getMeetingId() : Long? {
        return getLong(MEETING_ID)
    }
    fun putMeetingId(meetingId: Long?) {
        putLong(MEETING_ID, meetingId ?: 0L)
    }
    fun removeMeetingId() {
        putLong(MEETING_ID, 0L)
    }

    fun getMaxParticipants() : Int? {
        return getInt(MAX_PARTICIPANTS)
    }
    fun putMaxParticipants(maxParticipants: Int?) {
        putInt(MAX_PARTICIPANTS, maxParticipants ?: 1)
    }
    fun removeMaxParticipants() {
        putInt(MAX_PARTICIPANTS, 1)
    }

    // 리스트를 JSON 문자열로 변환하여 저장하는 함수
    fun putHashtagList(list: List<HashtagResponse>?) {
        val gson = Gson()
        val json = gson.toJson(list)
        putString("hashtagList", json)
    }

    fun putMeetingLgt(lgt: Double) {
        putLong(MEETING_LGT, lgt.toLong())
    }
    fun getMeetingLgt() : Double? {
        return getLong(MEETING_LGT).toDouble()
    }
    fun removeMeetingLgt() {
        putLong(MEETING_LGT, 0L)
    }
    fun putMeetingLat(lat: Double) {
        putLong(MEETING_LAT, lat.toLong())
    }
    fun getMeetingLat() : Double? {
        return getLong(MEETING_LAT).toDouble()
    }
    fun removeMeetingLat() {
        putLong(MEETING_LAT, 0L)
    }
    fun putMeetingAddress(address: String?) {
        putString(MEETING_ADDRESS, address)
    }
    fun getMeetingAddress() : String? {
        return getString(MEETING_ADDRESS)
    }
    fun removeMeetingAddress() {
        putString(MEETING_ADDRESS, null)
    }
    fun putMeetingLocation(location: String?) {
        putString(MEETING_LOCATION, location)
    }
    fun getMeetingLocation() : String? {
        return getString(MEETING_LOCATION)
    }
    fun removeMeetingLocation() {
        putString(MEETING_LOCATION, null)
    }
    fun putLeaderId(leaderId: Int?) {
        if(leaderId != null)
        putInt(MEETING_LEADERID, leaderId)
    }

    fun getLeaderId() : Int? {
        return getInt(MEETING_LEADERID)
    }


    // 해시태그 리스트
    // 리스트를 JSON 문자열로 변환하여 저장하는 함수
// SharedPreferences에서 리스트를 읽는 함수
    fun getHashtagList(): List<HashtagResponse> {
        val gson = Gson()
        val json = getString("hashtagList", "")
        val type = object : TypeToken<List<HashtagResponse>>() {}.type
        return gson.fromJson(json, type) ?: emptyList()
    }
}