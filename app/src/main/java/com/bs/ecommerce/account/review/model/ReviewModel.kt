package com.bs.ecommerce.account.review.model

import com.bs.ecommerce.networking.common.RequestCompleteListener
import com.bs.ecommerce.networking.common.KeyValueFormData
import com.bs.ecommerce.catalog.common.HelpfulnessResponse
import com.bs.ecommerce.account.review.model.data.MyReviewsResponse
import com.bs.ecommerce.account.review.model.data.ProductReviewResponse

interface ReviewModel {

    fun getMyReviews(
        pageNumber: Int,
        callback: RequestCompleteListener<MyReviewsResponse>
    )

    fun getProductReview(
        productId: Long,
        callback: RequestCompleteListener<ProductReviewResponse>
    )

    fun postProductReview(
        productId: Long,
        userData: ProductReviewResponse,
        callback: RequestCompleteListener<ProductReviewResponse>
    )

    fun postReviewHelpfulness(
        reviewId: Long,
        formData: KeyValueFormData,
        callback: RequestCompleteListener<HelpfulnessResponse>
    )
}