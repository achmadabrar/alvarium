package com.bs.ecommerce.home.category

import android.content.Context
import android.widget.ExpandableListView

class SecondLevelExpandableListView(context: Context) :
    ExpandableListView(context) {

    override fun onMeasure(
        widthMeasureSpec: Int,
        heightMeasureSpec: Int
    ) {
        //999999 is a size in pixels. ExpandableListView requires a maximum height in order to do measurement calculations.
        val mHeightMeasureSpec: Int = MeasureSpec.makeMeasureSpec(999999, MeasureSpec.AT_MOST)
        super.onMeasure(widthMeasureSpec, mHeightMeasureSpec)
    }
}