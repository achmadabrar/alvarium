package com.bs.ecommerce.more.vendor.model

import com.bs.ecommerce.networking.common.RequestCompleteListener
import com.bs.ecommerce.more.vendor.model.data.GetAllVendorsResponse
import com.bs.ecommerce.more.vendor.model.data.GetContactVendorResponse

interface VendorModel {

    fun getAllVendors(
        callback: RequestCompleteListener<GetAllVendorsResponse>
    )

    fun getContactVendorModel(
        vendorId: Int,
        callback: RequestCompleteListener<GetContactVendorResponse>
    )

    fun postContactVendorModel(
        reqBody: GetContactVendorResponse,
        callback: RequestCompleteListener<GetContactVendorResponse>
    )
}