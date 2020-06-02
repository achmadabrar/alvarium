@file:JvmName("ExtensionsUtils")

package com.bs.ecommerce.utils

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.net.ConnectivityManager
import android.os.Build
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.webkit.WebView
import android.widget.*
import androidx.annotation.AnimRes
import androidx.annotation.IdRes
import androidx.asynclayoutinflater.view.AsyncLayoutInflater
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.bs.ecommerce.R
import com.bs.ecommerce.base.BaseFragment
import com.bs.ecommerce.db.DbHelper
import com.bs.ecommerce.main.MainActivity
import com.squareup.picasso.Picasso
import java.util.*


/**
 * Created by bs206 on 4/10/18.
 */
fun <T1, T2> ifNotNull(value1: T1?, value2: T2?, bothNotNull: (T1, T2) -> (Unit)) {
    if (value1 != null && value2 != null)
        bothNotNull(value1, value2)

}

fun Fragment.inflateAsync(resId: Int, parent: ViewGroup?, f: (v: View) -> Unit) {

    val inflater = AsyncLayoutInflater(requireContext())

    inflater.inflate(resId, parent) { inflatedView, _, _ ->
        f(inflatedView)
    }
}

fun Fragment.replaceFragmentSafely(
    fragment: Fragment,
    //tag: String,
    //allowStateLoss: Boolean = false,
    @IdRes containerViewId: Int = R.id.layoutFrame,
    @AnimRes enterAnimation: Int = 0,
    @AnimRes exitAnimation: Int = 0,
    @AnimRes popEnterAnimation: Int = 0,
    @AnimRes popExitAnimation: Int = 0
) {
    val ft = requireActivity().supportFragmentManager
        .beginTransaction()
        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        .setCustomAnimations(enterAnimation, exitAnimation, popEnterAnimation, popExitAnimation)
        .replace(containerViewId, fragment, fragment::class.java.simpleName)
        .addToBackStack(fragment::class.java.simpleName)

    if (!requireActivity().supportFragmentManager.isStateSaved) {
        ft.commit()
    }
/*    else if (allowStateLoss)
    {
        ft?.commitAllowingStateLoss()
    }*/
}

inline fun <reified T> MainActivity.createIfNotInBackStack(fragment: BaseFragment) {
    //val fragment = Class.forName(fragmentName).newInstance() as BaseFragment

    if(supportFragmentManager.findFragmentById(R.id.layoutFrame) !is T) {

        val backStateName: String = fragment::class.java.simpleName

        val fragmentPopped = supportFragmentManager.popBackStackImmediate(backStateName, 0)

        if (!fragmentPopped) {
            supportFragmentManager.beginTransaction().replace(
                R.id.layoutFrame,
                fragment
            ).addToBackStack(backStateName).commit()
        }
    }
}


private var toast: Toast? = null

fun Activity.toast(resourceId: Int) = toast(getString(resourceId))

fun Activity.toast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    toast?.cancel()
    Toast.makeText(this, message, duration).show()

}

fun Fragment.toast(resourceId: Int) = toast(getString(resourceId))

fun Fragment.toast(message: String?, duration: Int = Toast.LENGTH_SHORT) {
    message?.apply {
        toast?.cancel()
        Toast.makeText(activity, message, duration).show()
    }
}


fun ViewGroup.inflate(layoutId: Int, attachToRoot: Boolean = false): View =
    LayoutInflater.from(context).inflate(layoutId, this, attachToRoot)


fun ImageView.loadImg(imageUrl: String?) {
    if (imageUrl.isNullOrEmpty()) {
        Picasso.with(context).load(R.drawable.ic_placeholder).into(this)
    } else {
        Picasso.with(context)
            .load(imageUrl)
            .placeholder(R.drawable.ic_placeholder)
            .fit()
            .centerInside()
            .into(this)
    }
}

fun <T> androidLazy(initializer: () -> T): Lazy<T> = lazy(LazyThreadSafetyMode.NONE, initializer)


fun isLollipopOrAbove(func: () -> Unit) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        func()

}


inline fun consume(f: () -> Unit): Boolean {
    f()
    return true
}

fun String.showLog(msg: String) = Log.v(toString(), msg)

fun Activity.hideKeyboard(): Boolean {
    val view = currentFocus

    view?.let {
        val inputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        return inputMethodManager.hideSoftInputFromWindow(
            it.windowToken,
            InputMethodManager.HIDE_NOT_ALWAYS
        )
    }
    return false
}

fun EditText?.showOrHideOrRequired(isEnabledParam: Boolean = false, isRequired: Boolean = false, value: String? = null, hintText :String? = null)
{
    if(this != null)
    {
        if (isEnabledParam)
            this.visibility = View.VISIBLE
        else
            this.visibility = View.GONE

        if(isRequired)
            this.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_star_formular, 0)

        if(value != null && value.isNotEmpty())
            this.setText(value)

        if(hint != null)
            this.hint = hintText
    }


}

fun LinearLayout?.showOrHide(isEnabledParam: Boolean = false)
{
    if(this != null)
    {
        if (isEnabledParam)
            this.visibility = View.VISIBLE
        else
            this.visibility = View.GONE
    }


}

fun CharSequence?.isEmailValid() = !isNullOrEmpty() && Patterns.EMAIL_ADDRESS.matcher(this!!).matches()

fun WebView.show(text : String, nightMode: Boolean = true)
{
    val htmlRtlHeader = "dir=\"rtl\" lang=\"\""

    var htmlColorHeader =""

    if(nightMode)
        htmlColorHeader = "<style type=\"text/css\">body{color: #fff; background-color: #222222;}</style>"


    val htmlFontHeader = "<style>@font-face {font-family: 'CustomFont';src: url('font/montserrat_regular.ttf');}#font {font-family: 'montserrat_regular';}</style>"


    val htmlFullHeader = ("<head>$htmlColorHeader$htmlFontHeader</head>")

    if (Locale.getDefault().language == Language.ARABIC)
        this.loadDataWithBaseURL("file:///android_asset/",
            "<html$htmlRtlHeader>$htmlFullHeader<body>$text</body></html>", "text/html", "UTF-8", "")
    else
        this.loadDataWithBaseURL("file:///android_asset/",
            "<html>$htmlFullHeader<body>$text</body></html>", "text/html", "UTF-8", "")

}

fun WebView.show(text: String, backgroundColorRes: Int, textColorRes: Int = R.color.whiteOrBlack) {

    val webViewBg = "#" + Integer.toHexString(
        ContextCompat.getColor(
            context, backgroundColorRes
        ) and 0x00ffffff
    )

    val textColor = "#" + Integer.toHexString(
        ContextCompat.getColor(
            context, textColorRes
        ) and 0x00ffffff
    )

    val htmlRtlHeader = "dir=\"rtl\" lang=\"\""

    var htmlColorHeader =
            "<style type=\"text/css\">body{color: $textColor; background-color: $webViewBg;}</style>"


    val htmlFontHeader =
        "<style>@font-face {font-family: 'CustomFont';src: url('font/montserrat_regular.ttf');}#font {font-family: 'montserrat_regular';}</style>"


    val htmlFullHeader = ("<head>$htmlColorHeader$htmlFontHeader</head>")

    if (Locale.getDefault().language == Language.ARABIC)
        this.loadDataWithBaseURL(
            "file:///android_asset/",
            "<html$htmlRtlHeader>$htmlFullHeader<body>$text</body></html>", "text/html", "UTF-8", ""
        )
    else
        this.loadDataWithBaseURL(
            "file:///android_asset/",
            "<html>$htmlFullHeader<body>$text</body></html>", "text/html", "UTF-8", ""
        )

}

fun String.isNumeric(): Boolean
{
    return try {
        this.toDouble()
        true
    } catch (e: NumberFormatException) {
        false
    }
}

fun TextView?.showTextPendingCalculationOnCheckout()
{
    this?.text = DbHelper.getString(Const.CALCULATED_DURING_CHECKOUT)
    this?.textSize = 13F
    this?.maxLines = 2
    this?.setTextColor(ContextCompat.getColor(context, R.color.pink))
}

fun Context.isOnline(): Boolean {
    val cm = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val networkInfo = cm.activeNetworkInfo
    return networkInfo?.isConnectedOrConnecting ?: false
}