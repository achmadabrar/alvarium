package com.bs.ecommerce.product.model.data

import com.google.gson.annotations.SerializedName

/**
 * Created by bs206 on 3/16/18.
 */
class AttributeControlValue
{
    @SerializedName("Name") var name: String? = null
    @SerializedName("ColorSquaresRgb") var colorSquaresRgb: String? = null
    @SerializedName("PriceAdjustment") var priceAdjustment: String? = null
    @SerializedName("PriceAdjustmentValue") var priceAdjustmentValue: Double = 0.toDouble()
    @SerializedName("IsPreSelected") var isPreSelected: Boolean = false
    @SerializedName("Id") var id: Int = 0
    @SerializedName("PictureModel") var pictureModel: PictureModel? = null
    @SerializedName("DefaultValue") var defaultValue: String? = null

    override fun equals(other: Any?): Boolean {
        if(other !is AttributeControlValue) return false

        return this.id == other.id
    }
}