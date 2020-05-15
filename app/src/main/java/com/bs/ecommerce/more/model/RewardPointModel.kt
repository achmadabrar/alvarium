package com.bs.ecommerce.more.model

import com.bs.ecommerce.common.RequestCompleteListener
import com.bs.ecommerce.product.model.data.RewardPointResponse

interface RewardPointModel {

    fun fetchRewardPoints(
        page: Int,
        callback: RequestCompleteListener<RewardPointResponse>
    )
}