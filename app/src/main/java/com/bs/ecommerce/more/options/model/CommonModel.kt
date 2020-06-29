package com.bs.ecommerce.more.options.model

import com.bs.ecommerce.common.RequestCompleteListener
import com.bs.ecommerce.product.model.data.ContactUsData
import com.bs.ecommerce.product.model.data.ContactUsResponse
import com.bs.ecommerce.product.model.data.ManufacturerResponse

interface CommonModel {

    fun postCustomerEnquiry(
        userData: ContactUsData,
        callback: RequestCompleteListener<ContactUsResponse>
    )

    fun getContactUsModel(
        callback: RequestCompleteListener<ContactUsResponse>
    )

    fun getAllManufacturers(
        callback: RequestCompleteListener<ManufacturerResponse>
    )
}