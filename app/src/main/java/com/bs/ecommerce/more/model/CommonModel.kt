package com.bs.ecommerce.more.model

import com.bs.ecommerce.common.RequestCompleteListener
import com.bs.ecommerce.product.model.data.ContactUsData
import com.bs.ecommerce.product.model.data.ContactUsResponse
import com.bs.ecommerce.product.model.data.TopicResponse

interface CommonModel {

    fun postCustomerEnquiry(
        userData: ContactUsData,
        callback: RequestCompleteListener<ContactUsResponse>
    )
}