package com.bs.ecommerce.home.homepage.model

import com.bs.ecommerce.networking.common.RequestCompleteListener
import com.bs.ecommerce.main.model.data.StringResourceResponse

interface StringResourceModel {

    fun getStringResource(
        languageId: Int,
        callback: RequestCompleteListener<StringResourceResponse>
    )
}