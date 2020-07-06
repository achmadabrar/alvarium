package com.bs.ecommerce.more.options.model.data

import com.bs.ecommerce.R

data class AppOptions(
    val nameResId: Int,
    val nameString: String,
    val iconResId: Int = R.drawable.ic_calendar
)