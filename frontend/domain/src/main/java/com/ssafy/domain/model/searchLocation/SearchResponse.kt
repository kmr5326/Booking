package com.ssafy.domain.model.searchLocation

data class SearchResponse(
    val items : List<SearchItem>
)

data class SearchItem(
    val title : String,
    val link : String,
    val category : String,
    val description : String,
    val telephone : String,
    val address : String,
    val roadAddress : String,
    val mapx : String,
    val mapy : String
)