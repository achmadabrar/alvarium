package com.bs.ecommerce.utils

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView


class RecyclerViewMargin (
    private val margin: Int,
    private val columns: Int,
    private val isVertical: Boolean
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)

        val position = parent.getChildLayoutPosition(view)
        outRect.right = margin

        if (isVertical) {
            outRect.bottom = margin
            //we only add top margin to the first row
            if (position < columns) {
                outRect.top = margin;
            }
            //add left margin only to the first column
            if (position % columns == 0) {
                outRect.left = margin;
            }
        }
    }


}