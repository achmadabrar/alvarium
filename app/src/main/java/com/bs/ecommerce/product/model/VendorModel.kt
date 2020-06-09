package com.bs.ecommerce.product.model

import com.bs.ecommerce.common.RequestCompleteListener
import com.bs.ecommerce.product.model.data.GetAllVendorsResponse
import com.bs.ecommerce.product.model.data.GetContactVendorResponse

interface VendorModel {

    fun getAllVendors(
        callback: RequestCompleteListener<GetAllVendorsResponse>
    )

    fun getContactVendorBody(
        vendorId: Int,
        callback: RequestCompleteListener<GetContactVendorResponse>
    )
}