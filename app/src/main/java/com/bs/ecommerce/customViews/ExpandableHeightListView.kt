package com.bs.ecommerce.customViews

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.ExpandableListView

/**
 * Created by Ashraful on 11/25/2015.
 */
class ExpandableHeightListView : ExpandableListView {

    var isExpanded = false

    constructor(context: Context, attrs: AttributeSet, defaultStyle: Int) : super(context, attrs, defaultStyle) {}
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    public override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        // HACK!  TAKE THAT ANDROID!
        if (isExpanded) {

            val expandSpec = View.MeasureSpec.makeMeasureSpec(
                    Integer.MAX_VALUE shr 2, View.MeasureSpec.AT_MOST)
            super.onMeasure(widthMeasureSpec, expandSpec)

            val params = layoutParams
            params.height = measuredHeight
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        }
    }


}