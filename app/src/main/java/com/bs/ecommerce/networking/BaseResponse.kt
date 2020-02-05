package com.bs.ecommerce.networking

import android.widget.Toast
import com.google.gson.annotations.SerializedName

/**
 * Created by Ashraful on 11/6/2015.
 */

open class BaseResponse
{


    @SerializedName("StatusCode") var statusCode: Int = 0

    @SerializedName("ErrorList") lateinit var errorList: Array<String>

    val errorsAsFormattedString: String
        get()
        {
            var errors = ""

            if (errorList.isNotEmpty())
            {
                for (i in errorList.indices)
                {
                    errors += "  " + (i + 1) + ": " + errorList[i] + " \n"
                }
            }
            return errors
        }
}
