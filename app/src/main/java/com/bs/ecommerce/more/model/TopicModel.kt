package com.bs.ecommerce.more.model

import com.bs.ecommerce.common.RequestCompleteListener
import com.bs.ecommerce.product.model.data.TopicResponse

interface TopicModel {

    fun getTopicBySystemName(
        systemName: String,
        callback: RequestCompleteListener<TopicResponse>
    )

    fun getTopicById(
        topicId: Int,
        callback: RequestCompleteListener<TopicResponse>
    )
}