package com.ssafy.domain.model.loacation
import com.google.gson.annotations.SerializedName
data class AddressResponse(
    @SerializedName("meta") val meta: Meta,
    @SerializedName("documents") val documents: List<Document>
)

data class Meta(
    @SerializedName("totalCount") val totalCount: Int
)

data class Document(
    @SerializedName("address") val address: Address,
    @SerializedName("roadAddress") val roadAddress: RoadAddress
)

data class Address(
    @SerializedName("addressName") val addressName: String,
    @SerializedName("region1DepthName") val region1DepthName: String,
    @SerializedName("region2DepthName") val region2DepthName: String,
    @SerializedName("region3DepthName") val region3DepthName: String,
    @SerializedName("mountainYn") val mountainYn: String,
    @SerializedName("mainAddressNo") val mainAddressNo: String,
    @SerializedName("subAddressNo") val subAddressNo: String
)

data class RoadAddress(
    @SerializedName("addressName") val addressName: String,
    @SerializedName("region1DepthName") val region1DepthName: String,
    @SerializedName("region2DepthName") val region2DepthName: String,
    @SerializedName("region3DepthName") val region3DepthName: String,
    @SerializedName("roadName") val roadName: String,
    @SerializedName("undergroundYn") val undergroundYn: String,
    @SerializedName("mainBuildingNo") val mainBuildingNo: String,
    @SerializedName("subBuildingNo") val subBuildingNo: String,
    @SerializedName("buildingName") val buildingName: String,
    @SerializedName("zoneNo") val zoneNo: String
)
