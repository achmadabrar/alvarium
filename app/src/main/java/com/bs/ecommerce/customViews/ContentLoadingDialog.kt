package com.bs.ecommerce.customViews

import android.app.Dialog
import android.content.Context
import android.view.Window
import com.bs.ecommerce.R


class ContentLoadingDialog(private val context: Context) {

    var dialog: Dialog? = null

    fun showDialog() {
        dialog = Dialog(context)
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog?.setCancelable(false)
        dialog?.setContentView(R.layout.custom_loading_layout)

        //ivLoaderGif.loadImg()

        /*
        it was never easy to load gif into an ImageView before Glide or Others library
        and for doing this we need DrawableImageViewTarget to that ImageView
        *//*
        val imageViewTarget =
            GlideDrawableImageViewTarget(gifImageView)

        //...now load that gif which we put inside the drawble folder here with the help of Glide
        Glide.with(context)
            .load(R.drawable.loading)
            .placeholder(R.drawable.loading)
            .centerCrop()
            .crossFade()
            .into(imageViewTarget)*/

        //...finaly show it

        dialog?.show()
    }

    fun hideDialog() {
        dialog?.dismiss()
    }
}