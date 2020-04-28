package com.bs.ecommerce.utils

import android.content.Context
import android.os.Build
import android.text.Html
import android.text.Spanned
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by bs206 on 3/15/18.
 */
class TextUtils {

    fun removeBlankLines(text: String): String {
        return text.replace("(?m)^[ \t]*\r?\n".toRegex(), "")
    }

    fun getHtmlFormattedText(htmlText: String?) : Spanned {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(htmlText, Html.FROM_HTML_MODE_COMPACT)
        } else {
            Html.fromHtml(htmlText)
        }
    }

    fun tzTimeConverter(createdOn: String?, context: Context): String {
        if (createdOn.isNullOrEmpty()) return ""
        val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        var date = ""

        try {
            val originalDate = parser.parse(createdOn)

            val dateFormat = android.text.format.DateFormat.getDateFormat(context)
            val timeFormat = android.text.format.DateFormat.getTimeFormat(context)

            val format =
                (dateFormat as SimpleDateFormat).toLocalizedPattern() + " " + (timeFormat as SimpleDateFormat).toLocalizedPattern()
            val formatter = SimpleDateFormat(format, Locale.getDefault())

            if(originalDate==null)
                return date

            date = formatter.format(originalDate)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return date
    }
}
