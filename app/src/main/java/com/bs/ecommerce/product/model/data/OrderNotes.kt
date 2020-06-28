package com.bs.ecommerce.product.model.data


import com.google.gson.annotations.SerializedName

data class OrderNotes(
    @SerializedName("CreatedOn")
    val createdOn: String?,
    @SerializedName("HasDownload")
    val hasDownload: Boolean?,
    @SerializedName("Id")
    val id: Int?,
    @SerializedName("Note")
    val note: String?
)