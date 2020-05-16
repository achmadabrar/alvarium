package com.bs.ecommerce.more.model

import com.bs.ecommerce.common.RequestCompleteListener
import com.bs.ecommerce.product.model.data.MyReviewsResponse
import com.bs.ecommerce.product.model.data.ProductReviewResponse

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

    //fun setReviewHelpfulness(
    // productId: Long)
}