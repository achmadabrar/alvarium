package com.bs.ecommerce.home.homepage.model

import com.bs.ecommerce.common.RequestCompleteListener
import com.bs.ecommerce.product.model.data.StringResourceResponse

interface StringResourceModel {

    fun getStringResource(
        languageId: Int,
        callback: RequestCompleteListener<StringResourceResponse>
    )
}