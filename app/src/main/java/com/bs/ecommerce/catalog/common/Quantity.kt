package com.bs.ecommerce.catalog.common

import com.google.gson.annotations.SerializedName

/**
 * Created by BS168 on 3/3/2017.
 */

class Quantity {

    @SerializedName("OrderMinimumQuantity") var orderMinimumQuantity: Int = 0

    @SerializedName("OrderMaximumQuantity") var orderMaximumQuantity: Int = 0

    @SerializedName("StockQuantity") var stockQuantity: Int = 0
}
