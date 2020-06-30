package com.bs.ecommerce.catalog.common

import com.google.gson.annotations.SerializedName

class AttributeControlValue {
    @SerializedName("ColorSquaresRgb")
    var colorSquaresRgb: String? = null
    @SerializedName("CustomProperties")
    var customProperties: CustomProperties? = null
    @SerializedName("CustomerEntersQty")
    var customerEntersQty: Boolean? = null
    @SerializedName("Id")
    var id: Int = 0
    @SerializedName("ImageSquaresPictureModel")
    var imageSquaresPictureModel: PictureModel? = null
    @SerializedName("IsPreSelected")
    var isPreSelected: Boolean = false
    @SerializedName("Name")
    var name: String? = null
    @SerializedName("PictureId")
    var pictureId: Int?  = null
    @SerializedName("PriceAdjustment")
    var priceAdjustment: String? = null
    @SerializedName("PriceAdjustmentUsePercentage")
    var priceAdjustmentUsePercentage: Boolean? = null
    @SerializedName("PriceAdjustmentValue")
    var priceAdjustmentValue: Double? = null
    @SerializedName("Quantity")
    var quantity: Int?  = null

    override fun equals(other: Any?): Boolean {
        if(other !is AttributeControlValue) return false

        return this.id == other.id
    }
}