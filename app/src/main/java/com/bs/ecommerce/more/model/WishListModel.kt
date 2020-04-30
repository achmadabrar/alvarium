package com.bs.ecommerce.more.model

import com.bs.ecommerce.auth.register.data.KeyValuePair
import com.bs.ecommerce.common.RequestCompleteListener
import com.bs.ecommerce.product.model.data.WishListResponse

interface WishListModel {

    fun getWishList(callback: RequestCompleteListener<WishListResponse>)

    fun updateWishListData(
        keyValuePairs: List<KeyValuePair>,
        callback: RequestCompleteListener<WishListResponse>
    )
}