package com.bs.ecommerce.customViews

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatButton


/**
 *
 * @author Rajiv
 *
 * A Button which aligns its text and left drawable at center. Especially useful when we have buttons which don't wrap.
 *
 * usage:
 *
 */
class DrawableAlignedButton : AppCompatButton {
    constructor(context: Context, attrs: AttributeSet?) : super(
        context,
        attrs
    ) {
    }

    constructor(context: Context) : super(context) {}
    constructor(
        context: Context,
        attrs: AttributeSet?,
        style: Int
    ) : super(context, attrs, style) {
    }

    private var mLeftDrawable: Drawable? = null

    //Overriden to work only with a left drawable.
    override fun setCompoundDrawablesWithIntrinsicBounds(
        left: Drawable?,
        top: Drawable?, right: Drawable?, bottom: Drawable?
    ) {
        if (left == null) return
        left.setBounds(0, 0, left.intrinsicWidth, left.intrinsicHeight)
        mLeftDrawable = left
    }

    override fun onDraw(canvas: Canvas) {
        //transform the canvas so we can draw both image and text at center.
        canvas.save()
        canvas.translate(2 + mLeftDrawable!!.intrinsicWidth / 2.toFloat(), 0f)
        super.onDraw(canvas)
        canvas.restore()
        canvas.save()
        val widthOfText = paint.measureText(text.toString()).toInt()
        val left = (width + widthOfText) / 2 - mLeftDrawable!!.intrinsicWidth - 2
        canvas.translate(
            left.toFloat(),
            (height - mLeftDrawable!!.intrinsicHeight) / 2.toFloat()
        )
        mLeftDrawable!!.draw(canvas)
        canvas.restore()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        var height = measuredHeight
        height = Math.max(
            height,
            mLeftDrawable!!.intrinsicHeight + paddingTop + paddingBottom
        )
        setMeasuredDimension(measuredWidth, height)
    }
}