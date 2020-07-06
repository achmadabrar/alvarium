package com.bs.ecommerce.more.options.model

import com.bs.ecommerce.networking.common.RequestCompleteListener
import com.bs.ecommerce.more.contactus.data.ContactUsData
import com.bs.ecommerce.more.contactus.data.ContactUsResponse
import com.bs.ecommerce.home.homepage.model.data.ManufacturerResponse

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