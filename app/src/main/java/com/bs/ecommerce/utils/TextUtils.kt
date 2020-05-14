package com.bs.ecommerce.utils

import android.content.Context
import android.os.Build
import android.text.Html
import android.text.Spanned
import com.bs.ecommerce.R
import com.bs.ecommerce.networking.common.BaseResponse
import com.bs.ecommerce.product.model.data.AddressModel
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import retrofit2.Response
import java.lang.ref.WeakReference
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

    fun tzTimeConverter(createdOn: String?, context: WeakReference<Context>): String {
        if (createdOn.isNullOrEmpty()) return ""
        val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        var date = ""

        try {
            val originalDate = parser.parse(createdOn)

            val dateFormat = android.text.format.DateFormat.getDateFormat(context.get())
            val timeFormat = android.text.format.DateFormat.getTimeFormat(context.get())

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

    fun epoch2DateString(epochMilliSeconds: Long, formatString: String): String {
        val sdf = SimpleDateFormat(formatString, Locale.getDefault())
        sdf.timeZone = TimeZone.getDefault()

        return sdf.format(epochMilliSeconds)
    }

    fun dateStringToEpoch(plainDate: String, format: String): Long? {
        val df = SimpleDateFormat(format, Locale.ROOT)

        try {
            val date = df.parse(plainDate)
            return date?.time
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return null
    }


    fun getFormattedAddress(address: AddressModel?, context: WeakReference<Context>): String {

        if (address == null) return ""

        return StringBuilder().apply {
            append(context.get()?.getString(R.string.reg_hint_email)).append(": ").append(address.email)
                .append("\n")

            append(context.get()?.getString(R.string.reg_hint_phone)).append(": ").append(address.phoneNumber)
                .append("\n")

            if (address.faxNumber?.isNotEmpty() == true)
                append(context.get()?.getString(R.string.reg_hint_fax)).append(": ").append(address.faxNumber)
                    .append("\n")

            if (address.company?.isNotEmpty() == true)
                append(context.get()?.getString(R.string.reg_hint_company_name)).append(": ")
                    .append(address.company).append("\n")

            append(address.address1).append("\n")
            append(address.city).append(" ").append(address.zipPostalCode).append("\n")
            append(address.countryName)

        }.toString()
    }

    fun getFormattedDate(dd: Int, mm: Int, yy: Int): String {

        if(dd < 1 || mm < 1 || yy < 1) return ""
        else return "$mm/$dd/$yy"
    }

    companion object {

        /**
         * Extracts error message from retrofit error body
         */
        fun getErrorMessage(response: Response<*>): String {

            val errorMsg = response.errorBody()?.string()

            if (errorMsg.isNullOrEmpty()) return response.raw().message

            val baseResponse = try {
                Gson().fromJson(errorMsg, BaseResponse::class.java)
            } catch (e: JsonSyntaxException) {
                null
            }

            return baseResponse?.errorsAsFormattedString ?: errorMsg
        }
    }
}
