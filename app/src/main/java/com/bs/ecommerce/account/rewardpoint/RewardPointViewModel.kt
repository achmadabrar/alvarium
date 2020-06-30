package com.bs.ecommerce.account.rewardpoint

import androidx.lifecycle.MutableLiveData
import com.bs.ecommerce.base.BaseViewModel
import com.bs.ecommerce.networking.common.RequestCompleteListener
import com.bs.ecommerce.account.rewardpoint.model.RewardPointModel
import com.bs.ecommerce.account.rewardpoint.model.data.RewardPointData
import com.bs.ecommerce.account.rewardpoint.model.data.RewardPointResponse

class RewardPointViewModel: BaseViewModel() {

    val rewardPointLD = MutableLiveData<RewardPointData>()

    var pageNumber = 0
    var lastPageReached = false
    var shouldAppend = true


    fun getRewardPoints(model: RewardPointModel) {

        if(lastPageReached || isLoadingLD.value == true) return

        isLoadingLD.value = true

        model.fetchRewardPoints(++pageNumber, object:
            RequestCompleteListener<RewardPointResponse> {

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