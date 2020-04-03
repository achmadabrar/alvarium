package com.bs.ecommerce.home.category

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ExpandableListView
import android.widget.ListView


class CustomList : ExpandableListView
{

    constructor(context: Context) : super(context) {}
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var heightMeasureSpec = heightMeasureSpec
        //999999 is a size in pixels. ExpandableListView requires a maximum height in order to do measurement calculations.
        heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(999999, View.MeasureSpec.AT_MOST)
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }
}
