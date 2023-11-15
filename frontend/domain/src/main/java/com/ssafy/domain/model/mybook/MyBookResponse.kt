package com.ssafy.domain.model.mybook

import com.ssafy.domain.model.booksearch.BookSearchResponse


data class Notes (
    val memo : String,
    val createdAt : String
)

data class MyBookListResponse (
    val memberBookId : String,
    val memberNickname : String,
    val bookInfo : BookSearchResponse,
    val notes : List<Notes>?,
    val createdAt: String
)