package com.bs.ecommerce.utils

import android.content.Context
import android.os.Build
import android.os.LocaleList
import androidx.annotation.RequiresApi
import java.util.*

/**
 * Created by bs206 on 3/15/18.
 */
class ContextWrapper(base: Context) : android.content.ContextWrapper(base)
{
    companion object
    {

        @RequiresApi(Build.VERSION_CODES.N)
        fun wrap(context: Context, newLocale: Locale): ContextWrapper {
            var context = context

            val res = context.resources
            val configuration = res.configuration

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            {
                configuration.setLocale(newLocale)

                val localeList = LocaleList(newLocale)
                LocaleList.setDefault(localeList)
                configuration.setLocales(localeList)

                context = context.createConfigurationContext(configuration)

            }
            else
            {
                configuration.locale = newLocale
                res.updateConfiguration(configuration, res.displayMetrics)
            }

            return ContextWrapper(context)
        }
    }
}
