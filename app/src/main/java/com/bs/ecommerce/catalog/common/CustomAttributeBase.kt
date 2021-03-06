package com.bs.ecommerce.catalog.common

import com.google.gson.annotations.SerializedName

/**
 * Base class for all types of custom attributes
 */
open class CustomAttribute {
    @SerializedName("AllowedFileExtensions")
    val allowedFileExtensions: List<Any>? = null

    @SerializedName("AttributeControlType")
    val attributeControlType: Int? = null

    @SerializedName("CustomProperties")
    val customProperties: CustomProperties? = null

    @SerializedName("DefaultValue")
    val defaultValue: String? = null

    @SerializedName("Description")
    val description: String? = null

    @SerializedName("HasCondition")
    val hasCondition: Boolean? = null

    @SerializedName("Id")
    val id: Long = 0

    @SerializedName("IsRequired")
    val isRequired: Boolean = false

    @SerializedName("Name")
    val name: String? = null

    @SerializedName("ProductId")
    val productId: Long? = null

    @SerializedName("SelectedDay")
    val selectedDay: Int? = null

    @SerializedName("SelectedMonth")
    val selectedMonth: Int? = null

    @SerializedName("SelectedYear")
    val selectedYear: Int? = null

    @SerializedName("TextPrompt")
    val textPrompt: String? = null

    @SerializedName("Values")
    var values: List<AttributeControlValue> = listOf()

    override fun equals(other: Any?): Boolean {
        if (other !is CustomAttribute) return false
        return other.id == this.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}

class ProductAttribute : CustomAttribute() {
    @SerializedName("ProductAttributeId")
    val productAttributeId: Long = 0

    /*override fun equals(other: Any?): Boolean {
        if (other !is ProductAttribute) return false
        return other.productAttributeId == this.productAttributeId
    }

    override fun hashCode(): Int {
        return productAttributeId.hashCode()
    }*/
}

class CustomerAttribute : CustomAttribute()

class CheckoutAttribute: CustomAttribute()

class AddressAttribute: CustomAttribute()
