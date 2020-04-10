package com.bs.ecommerce.utils

import android.os.Build
import android.text.Html
import android.text.Spanned

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
}
