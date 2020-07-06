package com.bs.ecommerce.utils

interface PermissionCallback {
    fun onPermissionResponse(isGranted: Boolean)
}