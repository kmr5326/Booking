package com.ssafy.domain.usecase

import com.ssafy.domain.model.mybook.MyBookListResponse
import com.ssafy.domain.model.mybook.MyBookMemoRegisterRequest
import com.ssafy.domain.model.mybook.MyBookRegisterRequest
import com.ssafy.domain.repository.MyBookRepository
import retrofit2.Response
import javax.inject.Inject

class MyBookUseCase @Inject constructor(
    private val repository: MyBookRepository
) {

    suspend fun postBookRegister(request: MyBookRegisterRequest) : Response<Unit> {
        return repository.postBookRegister(request)
    }

    suspend fun getBookList(nickname: String) : Response<List<MyBookListResponse>> {
        return repository.getBookList(nickname)
    }

    suspend fun getBookDetail(nickname: String, isbn: String) : Response<MyBookListResponse> {
        return repository.getBookDetail(nickname, isbn)
    }

    suspend fun postBookMemo(request: MyBookMemoRegisterRequest) : Response<Unit> {
        return repository.postBookMemo(request)
    }

    suspend fun deleteBookRegister(memberBookId: String) : Response<Unit> {
        return repository.deleteBookRegister(memberBookId)
    }
}