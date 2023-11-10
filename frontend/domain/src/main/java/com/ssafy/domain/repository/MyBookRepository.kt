package com.ssafy.domain.repository

import com.ssafy.domain.model.mybook.MyBookListResponse
import com.ssafy.domain.model.mybook.MyBookMemoRegisterRequest
import com.ssafy.domain.model.mybook.MyBookRegisterRequest
import retrofit2.Response


interface MyBookRepository {

    suspend fun postBookRegister(request: MyBookRegisterRequest) : Response<Unit>

    suspend fun getBookList(nickname : String) : Response<List<MyBookListResponse>>

    suspend fun getBookDetail(nickname: String, isbn: String) : Response<MyBookListResponse>

    suspend fun postBookMemo(request: MyBookMemoRegisterRequest) : Response<Unit>

    suspend fun deleteBookRegister(memberBookId: String) : Response<Unit>
}