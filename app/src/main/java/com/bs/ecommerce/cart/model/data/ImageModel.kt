package com.bs.ecommerce.cart.model.data

import com.google.gson.annotations.SerializedName


open class ImageModel
{

    @SerializedName("IsProduct") var isProduct: Int = 0
    @SerializedName("ProdOrCatId") var prodOrCatId: Int = 0

    @SerializedName("ImageUrl") var imageUrl: String? = null

    @SerializedName("FullSizeImageUrl") var fullSizeImageUrl: String? = null
}
