package com.bs.ecommerce.product

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import com.bs.ecommerce.R
import com.bs.ecommerce.product.model.data.PictureModel
import com.bs.ecommerce.utils.loadImg

/**
 * Created by BS148 on 11/8/2016.
 */

class DetailsSliderAdapter(private val context: Context, private var imageUrl: List<PictureModel>) :
    androidx.viewpager.widget.PagerAdapter() {
    private var layoutInflater: LayoutInflater? = null

    internal var sliderClickListener: OnSliderClickListener? = null

    override fun getCount(): Int {
        return imageUrl.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object` as LinearLayout
    }

    override fun instantiateItem(container: ViewGroup, pos: Int): Any {
        layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = layoutInflater!!.inflate(R.layout.layout_viewpager_slider, container, false)
        val imageView = view.findViewById<View>(R.id.image_view) as ImageView
        val linearLayout = view.findViewById<View>(R.id.layout) as LinearLayout

        imageView.loadImg(imageUrl[pos].imageUrl)
        container.addView(view)
        linearLayout.setOnClickListener { v ->
            if (sliderClickListener != null) {
                sliderClickListener!!.onSliderClick(v, pos)
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
