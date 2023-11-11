package com.ssafy.data.remote.api
import com.ssafy.domain.model.loacation.AddressResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

interface LocationApi {
    // 위도,경도를 주소로 변환
    @Headers("Content-Type: application/json;charset=UTF-8")
    @GET("https://dapi.kakao.com/v2/local/geo/coord2address.json?x={lng}&y={lat}")
    suspend fun getAddress(@Path("lng") lng: String,@Path("lat") lat:String): Response<AddressResponse>
}