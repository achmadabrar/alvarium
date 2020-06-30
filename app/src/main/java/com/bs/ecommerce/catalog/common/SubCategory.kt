package com.bs.ecommerce.catalog.common


import com.bs.ecommerce.catalog.common.CustomProperties
import com.bs.ecommerce.catalog.common.PictureModel
import com.google.gson.annotations.SerializedName

data class SubCategory(
    @SerializedName("CustomProperties")
    val customProperties: CustomProperties? = CustomProperties(),
    @SerializedName("Id")
    val id: Int? = 0,
    @SerializedName("Name")
    val name: String? = "",
    @SerializedName("PictureModel")
    val pictureModel: PictureModel? = PictureModel(),
    @SerializedName("SeName")
    val seName: String? = ""
)