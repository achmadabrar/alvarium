package com.bs.ecommerce.cart.model.data

import com.google.gson.annotations.SerializedName

data class CheckoutAttribute(
    @SerializedName("AllowedFileExtensions") var allowedFileExtensions: List<Any> = listOf(),
    @SerializedName("AttributeControlType") var attributeControlType: Int = 0,
    @SerializedName("DefaultValue") var defaultValue: Any = Any(),
    @SerializedName("Id") var id: Int = 0,
    @SerializedName("IsRequired") var isRequired: Boolean = false,
    @SerializedName("Name") var name: String = "",
    @SerializedName("SelectedDay") var selectedDay: Any = Any(),
    @SerializedName("SelectedMonth") var selectedMonth: Any = Any(),
    @SerializedName("SelectedYear") var selectedYear: Any = Any(),
    @SerializedName("TextPrompt") var textPrompt: Any = Any(),
    @SerializedName("Values") var values: List<Value> = listOf()
)



data class Value(
    @SerializedName("ColorSquaresRgb") var colorSquaresRgb: Any = Any(),
    @SerializedName("Id") var id: Int = 0,
    @SerializedName("IsPreSelected") var isPreSelected: Boolean = false,
    @SerializedName("Name") var name: String = "",
    @SerializedName("PriceAdjustment") var priceAdjustment: Any = Any()
)

