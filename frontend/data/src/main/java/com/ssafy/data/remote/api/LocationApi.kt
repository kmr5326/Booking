package com.ssafy.data.remote.api
import com.ssafy.domain.model.loacation.AddressResponse
import com.ssafy.domain.model.loacation.KakaoSearchResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface LocationApi {
    // 위도,경도를 주소로 변환

//    @Headers("Content-Type: application/json;charset=UTF-8",
@Headers("Content-Type: application/json;charset=UTF-8","Authorization:KakaoAK f166b3e6f0c90bc4bbd204d02b311184")
    @GET("https://dapi.kakao.com/v2/local/geo/coord2address")
    suspend fun getAddress(@Query("x") lng: String,@Query("y") lat:String): Response<AddressResponse>

    @Headers("Content-Type: application/json;charset=UTF-8","Authorization:KakaoAK f166b3e6f0c90bc4bbd204d02b311184")
    @GET("https://dapi.kakao.com/v2/local/search/keyword")
    suspend fun getSearchList(@Query("query") query:String,@Query("page") page:Int,@Query("size") size:Int,@Query("y") y:String,@Query("x") x:String,@Query("radius") radius:Int): Response<KakaoSearchResponse>

}