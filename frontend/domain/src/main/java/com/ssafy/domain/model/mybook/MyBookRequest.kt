package com.ssafy.domain.model.mybook


data class MyBookRegisterRequest (
    val nickname: String,
    val bookIsbn : String,
//    val memo : String,
)

data class MyBookMemoRegisterRequest (
    val nickname: String,
    val isbn: String,
    val content: String
)
