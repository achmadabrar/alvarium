package com.bs.ecommerce.catalog.product.model

import com.bs.ecommerce.account.orders.model.data.UploadFileData
import com.bs.ecommerce.catalog.common.AddToCartResponse
import com.bs.ecommerce.catalog.common.AddToWishListResponse
import com.bs.ecommerce.catalog.common.ProductDetailResponse
import com.bs.ecommerce.home.homepage.model.data.HomePageProductResponse
import com.bs.ecommerce.networking.common.KeyValueFormData
import com.bs.ecommerce.networking.common.RequestCompleteListener
import okhttp3.ResponseBody
import retrofit2.Response
import java.io.File

interface ProductDetailModel {

    fun getProductDetailModel(productId: Long, callback: RequestCompleteListener<ProductDetailResponse>)

    fun addProductToCartModel(productId: Long,
                              cartTypeId: Long,
                              KeyValueFormData: KeyValueFormData, callback: RequestCompleteListener<AddToCartResponse>
    )

    fun getRelatedProducts(productId: Long, thumbnailSizePx: Int,
                           callback: RequestCompleteListener<HomePageProductResponse>
    )

    fun getAlsoPurchasedProducts(productId: Long, thumbnailSizePx: Int,
                                 callback: RequestCompleteListener<HomePageProductResponse>
    )

    fun addProductToWishList(
        productId: Long,
        cartType: Long,
        callback: RequestCompleteListener<AddToWishListResponse>
    )

    fun downloadSample(
        productId: Long,
        callback: RequestCompleteListener<Response<ResponseBody>>
    )

    fun uploadFile(
        file: File,
        mimeType: String?,
        requestCompleteListener: RequestCompleteListener<UploadFileData>
    )

}