@file:JvmName("ExtensionsUtils")

package com.bs.ecommerce.utils

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Rect
import android.graphics.Shader
import android.os.Build
import android.text.TextPaint
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.inputmethod.InputMethodManager
import android.webkit.WebView
import android.widget.*
import androidx.annotation.AnimRes
import androidx.annotation.IdRes
import androidx.asynclayoutinflater.view.AsyncLayoutInflater
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bs.ecommerce.R
import com.bs.ecommerce.base.BaseActivity
import com.bs.ecommerce.base.BaseFragment
import com.bs.ecommerce.db.DbHelper
import com.bs.ecommerce.main.MainActivity
import com.google.android.material.textfield.TextInputLayout
import com.squareup.picasso.Picasso
import kotlin.math.floor

const val MAX_ALLOWED_FRAGMENT = 4

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
    try {
        val fragManager = requireActivity().supportFragmentManager

        if(fragManager.backStackEntryCount > MAX_ALLOWED_FRAGMENT) {
            "test_".showLog("Pop BS")
            fragManager.popBackStack()
        } else {
            "test_".showLog("No need to pop BS. ${fragManager.backStackEntryCount}")
        }

        val ft = fragManager
            .beginTransaction()
            //.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            //.setCustomAnimations(enterAnimation, exitAnimation, popEnterAnimation, popExitAnimation)
            .hide(this)
            .add(containerViewId, fragment, fragment::class.java.simpleName)
            .addToBackStack(fragment::class.java.simpleName)

        if (!fragManager.isStateSaved) {
            ft.commit()
        }
    } catch(e: Exception) {
        e.printStackTrace()
    }
}

inline fun <reified T> MainActivity.createIfNotInBackStack(fragment: BaseFragment) {
    //val fragment = Class.forName(fragmentName).newInstance() as BaseFragment

    try {
        if(supportFragmentManager.findFragmentById(R.id.layoutFrame) !is T) {

            val backStateName: String = fragment::class.java.simpleName

            val topFragment = supportFragmentManager.findFragmentById(R.id.layoutFrame)

            val fragmentPopped = supportFragmentManager.popBackStackImmediate(backStateName, 0)

            if (!fragmentPopped) {
                supportFragmentManager.beginTransaction()
                    .hide(topFragment!!).add(
                        R.id.layoutFrame,
                        fragment
                    ).addToBackStack(backStateName).commit()
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun BaseActivity.replaceFragment(
    fragment: Fragment,
    @IdRes containerViewId: Int = R.id.layoutFrame
) {
    try {
        val fragManager = supportFragmentManager

        if(fragManager.backStackEntryCount > MAX_ALLOWED_FRAGMENT) {
            "test_".showLog("Pop BS")
            fragManager.popBackStack()
        } else {
            "test_".showLog("No need to pop BS. ${fragManager.backStackEntryCount}")
        }

        val topFragment = fragManager.findFragmentById(containerViewId)

        val ft = fragManager
            .beginTransaction()
            .hide(topFragment!!)
            .add(containerViewId, fragment, fragment::class.java.simpleName)
            .addToBackStack(fragment::class.java.simpleName)

        if (!fragManager.isStateSaved) {
            ft.commit()
        }
    } catch(e: Exception) {
        e.printStackTrace()
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


fun ImageView.loadJustImg(
    imageUrl: String?,
    showPlaceholder: Boolean = true
) {
    if (imageUrl.isNullOrEmpty()) {
        if (showPlaceholder)
            Picasso.with(context)
                .load(R.drawable.ic_placeholder)
                .into(this)
    } else {
        if (showPlaceholder) {
            Picasso.with(context)
                .load(imageUrl)
                .placeholder(R.drawable.ic_placeholder)
                .error(R.drawable.ic_placeholder)
                .into(this)
        } else {
            Picasso.with(context)
                .load(imageUrl)
                .into(this)
        }
    }
}


fun ImageView.loadImg(
    imageUrl: String?,
    placeHolder: Int? = R.drawable.ic_placeholder,
    roundedCorner: Boolean = true
) {
    if (imageUrl.isNullOrEmpty()) {
        if(placeHolder!=null) Picasso.with(context).load(placeHolder).into(this)
    } else {

        if(placeHolder!=null) {
            val reqCreator = Picasso.with(context)
                .load(imageUrl)

            if(roundedCorner)
                reqCreator.transform(RoundedCornersTransform())

            reqCreator
                .placeholder(R.drawable.ic_placeholder)
                .error(R.drawable.ic_placeholder)
                .fit()
                .centerInside()
                .into(this)
        } else {
            Picasso.with(context)
                .load(imageUrl)
                .fit()
                .centerInside()
                .into(this)
        }
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
        val view: Any? = if(this.parent.parent is TextInputLayout) this.parent.parent else null

        if (isEnabledParam)
            if(view is TextInputLayout) view.visibility = View.VISIBLE else this.visibility = View.VISIBLE
        else
            if(view is TextInputLayout) view.visibility = View.GONE else this.visibility = View.GONE

        if(isRequired)
            this.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_star_formular, 0)

        if(value != null && value.isNotEmpty())
            this.setText(value)

        if(hintText != null) {
            if(view is TextInputLayout)
                view.hint = hintText
            else
                this.hint = hintText

            this.tag = hintText
        }
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

fun WebView.show(text : String, nightMode: Boolean = false)
{
    val htmlRtlHeader = "dir=\"rtl\" lang=\"\""

    var htmlColorHeader =""

    if(nightMode)
        htmlColorHeader = "<style type=\"text/css\">body{color: #fff; background-color: #222222;}</style>"


    val htmlFontHeader = "<style>@font-face {font-family: 'CustomFont';src: url('font/montserrat_regular.ttf');}#font {font-family: 'montserrat_regular';}</style>"


    val htmlFullHeader = ("<head>$htmlColorHeader$htmlFontHeader</head>")

    try {
        if (resources.configuration.layoutDirection == View.LAYOUT_DIRECTION_RTL)
            this.loadDataWithBaseURL("file:///android_asset/",
                "<html $htmlRtlHeader>$htmlFullHeader<body>$text</body></html>", "text/html", "UTF-8", "")
        else
            this.loadDataWithBaseURL("file:///android_asset/",
                "<html>$htmlFullHeader<body>$text</body></html>", "text/html", "UTF-8", "")
    } catch (e: Exception) {
        e.printStackTrace()
    }
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

    val htmlColorHeader =
            "<style type=\"text/css\">body{color: $textColor; background-color: $webViewBg;}</style>"


    val htmlFontHeader =
        "<style>@font-face {font-family: 'CustomFont';src: url('font/montserrat_regular.ttf');}#font {font-family: 'montserrat_regular';}</style>"


    val htmlFullHeader = ("<head>$htmlColorHeader$htmlFontHeader</head>")

    try {
        if (resources.configuration.layoutDirection == View.LAYOUT_DIRECTION_RTL)
            this.loadDataWithBaseURL(
                "file:///android_asset/",
                "<html $htmlRtlHeader>$htmlFullHeader<body>$text</body></html>", "text/html", "UTF-8", ""
            )
        else
            this.loadDataWithBaseURL(
                "file:///android_asset/",
                "<html>$htmlFullHeader<body>$text</body></html>", "text/html", "UTF-8", ""
            )
    } catch (e: Exception) {
        e.printStackTrace()
    }

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
    this?.setTextColor(ContextCompat.getColor(context, R.color.themePrimary))
}

fun TextView.setDrawableEnd(resId: Int) {
    val config: Configuration = resources.configuration

    if (config.layoutDirection == View.LAYOUT_DIRECTION_RTL) {
        setCompoundDrawablesWithIntrinsicBounds(resId, 0, 0, 0)
    } else {
        setCompoundDrawablesWithIntrinsicBounds(0, 0, resId, 0)
    }
}

fun TextView.setGradient() {
    val paint: TextPaint = this.paint
    val width: Float = paint.measureText(this.text.toString())

    val textShader: Shader = LinearGradient(
        0f, 0f, width, this.textSize, intArrayOf(
            Color.parseColor("#F97C3C"),
            Color.parseColor("#FDB54E"),
            Color.parseColor("#64B678"),
            Color.parseColor("#478AEA"),
            Color.parseColor("#8446CC")
        ), null, Shader.TileMode.CLAMP
    )
    this.paint.shader = textShader
}

fun RecyclerView.calculateColumnsForGridLayout() {

    layoutManager = GridLayoutManager(context, 2)

    viewTreeObserver.addOnGlobalLayoutListener(
        object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                viewTreeObserver?.removeOnGlobalLayoutListener(this)

                val viewWidth = measuredWidth
                val cardViewWidth =
                    resources?.getDimension(R.dimen.product_item_size_grid)

                val newSpanCount = floor((viewWidth / cardViewWidth!!).toDouble()).toInt()

                (layoutManager as GridLayoutManager).spanCount = newSpanCount
                (layoutManager as GridLayoutManager).requestLayout()

                val decoration = object : RecyclerView.ItemDecoration() {
                    override fun getItemOffsets(
                        outRect: Rect,
                        view: View,
                        parent: RecyclerView,
                        state: RecyclerView.State
                    ) {
                        super.getItemOffsets(outRect, view, parent, state)
                        outRect.set(15, 15, 15, 15)
                    }
                }

                addItemDecoration(decoration)
            }
        })
}
