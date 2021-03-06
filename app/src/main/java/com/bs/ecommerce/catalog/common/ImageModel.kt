package com.bs.ecommerce.catalog.common

import com.bs.ecommerce.catalog.common.CustomProperties
import com.google.gson.annotations.SerializedName

/**
 * Created by Ashraful on 11/9/2015.
 */
open class ImageModel {

    @SerializedName("IsProduct")
    var isProduct: Int = 0
    @SerializedName("ProdOrCatId")
    var prodOrCatId: Int = 0

    // new service
    @SerializedName("ImageUrl")
    var imageUrl: String? = null

    @SerializedName("FullSizeImageUrl")
    var fullSizeImageUrl: String? = null

    @SerializedName("AlternateText")
    val alternateText: String? = ""

    @SerializedName("CustomProperties")
    val customProperties: CustomProperties? =
        CustomProperties()

    @SerializedName("Title")
    val title: String? = ""
}
