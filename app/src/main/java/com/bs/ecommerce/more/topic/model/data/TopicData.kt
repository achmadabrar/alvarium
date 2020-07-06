package com.bs.ecommerce.more.topic.model.data


import com.bs.ecommerce.catalog.common.CustomProperties
import com.google.gson.annotations.SerializedName

data class TopicData(
    @SerializedName("Body")
    val body: String?,
    @SerializedName("CustomProperties")
    val customProperties: CustomProperties?,
    @SerializedName("Id")
    val id: Int?,
    @SerializedName("IncludeInSitemap")
    val includeInSitemap: Boolean?,
    @SerializedName("IsPasswordProtected")
    val isPasswordProtected: Boolean?,
    @SerializedName("MetaDescription")
    val metaDescription: String?,
    @SerializedName("MetaKeywords")
    val metaKeywords: Any?,
    @SerializedName("MetaTitle")
    val metaTitle: String?,
    @SerializedName("SeName")
    val seName: String?,
    @SerializedName("SystemName")
    val systemName: String?,
    @SerializedName("Title")
    val title: String?,
    @SerializedName("TopicTemplateId")
    val topicTemplateId: Int?
)