@file:JvmName("ExtensionsUtils")

package com.bs.ecommerce.utils

import android.R.attr.fragment
import android.app.Activity
import android.content.Context
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.annotation.AnimRes
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import com.bs.ecommerce.R
import com.bs.ecommerce.base.BaseFragment
import com.bs.ecommerce.main.MainActivity
import com.bs.ecommerce.more.OptionsFragment
import com.fasterxml.jackson.databind.ser.Serializers
import com.squareup.picasso.Picasso


/**
 * Created by bs206 on 4/10/18.
 */
fun <T1, T2> ifNotNull(value1: T1?, value2: T2?, bothNotNull: (T1, T2) -> (Unit)) {
    if (value1 != null && value2 != null)
        bothNotNull(value1, value2)

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
        .setCustomAnimations(enterAnimation, exitAnimation, popEnterAnimation, popExitAnimation)
        .replace(containerViewId, fragment)
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

fun Fragment.toast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    toast?.cancel()
    Toast.makeText(activity, message, duration).show()

}


fun ViewGroup.inflate(layoutId: Int, attachToRoot: Boolean = false): View =
    LayoutInflater.from(context).inflate(layoutId, this, attachToRoot)


fun ImageView.loadImg(imageUrl: String?) {
    if (imageUrl.isNullOrEmpty()) {
        Picasso.with(context).load(R.drawable.ic_placeholder).into(this)
    } else {
        Picasso.with(context).load(imageUrl).into(this)
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

fun EditText?.showOrHideOrRequired(isEnabledParam: Boolean = false, isRequired: Boolean = false)
{
    if(this != null)
    {
        if (isEnabledParam)
            this.visibility = View.VISIBLE
        else
            this.visibility = View.GONE

        if(isRequired)
            this.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_user, 0)
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