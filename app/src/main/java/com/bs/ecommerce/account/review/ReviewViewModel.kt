package com.bs.ecommerce.account.review

import androidx.lifecycle.MutableLiveData
import com.bs.ecommerce.account.auth.register.data.KeyValuePair
import com.bs.ecommerce.base.BaseViewModel
import com.bs.ecommerce.networking.common.RequestCompleteListener
import com.bs.ecommerce.account.review.model.ReviewModel
import com.bs.ecommerce.account.review.model.data.MyReviewsResponse
import com.bs.ecommerce.account.review.model.data.ProductReview
import com.bs.ecommerce.account.review.model.data.ProductReviewData
import com.bs.ecommerce.account.review.model.data.ProductReviewResponse
import com.bs.ecommerce.catalog.common.Helpfulness
import com.bs.ecommerce.catalog.common.HelpfulnessResponse
import com.bs.ecommerce.networking.Api
import com.bs.ecommerce.networking.common.KeyValueFormData

class ReviewViewModel: BaseViewModel() {

    var myReviewsLD = MutableLiveData<List<ProductReview>?>()
    var productReviewLD = MutableLiveData<ProductReviewData?>()

    var pageNumber = 0
    var lastPageReached = false
    var shouldAppend = true

    // tracks the list item, for which the current operation is ongoing
    var operationalReviewIdLD = MutableLiveData<Helpfulness?>()

    fun getMyReviews(model: ReviewModel) {

        if(lastPageReached || isLoadingLD.value == true) return

        isLoadingLD.value = true

        model.getMyReviews(++pageNumber, object:
            RequestCompleteListener<MyReviewsResponse> {

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

        model.getProductReview(productId, object:
            RequestCompleteListener<ProductReviewResponse> {

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

    fun postReviewHelpfulness(reviewId: Long, positive: Boolean, model: ReviewModel) {

        isLoadingLD.value = true

        // preparing request body

        val formValues: MutableList<KeyValuePair> = mutableListOf()

        formValues.add(
            KeyValuePair().apply {
            key = Api.productReviewId
            value = reviewId.toString()
        })

        formValues.add(
            KeyValuePair().apply {
            key = Api.wasReviewHelpful
            value = positive.toString()
        })

        model.postReviewHelpfulness(
            reviewId,
            KeyValueFormData(formValues),
            object:
                RequestCompleteListener<HelpfulnessResponse> {

            override fun onRequestSuccess(data: HelpfulnessResponse) {
                isLoadingLD.value = false

                if(data.data != null) {
                    toast(data.message)

                    data.data.productReviewId = reviewId
                    operationalReviewIdLD.value = data.data
                }
            }

            override fun onRequestFailed(errorMessage: String) {
                isLoadingLD.value = false
                toast(errorMessage)
            }

        })
    }

    fun postProductReview(
        userData: ProductReviewData,
        model: ReviewModel
    ) {
        isLoadingLD.value = true

        model.postProductReview(
            userData.productId ?: -1,
            ProductReviewResponse(
                userData
            ),
            object:
                RequestCompleteListener<ProductReviewResponse> {

            override fun onRequestSuccess(data: ProductReviewResponse) {
                isLoadingLD.value = false

                if(data.data != null) {
                    productReviewLD.value = data.data

                    toast(data.data.addProductReview?.result)
                }
            }

            override fun onRequestFailed(errorMessage: String) {
                isLoadingLD.value = false
                toast(errorMessage)
            }

        })
    }
}