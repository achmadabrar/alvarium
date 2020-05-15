package com.bs.ecommerce.more.viewmodel

import androidx.lifecycle.MutableLiveData
import com.bs.ecommerce.base.BaseViewModel
import com.bs.ecommerce.common.RequestCompleteListener
import com.bs.ecommerce.more.model.RewardPointModel
import com.bs.ecommerce.product.model.data.RewardPointData
import com.bs.ecommerce.product.model.data.RewardPointResponse

class RewardPointViewModel: BaseViewModel() {

    val rewardPointLD = MutableLiveData<RewardPointData>()

    var pageNumber = 0
    var lastPageReached = false
    var shouldAppend = true


    fun getRewardPoints(model: RewardPointModel) {

        if(lastPageReached) return

        isLoadingLD.value = true

        model.fetchRewardPoints(++pageNumber, object: RequestCompleteListener<RewardPointResponse> {

            override fun onRequestSuccess(data: RewardPointResponse) {
                isLoadingLD.value = false

                if(data.data != null)
                    rewardPointLD.value = data.data
            }

            override fun onRequestFailed(errorMessage: String) {
                isLoadingLD.value = false
                toast(errorMessage)
            }
        })
    }
}