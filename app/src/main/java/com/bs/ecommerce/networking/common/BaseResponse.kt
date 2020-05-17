package com.bs.ecommerce.networking.common

import com.bs.ecommerce.auth.register.data.KeyValuePair
import com.bs.ecommerce.utils.showLog
import com.google.gson.annotations.SerializedName

/**
 * Created by Ashraful on 11/6/2015.
 */

open class BaseResponse
{
    @SerializedName("FormValues") var formValues: List<KeyValuePair>? = null

    @SerializedName("Message") var message: String? = ""

    @SerializedName("ErrorList")  var errorList: Array<String> = arrayOf()

    val errorsAsFormattedString: String
        get()
        {
            var errors = ""

            if (errorList.isNotEmpty())
            {
                for (i in errorList.indices)
                {
                    //errors += "  " + (i + 1) + ": " + errorList[i] + " \n"
                    errors += "‣ " + errorList[i] + "\n"

                    "wewtet".showLog(errorList[i])
                }
            }
            return errors.trim()
        }
}
