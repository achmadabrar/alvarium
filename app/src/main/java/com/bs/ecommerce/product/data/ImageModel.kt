package com.bs.ecommerce.product.data

import com.google.gson.annotations.SerializedName

/**
 * Created by Ashraful on 11/9/2015.
 */
open class ImageModel
{

    @SerializedName("IsProduct") var isProduct: Int = 0
    @SerializedName("ProdOrCatId") var prodOrCatId: Int = 0

    @SerializedName("ImageUrl") var imageUrl: String? = null

    @SerializedName("FullSizeImageUrl") var fullSizeImageUrl: String? = null
}