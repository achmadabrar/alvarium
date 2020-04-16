package com.bs.ecommerce.home.homepage

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import com.bs.ecommerce.R
import com.bs.ecommerce.home.homepage.model.data.Slider
import com.squareup.picasso.Picasso

class BannerSliderAdapter(private var imageUrl: List<Slider>) :
    androidx.viewpager.widget.PagerAdapter() {
    private var layoutInflater: LayoutInflater? = null

    override fun getCount(): Int = imageUrl.size

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object` as LinearLayout
    }

    override fun instantiateItem(container: ViewGroup, pos: Int): Any {
        layoutInflater =
            container.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        val view = layoutInflater!!.inflate(R.layout.layout_viewpager_slider, container, false)
        val imageView = view.findViewById<View>(R.id.image_view) as ImageView

        Picasso.with(container.context).load(imageUrl[pos].imageUrl).fit().centerInside()
            .into(imageView)
        container.addView(view)

        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as LinearLayout)
    }
}