package com.bs.ecommerce.utils

import android.content.Context
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.AppCompatSpinner
import com.bs.ecommerce.R
import com.bs.ecommerce.checkout.model.data.AvailableCountry
import com.bs.ecommerce.checkout.model.data.AvailableState

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

    fun showToastIfEmpty(et: EditText, msg: String): String? {
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

    fun populateCountrySpinner(
        context: Context,
        spinner: AppCompatSpinner,
        availableCountries: List<AvailableCountry>,
        itemClickListener: ItemClickListener<AvailableCountry>
    ) {

        val countryAdapter: ArrayAdapter<AvailableCountry> =
            ArrayAdapter<AvailableCountry>(context, R.layout.simple_spinner_item, availableCountries)

        countryAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)

        spinner.adapter = countryAdapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) = itemClickListener.onClick(view, position, availableCountries[position])


            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        // set selected country
        availableCountries.indexOfFirst { it.selected }.also {
            if (it >= 0) spinner.setSelection(it)
        }
    }

    fun populateStateSpinner(
        context: Context,
        spinner: AppCompatSpinner,
        selectedStateId: Int?,
        states: List<AvailableState>) {

        val spinnerAdapter: ArrayAdapter<AvailableState> =
            ArrayAdapter<AvailableState>(context, R.layout.simple_spinner_item, states)

        spinnerAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)

        spinner.adapter = spinnerAdapter

        spinner.visibility = View.VISIBLE

        // set selected country
        states.indexOfFirst { it.id == selectedStateId }.also {
            if (it >= 0) spinner.setSelection(it)
        }
    }
}