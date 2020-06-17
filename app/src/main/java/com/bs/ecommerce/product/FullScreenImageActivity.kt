package com.bs.ecommerce.product

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bs.ecommerce.R
import com.bs.ecommerce.customViews.TouchImageViewJava
import com.bs.ecommerce.product.model.data.ImageModel
import com.bs.ecommerce.utils.loadImg
import kotlinx.android.synthetic.main.activity_full_screen_image.*

class FullScreenImageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_full_screen_image)

        setViewPager()
        onGalleryCloseBtnClicked()
    }

    private fun onGalleryCloseBtnClicked() {
        galleryCloseImageBtn?.setOnClickListener { finish() }
    }

    private fun setViewPager() {
        view_pager?.adapter = TouchImageAdapter()
        circlePageIndicator?.setViewPager(view_pager)
        circlePageIndicator?.setCurrentItem(sliderPosition)
        circlePageIndicator?.pageColor = ContextCompat.getColor(this, R.color.white)
        circlePageIndicator?.fillColor = ContextCompat.getColor(this, R.color.black)
    }

    internal inner class TouchImageAdapter : androidx.viewpager.widget.PagerAdapter() {

        // private static int[] images = { R.drawable.nature_1, R.drawable.nature_2, R.drawable.nature_3, R.drawable.nature_4, R.drawable.nature_5 };

        override fun getCount(): Int = pictureModels?.size ?: 0

        override fun instantiateItem(container: ViewGroup, position: Int): View {
            val img = TouchImageViewJava(container.context)

            img.maxZoom = 4F

            img.loadImg(pictureModels?.get(position)?.fullSizeImageUrl)

            container.addView(
                img
                , RelativeLayout.LayoutParams.MATCH_PARENT
                , RelativeLayout.LayoutParams.WRAP_CONTENT
            )


            return img
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            container.removeView(`object` as View)
        }

        override fun isViewFromObject(view: View, `object`: Any): Boolean {
            return view === `object`
        }

    }

    companion object {

        var sliderPosition = 0

        var pictureModels: List<ImageModel?>? = null
    }
}