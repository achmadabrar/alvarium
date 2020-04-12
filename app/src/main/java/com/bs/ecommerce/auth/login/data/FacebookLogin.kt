package com.bs.ecommerce.auth.login.data

import com.google.gson.annotations.SerializedName


class FacebookLogin
{
    @SerializedName("ProviderUserId") var userId: String? = null

    @SerializedName("AccessToken") var accessToken: String? = null

    @SerializedName("Email") var email: String? = null

    @SerializedName("Name") var name: String? = null
}
