package com.bs.ecommerce.utils

import android.content.Context
import android.net.ConnectivityManager


class Utils {

    /**
     * Checks internet connectivity through wifi or mobile network
     *
     * @return `true` if wifi or mobile internet is available
     */
    fun isWifiOrMobileNetworkAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val networkInfo = connectivityManager.activeNetworkInfo

        return !(networkInfo == null || !networkInfo.isConnected ||
                (networkInfo.type != ConnectivityManager.TYPE_WIFI
                        && networkInfo.type != ConnectivityManager.TYPE_MOBILE))
    }
}