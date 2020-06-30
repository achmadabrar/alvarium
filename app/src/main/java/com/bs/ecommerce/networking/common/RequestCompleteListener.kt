package com.bs.ecommerce.networking.common

interface RequestCompleteListener<T>
{
    fun onRequestSuccess(data: T)
    fun onRequestFailed(errorMessage: String)
}