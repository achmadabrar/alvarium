package com.bs.ecommerce.networking.common

import com.bs.ecommerce.auth.register.data.KeyValuePair
import com.google.gson.annotations.SerializedName


data class KeyValueFormData(@SerializedName("FormValues") var formValues: List<KeyValuePair> = listOf())