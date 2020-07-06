package com.bs.ecommerce.more.vendor.model.data


import com.bs.ecommerce.catalog.common.ProductByVendorData
import com.bs.ecommerce.networking.common.BaseResponse
import com.google.gson.annotations.SerializedName

data class GetAllVendorsResponse(
    @SerializedName("Data")
    val `data`: List<ProductByVendorData>?
): BaseResponse()