package com.bs.ecommerce.product.model.data

import com.google.gson.annotations.SerializedName

/**
 * Created by Ashraful on 11/20/2015.
 */
class ProductAttribute {
    @SerializedName("ProductId") var productId: Long = 0
    @SerializedName("Id") var id: Int = 0


    @SerializedName("ProductAttributeId") var productAttributeId: Long = 0
    @SerializedName("Name") var name: String? = null
    @SerializedName("Description") var description: String? = null
    @SerializedName("TextPrompt") var textPrompt: String? = null
    @SerializedName("IsRequired") var isRequired: Boolean = false
    @SerializedName("DefaultValue") var defaultValue: String? = null
    @SerializedName("AttributeControlType") var attributeControlType: Int = 0
    @SerializedName("Values") var values: List<AttributeControlValue> = mutableListOf()
    @SerializedName("SelectedDay") private val SelectedDay: Any? = null
    @SerializedName("SelectedMonth") private val SelectedMonth: Any? = null
    @SerializedName("SelectedYear") private val SelectedYear: Any? = null

    override fun equals(other: Any?): Boolean {
        if(other !is ProductAttribute) return false
        return other.productAttributeId == this.productAttributeId
    }
}

class CustomerAttribute {
    @SerializedName("Id") var id: Long = 0
    @SerializedName("Name") var name: String = ""
    @SerializedName("Description") var description: String = ""
    @SerializedName("TextPrompt") var textPrompt: String? = null
    @SerializedName("IsRequired") var isRequired: Boolean = false
    @SerializedName("DefaultValue") var defaultValue: String? = null
    @SerializedName("AttributeControlType") var attributeControlType: Int = 0
    @SerializedName("Values") var values: List<AttributeControlValue> = mutableListOf()
    @SerializedName("SelectedDay") private val SelectedDay: Any? = null
    @SerializedName("SelectedMonth") private val SelectedMonth: Any? = null
    @SerializedName("SelectedYear") private val SelectedYear: Any? = null
}
