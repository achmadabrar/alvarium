package com.bs.ecommerce.main.model.data
import com.google.gson.annotations.SerializedName


data class CategoryTreeResponse(
    @SerializedName("Data") var categoryList: List<Category> = listOf(),
    @SerializedName("ErrorList") var errorList: List<Any> = listOf(),
    @SerializedName("Message") var message: Any = Any()
)

data class Category(
    @SerializedName("CategoryId") var categoryId: Int = 0,
    @SerializedName("IconUrl") var iconUrl: String = "",
    @SerializedName("Name") var name: String = "",
    var childVisible: Boolean = false,
    @SerializedName("Subcategories") var subcategories: List<Subcategory> = listOf()
)

data class Subcategory(
    @SerializedName("CategoryId") var categoryId: Int = 0,
    @SerializedName("IconUrl") var iconUrl: String = "",
    @SerializedName("Name") var name: String = "",
    @SerializedName("Subcategories") var subcategories: List<SecondSubcategory> = listOf()
)

data class SecondSubcategory(
    @SerializedName("CategoryId") var categoryId: Int = 0,
    @SerializedName("IconUrl") var iconUrl: String = "",
    @SerializedName("Name") var name: String = "",
    @SerializedName("Subcategories") var subcategories: List<Any> = listOf()
)