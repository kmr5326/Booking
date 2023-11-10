package com.ssafy.booking.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.booking.model.MyBookState
import com.ssafy.domain.model.mybook.MyBookListResponse
import com.ssafy.domain.model.mybook.MyBookMemoRegisterRequest
import com.ssafy.domain.model.mybook.MyBookRegisterRequest
import com.ssafy.domain.usecase.MyBookUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject


@HiltViewModel
class MyBookViewModel @Inject constructor(
    private val myBookUseCase: MyBookUseCase
) : ViewModel() {

    private val _myBookState = MutableLiveData<MyBookState>()
    val myBookState : LiveData<MyBookState> get() = _myBookState

    private val _myBookResponse = MutableLiveData<Response<List<MyBookListResponse>>>()
    val myBookResponse : LiveData<Response<List<MyBookListResponse>>> get() = _myBookResponse

    fun getMyBookResponse(nickname : String) =
        viewModelScope.launch {
            _myBookState.value = MyBookState.Loading
            try {
                val myBookRes = myBookUseCase.getBookList(nickname)
                if (myBookRes.isSuccessful && myBookRes.body() != null) {
                    _myBookState.value = MyBookState.Success(myBookRes.body()!!)
                }
            } catch (e: Exception) {
                _myBookState.value = MyBookState.Error(e.message ?: "알 수 없는 에러가 발생했습니다.")
            }
        }
    // 내 서재 상세 조회
    private val _myBookDetailResponse = MutableLiveData<Response<MyBookListResponse>>()
    val myBookDetailResponse : LiveData<Response<MyBookListResponse>> get() = _myBookDetailResponse
    fun getMyBookDetailResponse(nickname: String, isbn: String) =
        viewModelScope.launch {
            _myBookDetailResponse.value = myBookUseCase.getBookDetail(nickname,isbn)
        }

    private val _postBookRegisterResult = MutableLiveData<Response<Unit>>()
    val postBookRegisterResult: LiveData<Response<Unit>> get() = _postBookRegisterResult

    fun postBookRegister(request: MyBookRegisterRequest) =
        viewModelScope.launch {
            _postBookRegisterResult.value = myBookUseCase.postBookRegister(request)
        }

    // 메모 요청 날리기
    private val _postBookMemoResult = MutableLiveData<Response<Unit>>()
    val postBookMemoResult : LiveData<Response<Unit>> get() = _postBookMemoResult

    fun postBookMemo(request: MyBookMemoRegisterRequest) =
        viewModelScope.launch {
            _postBookMemoResult.value = myBookUseCase.postBookMemo(request)
        }

    private val _deleteBookRegisterResult = MutableLiveData<Response<Unit>>()
    val deleteBookRegisterResult : LiveData<Response<Unit>> get() = _deleteBookRegisterResult

    fun deleteBookRegister(memberBookId: String) =
        viewModelScope.launch {
            _deleteBookRegisterResult.value = myBookUseCase.deleteBookRegister(memberBookId)
        }
 }