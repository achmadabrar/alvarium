package com.bs.ecommerce.account.rewardpoint.model

import com.bs.ecommerce.networking.common.RequestCompleteListener
import com.bs.ecommerce.account.rewardpoint.model.data.RewardPointResponse

interface RewardPointModel {

    fun fetchRewardPoints(
        page: Int,
        callback: RequestCompleteListener<RewardPointResponse>
    )
}