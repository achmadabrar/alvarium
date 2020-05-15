package com.bs.ecommerce.more.model

import com.bs.ecommerce.common.RequestCompleteListener
import com.bs.ecommerce.networking.RetroClient
import com.bs.ecommerce.product.model.data.RewardPointResponse
import com.bs.ecommerce.utils.TextUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RewardPointModelImpl: RewardPointModel {

    override fun fetchRewardPoints(
        page: Int,
        callback: RequestCompleteListener<RewardPointResponse>
    ) {
        RetroClient.api.getRewardPoints(page).enqueue(object : Callback<RewardPointResponse> {

            override fun onFailure(call: Call<RewardPointResponse>, t: Throwable) {
                callback.onRequestFailed(t.localizedMessage ?: "Unknown")
            }

            override fun onResponse(
                call: Call<RewardPointResponse>,
                response: Response<RewardPointResponse>
            ) {

                if (response.body() != null && response.code() == 200) {
                    callback.onRequestSuccess(response.body() as RewardPointResponse)
                } else {
                    callback.onRequestFailed(TextUtils.getErrorMessage(response))
                }
            }

        })
    }
}