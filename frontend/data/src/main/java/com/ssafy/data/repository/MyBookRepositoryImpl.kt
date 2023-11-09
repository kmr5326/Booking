package com.ssafy.data.repository

import com.ssafy.data.remote.api.MyBookApi
import com.ssafy.domain.model.mybook.MyBookListResponse
import com.ssafy.domain.model.mybook.MyBookMemoRegisterRequest
import com.ssafy.domain.model.mybook.MyBookRegisterRequest
import com.ssafy.domain.repository.MyBookRepository
import retrofit2.Response
import javax.inject.Inject

class MyBookRepositoryImpl @Inject constructor(
    private val myBookApi: MyBookApi
) : MyBookRepository {
    override suspend fun postBookRegister(request: MyBookRegisterRequest): Response<Unit> {
        return myBookApi.postBookRegister(request)
    }

    override suspend fun getBookList(nickname: String): Response<List<MyBookListResponse>> {
        return myBookApi.getBookList(nickname)
    }

    override suspend fun getBookDetail(
        nickname: String,
        isbn: String
    ): Response<MyBookListResponse> {
        return myBookApi.getBookDetail(nickname, isbn)
    }

    override suspend fun postBookMemo(request: MyBookMemoRegisterRequest) : Response<Unit> {
        return myBookApi.postBookMemo(request)
    }

    override suspend fun deleteBookRegister(memberBookId: String): Response<Unit> {
        return myBookApi.deleteBookRegister(memberBookId)
    }
}