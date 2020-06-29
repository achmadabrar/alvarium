package com.bs.ecommerce.more.topic

import androidx.lifecycle.MutableLiveData
import com.bs.ecommerce.base.BaseViewModel
import com.bs.ecommerce.common.RequestCompleteListener
import com.bs.ecommerce.more.topic.model.TopicModel
import com.bs.ecommerce.product.model.data.TopicData
import com.bs.ecommerce.product.model.data.TopicResponse

class TopicViewModel: BaseViewModel() {

    var topicLD = MutableLiveData<TopicData?>()

    fun fetchTopic(sysName: String, model: TopicModel) {

        isLoadingLD.value = true

        model.getTopicBySystemName(sysName, object: RequestCompleteListener<TopicResponse> {

            override fun onRequestSuccess(data: TopicResponse) {
                isLoadingLD.value = false

                if(data.data != null)
                    topicLD.value = data.data
            }

            override fun onRequestFailed(errorMessage: String) {
                isLoadingLD.value = false
                toast(errorMessage)
            }

        })
    }

    fun fetchTopic(topicId: Int, model: TopicModel) {

        isLoadingLD.value = true

        model.getTopicById(topicId, object: RequestCompleteListener<TopicResponse> {

            override fun onRequestSuccess(data: TopicResponse) {
                isLoadingLD.value = false

                if(data.data != null)
                    topicLD.value = data.data
            }

            override fun onRequestFailed(errorMessage: String) {
                isLoadingLD.value = false
                toast(errorMessage)
            }

        })
    }
}