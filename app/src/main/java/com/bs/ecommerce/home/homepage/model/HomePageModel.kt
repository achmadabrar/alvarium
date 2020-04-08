package com.bs.ecommerce.home.homepage.model

import com.bs.ecommerce.auth.data.KeyValuePair
import com.bs.ecommerce.cart.model.data.CartResponse
import com.bs.ecommerce.common.RequestCompleteListener
import com.bs.ecommerce.home.homepage.model.data.HomePageProductResponse

interface HomePageModel
{
    fun getFeaturedProducts(callback: RequestCompleteListener<HomePageProductResponse>)



}