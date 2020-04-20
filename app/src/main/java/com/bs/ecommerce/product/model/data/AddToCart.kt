package com.bs.ecommerce.product.model.data

import com.google.gson.annotations.SerializedName

/**
 * Created by bs206 on 3/16/18.
 */
class AddToCart
(
    @SerializedName("ProductId") var productId: Int,
    @SerializedName("EnteredQuantity") var enteredQuantity: Int

)