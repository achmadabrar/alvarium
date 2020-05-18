package com.bs.ecommerce.more.model

import com.bs.ecommerce.common.RequestCompleteListener
import com.bs.ecommerce.networking.RetroClient
import com.bs.ecommerce.product.model.data.TopicResponse
import com.bs.ecommerce.utils.TextUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TopicModelImpl : TopicModel {

    override fun getTopicBySystemName(
        systemName: String,
        callback: RequestCompleteListener<TopicResponse>
    ) {
        RetroClient.api.getTopic(systemName).enqueue(object : Callback<TopicResponse> {

            override fun onFailure(call: Call<TopicResponse>, t: Throwable) {
                callback.onRequestFailed(t.localizedMessage ?: "Unknown")
            }

            override fun onResponse(call: Call<TopicResponse>, response: Response<TopicResponse>) {

                if(response.body()!=null && response.code() == 200) {
                    callback.onRequestSuccess(response.body() as TopicResponse)
                } else {
                    callback.onRequestFailed(TextUtils.getErrorMessage(response))
                }
            }

        })
    }

    override fun getTopicById(
        topicId: Int,
        callback: RequestCompleteListener<TopicResponse>
    ) {
        RetroClient.api.getTopicById(topicId).enqueue(object : Callback<TopicResponse> {

            override fun onFailure(call: Call<TopicResponse>, t: Throwable) {
                callback.onRequestFailed(t.localizedMessage ?: "Unknown")
            }

            override fun onResponse(call: Call<TopicResponse>, response: Response<TopicResponse>) {

                if(response.body()!=null && response.code() == 200) {
                    callback.onRequestSuccess(response.body() as TopicResponse)
                } else {
                    callback.onRequestFailed(TextUtils.getErrorMessage(response))
                }
            }

        })
    }
}