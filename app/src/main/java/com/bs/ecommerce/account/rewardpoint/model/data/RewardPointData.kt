package com.bs.ecommerce.account.rewardpoint.model.data


import com.bs.ecommerce.catalog.common.CustomProperties
import com.bs.ecommerce.catalog.common.PagerModel
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