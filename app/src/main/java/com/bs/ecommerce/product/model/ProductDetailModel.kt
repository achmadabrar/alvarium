package com.bs.ecommerce.product.model

import com.bs.ecommerce.common.RequestCompleteListener
import com.bs.ecommerce.home.homepage.model.data.HomePageProductResponse
import com.bs.ecommerce.product.model.data.AddToCartResponse
import com.bs.ecommerce.product.model.data.ProductDetailResponse
import com.bs.ecommerce.product.model.data.UpdateCartPostData

interface ProductDetailModel {

    fun getProductDetailModel(productId: Long, callback: RequestCompleteListener<ProductDetailResponse>)

    fun addProductToCartModel(productId: Long,
                              cartTypeId: Long,
                              updateCartPostData: UpdateCartPostData, callback: RequestCompleteListener<AddToCartResponse>)

    fun getRelatedProducts(productId: Long, thumbnailSizePx: Int,
                           callback: RequestCompleteListener<HomePageProductResponse>)

    fun getAlsoPurchasedProducts(productId: Long, thumbnailSizePx: Int,
                                 callback: RequestCompleteListener<HomePageProductResponse>)

    fun addProductToWishList(
        productId: Long,
        callback: RequestCompleteListener<AddToCartResponse>
    )

}