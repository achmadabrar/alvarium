package com.bs.ecommerce.utils

import android.view.View

interface ItemClickListener<T> {
    fun onClick(view: View, position: Int, data: T)
}