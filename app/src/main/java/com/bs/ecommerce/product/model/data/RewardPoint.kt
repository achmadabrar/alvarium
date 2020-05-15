package com.bs.ecommerce.product.model.data


import com.google.gson.annotations.SerializedName

data class RewardPoint(
    @SerializedName("CreatedOn")
    val createdOn: String?,
    @SerializedName("CustomProperties")
    val customProperties: CustomProperties?,
    @SerializedName("EndDate")
    val endDate: String?,
    @SerializedName("Id")
    val id: Long?,
    @SerializedName("Message")
    val message: String?,
    @SerializedName("Points")
    val points: Double?,
    @SerializedName("PointsBalance")
    val pointsBalance: String?
)