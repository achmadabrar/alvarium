package com.bs.ecommerce.more.model

import com.bs.ecommerce.common.RequestCompleteListener
import com.bs.ecommerce.networking.RetroClient
import com.bs.ecommerce.product.model.data.MyReviewsResponse
import com.bs.ecommerce.product.model.data.ProductReviewResponse
import com.bs.ecommerce.utils.TextUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ReviewModelImpl: ReviewModel {

    override fun getMyReviews(
        pageNumber: Int,
        callback: RequestCompleteListener<MyReviewsResponse>
    ) {
        RetroClient.api.getMyReviews(pageNumber).enqueue(object : Callback<MyReviewsResponse> {

            override fun onFailure(call: Call<MyReviewsResponse>, t: Throwable) {
                callback.onRequestFailed(t.localizedMessage ?: "Unknown")
            }

            override fun onResponse(call: Call<MyReviewsResponse>, response: Response<MyReviewsResponse>) {

                if(response.body()!=null && response.code() == 200) {
                    callback.onRequestSuccess(response.body() as MyReviewsResponse)
                } else {
                    callback.onRequestFailed(TextUtils.getErrorMessage(response))
                }
            }

        })
    }

    override fun getProductReview(
        productId: Long,
        callback: RequestCompleteListener<ProductReviewResponse>
    ) {
        RetroClient.api.getProductReview(productId).enqueue(object : Callback<ProductReviewResponse> {

            override fun onFailure(call: Call<ProductReviewResponse>, t: Throwable) {
                callback.onRequestFailed(t.localizedMessage ?: "Unknown")
            }

            override fun onResponse(
                call: Call<ProductReviewResponse>,
                response: Response<ProductReviewResponse>
            ) {

                if(response.body()!=null && response.code() == 200) {
                    callback.onRequestSuccess(response.body() as ProductReviewResponse)
                } else {
                    callback.onRequestFailed(TextUtils.getErrorMessage(response))
                }
            }

        })
    }
}