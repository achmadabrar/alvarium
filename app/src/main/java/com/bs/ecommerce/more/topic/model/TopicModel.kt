package com.bs.ecommerce.more.topic.model

import com.bs.ecommerce.networking.common.RequestCompleteListener
import com.bs.ecommerce.more.topic.model.data.TopicResponse

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