package com.bs.ecommerce.more.viewmodel

import androidx.lifecycle.MutableLiveData
import com.bs.ecommerce.base.BaseViewModel
import com.bs.ecommerce.common.RequestCompleteListener
import com.bs.ecommerce.more.model.ReviewModel
import com.bs.ecommerce.product.model.data.MyReviewsResponse
import com.bs.ecommerce.product.model.data.ProductReview
import com.bs.ecommerce.product.model.data.ProductReviewData
import com.bs.ecommerce.product.model.data.ProductReviewResponse

class ReviewViewModel: BaseViewModel() {

    var myReviewsLD = MutableLiveData<List<ProductReview>?>()
    var productReviewLD = MutableLiveData<ProductReviewData?>()

    var pageNumber = 0
    var lastPageReached = false
    var shouldAppend = true

    // tracks the list item, for which the current operation is ongoing
    var operationalItemIndex: Int? = null

    fun getMyReviews(model: ReviewModel) {

        if(lastPageReached) return

        isLoadingLD.value = true

        model.getMyReviews(++pageNumber, object: RequestCompleteListener<MyReviewsResponse> {

            override fun onRequestSuccess(data: MyReviewsResponse) {
                isLoadingLD.value = false

                lastPageReached =
                    data.data?.pagerModel?.currentPage == data.data?.pagerModel?.totalPages

                if(data.data != null)
                    myReviewsLD.value = data.data.productReviews ?: listOf()
            }

            override fun onRequestFailed(errorMessage: String) {
                isLoadingLD.value = false
                toast(errorMessage)
            }

        })
    }

    fun getProductReview(productId: Long, model: ReviewModel) {

        isLoadingLD.value = true

        model.getProductReview(productId, object: RequestCompleteListener<ProductReviewResponse> {

            override fun onRequestSuccess(data: ProductReviewResponse) {
                isLoadingLD.value = false

                if(data.data != null)
                    productReviewLD.value = data.data
            }

            override fun onRequestFailed(errorMessage: String) {
                isLoadingLD.value = false
                toast(errorMessage)
            }

        })
    }

    fun setReviewHelpfulness(position: Int, positive: Boolean, model: ReviewModel) {

        operationalItemIndex = position


    }
}