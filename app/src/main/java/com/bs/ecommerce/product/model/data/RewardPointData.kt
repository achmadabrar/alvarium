package com.bs.ecommerce.product.model.data


import com.google.gson.annotations.SerializedName

data class RewardPointData(
    @SerializedName("CustomProperties")
    val customProperties: CustomProperties?,
    @SerializedName("MinimumRewardPointsAmount")
    val minimumRewardPointsAmount: String?,
    @SerializedName("MinimumRewardPointsBalance")
    val minimumRewardPointsBalance: Double?,
    @SerializedName("PagerModel")
    val pagerModel: PagerModel?,
    @SerializedName("RewardPoints")
    val rewardPoints: List<RewardPoint>?,
    @SerializedName("RewardPointsAmount")
    val rewardPointsAmount: String?,
    @SerializedName("RewardPointsBalance")
    val rewardPointsBalance: Double?
)