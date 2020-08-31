package com.bs.ecommerce.customViews

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import com.bs.ecommerce.R
import com.bs.ecommerce.db.DbHelper
import com.bs.ecommerce.utils.Const
import kotlinx.android.synthetic.main.custom_loading_layout.view.*


class ContentLoadingDialog(private val context: Context) {

    var dialog: Dialog? = null

    fun showDialog() {
        val inflater = context
            .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        val view: View = inflater.inflate(R.layout.custom_loading_layout, null, false)
        view.tvPleaseWait?.text = DbHelper.getString(Const.COMMON_PLEASE_WAIT)

        dialog = Dialog(context)
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog?.setCancelable(false)
        dialog?.setContentView(view)

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