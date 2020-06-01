package com.bs.ecommerce.utils

import android.widget.EditText
import android.widget.Toast

/**
 * Created by bs206 on 3/15/18.
 */
class EditTextUtils {

    fun isValidString(et: EditText): Boolean
    {
        val string = et.text.toString().trim()

        return string.isNotEmpty()
    }

    fun getString(et: EditText): String
    {
        val string = et.text.toString().trim()
        return if (string.isEmpty())
            ""
        else string
    }

    fun isValidInteger(et: EditText): Boolean
    {
        if (!isValidString(et))
            return false
         else
        {
            try
            {
                Integer.parseInt(getString(et))
                return true
            }
            catch (e: NumberFormatException)
            {
                e.printStackTrace()
            }
        }
        return false
    }

    fun getInteger(et: EditText): Int
    {
         if (isValidInteger(et))
            return Integer.parseInt(getString(et))
         else return 0
    }

    fun showToastIfEmpty(et: EditText, msg:String = ""): String? {
        val userInput = et.text.toString().trim()

        return if (userInput.isEmpty()) {

            Toast.makeText(
                et.context,
                msg,
                Toast.LENGTH_SHORT
            ).show()

            null
        } else {
            userInput
        }
    }
}