package com.bs.ecommerce.common

interface RequestCompleteListener<T>
{
    fun onRequestSuccess(data: T)
    fun onRequestFailed(errorMessage: String)
}