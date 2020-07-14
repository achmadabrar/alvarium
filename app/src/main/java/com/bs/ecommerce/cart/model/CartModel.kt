package com.bs.ecommerce.cart.model

import com.bs.ecommerce.account.auth.register.data.KeyValuePair
import com.bs.ecommerce.account.orders.model.data.UploadFileData
import com.bs.ecommerce.cart.model.data.CartResponse
import com.bs.ecommerce.cart.model.data.CartRootData
import com.bs.ecommerce.cart.model.data.EstimateShipping
import com.bs.ecommerce.cart.model.data.EstimateShippingData
import com.bs.ecommerce.networking.common.KeyValueFormData
import com.bs.ecommerce.networking.common.RequestCompleteListener
import java.io.File

interface CartModel
{
    fun getCartData(callback: RequestCompleteListener<CartResponse>)

    fun updateCartData(keyValueFormData: KeyValueFormData, callback: RequestCompleteListener<CartResponse>)

    fun applyCouponModel(keyValueFormData: KeyValueFormData, callback: RequestCompleteListener<CartResponse>)

    fun removeCouponModel(keyValueFormData: KeyValueFormData, callback: RequestCompleteListener<CartResponse>)

    fun applyGiftCardModel(keyValueFormData: KeyValueFormData, callback: RequestCompleteListener<CartResponse>)

    fun removeGiftCardModel(keyValueFormData: KeyValueFormData, callback: RequestCompleteListener<CartResponse>)

    fun applyCheckoutAttributes(list: List<KeyValuePair>, callback: RequestCompleteListener<CartRootData>)

    fun estimateShipping(body: EstimateShipping, callback: RequestCompleteListener<EstimateShippingData>)

    fun uploadFile(
        file: File,
        mimeType: String?,
        requestCompleteListener: RequestCompleteListener<UploadFileData>
    )

}