package com.bs.ecommerce.customViews

import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatRadioButton

/**
 * Created by Ashraful on 12/9/2015.
 */
class MethodSelectionProcess(var radioGridGroup: RadioGridGroupforReyMaterial)
{

    fun resetRadioButton(id: Int)
    {
        var i = 0
        val count = radioGridGroup.childCount
        while (i < count)
        {
            val view = radioGridGroup.getChildAt(i)

            if (view is LinearLayout)
            {
                val viewGroup = view as ViewGroup
                var j = 0
                val count2 = viewGroup.childCount
                while (j < count2)
                {
                    val radiobView = viewGroup.getChildAt(j)

                    if (radiobView is AppCompatRadioButton)
                    {
                        if (radiobView.getId() != id)
                            radiobView.isChecked = false
                    }
                    ++j
                }
            }
            ++i
        }

    }
}
