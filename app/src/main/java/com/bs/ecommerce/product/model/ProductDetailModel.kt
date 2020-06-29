package com.bs.ecommerce.product.model

import com.bs.ecommerce.common.RequestCompleteListener
import com.bs.ecommerce.home.homepage.model.data.HomePageProductResponse
import com.bs.ecommerce.networking.common.KeyValueFormData
import com.bs.ecommerce.product.model.data.AddToCartResponse
import com.bs.ecommerce.product.model.data.AddToWishListResponse
import com.bs.ecommerce.product.model.data.ProductDetailResponse
import okhttp3.ResponseBody
import retrofit2.Response

interface ProductDetailModel {

    fun getProductDetailModel(productId: Long, callback: RequestCompleteListener<ProductDetailResponse>)

    fun addProductToCartModel(productId: Long,
                              cartTypeId: Long,
                              KeyValueFormData: KeyValueFormData, callback: RequestCompleteListener<AddToCartResponse>)

    fun getRelatedProducts(productId: Long, thumbnailSizePx: Int,
                           callback: RequestCompleteListener<HomePageProductResponse>)

    fun getAlsoPurchasedProducts(productId: Long, thumbnailSizePx: Int,
                                 callback: RequestCompleteListener<HomePageProductResponse>)

    fun addProductToWishList(
        productId: Long,
        cartType: Long,
        callback: RequestCompleteListener<AddToWishListResponse>
    )

    fun downloadSample(
        productId: Long,
        callback: RequestCompleteListener<Response<ResponseBody>>
    )

}