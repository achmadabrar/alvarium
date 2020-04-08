package com.bs.ecommerce.utils

import androidx.appcompat.widget.AppCompatRadioButton
import com.bs.ecommerce.customViews.RadioGridGroupforReyMaterial

/**
 * Created by Tawfiq on 08/04/2020
 */
class ColorSelectionProcess(var radioGridGroup: RadioGridGroupforReyMaterial) {

    fun resetRadioButton(id: Int) {
        var i = 0
        val count = radioGridGroup.childCount
        while (i < count) {
            val view = radioGridGroup.getChildAt(i)

            if (view is AppCompatRadioButton && view.getId() != id) {
                view.isChecked = false
            }
            ++i
        }

    }
}
