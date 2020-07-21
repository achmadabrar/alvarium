package com.bs.ecommerce.catalog.product

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.viewpager.widget.PagerAdapter
import com.bs.ecommerce.R
import com.bs.ecommerce.catalog.common.ImageModel
import com.bs.ecommerce.utils.loadImg


class DetailsSliderAdapter(private val context: Context, private var imageUrl: List<ImageModel?>?) :
    PagerAdapter() {

    private var sliderClickListener: OnSliderClickListener? = null

    override fun getCount(): Int {
        return imageUrl?.size ?: 0
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object` as LinearLayout
    }

    override fun instantiateItem(container: ViewGroup, pos: Int): Any {
        val layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = layoutInflater.inflate(R.layout.layout_viewpager_slider, container, false)

        val imageView = view.findViewById<View>(R.id.image_view) as ImageView
        val linearLayout = view.findViewById<View>(R.id.layout) as LinearLayout

        imageView.loadImg(imageUrl?.get(pos)?.imageUrl, roundedCorner = false)
        container.addView(view)

        linearLayout.setOnClickListener { v ->
            sliderClickListener?.apply {
                onSliderClick(v, pos)
            }
        }

        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as LinearLayout)
    }

    fun setOnSliderClickListener(sliderClickListener: OnSliderClickListener) {
        this.sliderClickListener = sliderClickListener
    }

    interface OnSliderClickListener {
        fun onSliderClick(view: View, position: Int)
    }
}
