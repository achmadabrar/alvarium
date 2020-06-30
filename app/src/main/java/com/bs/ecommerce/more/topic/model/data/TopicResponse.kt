package com.bs.ecommerce.more.topic.model.data


import com.bs.ecommerce.more.topic.model.data.TopicData
import com.bs.ecommerce.networking.common.BaseResponse
import com.google.gson.annotations.SerializedName

data class TopicResponse(
    @SerializedName("Data")
    val `data`: TopicData?
): BaseResponse()