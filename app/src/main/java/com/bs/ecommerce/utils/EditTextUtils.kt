package com.bs.ecommerce.utils

import android.widget.EditText

/**
 * Created by bs206 on 3/15/18.
 */
class EditTextUtils {

    fun isValidString(et: EditText): Boolean
    {
        val string = et.text.toString()

        if (string == null || string.isEmpty())
            return false
         else return true
    }

    fun getString(et: EditText): String
    {
        val string = et.text.toString()
        if (string == null || string.length <= 0)
            return ""
        else return string
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
}