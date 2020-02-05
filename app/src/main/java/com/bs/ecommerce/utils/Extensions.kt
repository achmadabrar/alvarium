@file:JvmName("ExtensionsUtils")

package com.bs.ecommerce.utils

import android.app.Activity
import android.os.Build
import androidx.annotation.AnimRes
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import com.bs.ecommerce.R
import com.squareup.picasso.Picasso

/**
 * Created by bs206 on 4/10/18.
 */
fun <T1, T2> ifNotNull(value1: T1?, value2: T2?, bothNotNull: (T1, T2) -> (Unit))
{
    if (value1 != null && value2 != null)
        bothNotNull(value1, value2)

}

fun Fragment.replaceFragmentSafely(fragment: androidx.fragment.app.Fragment,
                                            //tag: String,
                                            //allowStateLoss: Boolean = false,
                                                         @IdRes containerViewId: Int = R.id.container,
                                                         @AnimRes enterAnimation: Int = 0,
                                                         @AnimRes exitAnimation: Int = 0,
                                                         @AnimRes popEnterAnimation: Int = 0,
                                                         @AnimRes popExitAnimation: Int = 0)
{
    val ft = fragmentManager
            ?.beginTransaction()
            ?.setCustomAnimations(enterAnimation, exitAnimation, popEnterAnimation, popExitAnimation)
            ?.replace(containerViewId, fragment)
            ?.addToBackStack(null)

    if (!fragmentManager?.isStateSaved!!)
    {
        ft?.commit()
    }
/*    else if (allowStateLoss)
    {
        ft?.commitAllowingStateLoss()
    }*/
}



var toast :Toast? = null

fun Activity.toast(resourceId: Int) = toast(getString(resourceId))

fun Activity.toast(message: String, duration: Int = Toast.LENGTH_SHORT)
{
    toast?.cancel()
    Toast.makeText(this, message, duration).show()

}

fun androidx.fragment.app.Fragment.toast(resourceId: Int) = toast(getString(resourceId))

fun androidx.fragment.app.Fragment.toast(message: String, duration: Int = Toast.LENGTH_SHORT)
{
    toast?.cancel()
    Toast.makeText(activity, message, duration).show()

}



fun ViewGroup.inflate(layoutId: Int, attachToRoot: Boolean = false): View

    = LayoutInflater.from(context).inflate(layoutId, this, attachToRoot)





fun ImageView.loadImg(imageUrl: String)
{
    if (TextUtils.isEmpty(imageUrl))
    {
        //Picasso.with(context).load(R.mipmap.ic_launcher_nopstation).into(this)
    }
    else
    {
        //Picasso.with(context).load(imageUrl).into(this)
    }
}

fun <T> androidLazy(initializer: () -> T) : Lazy<T> = lazy(LazyThreadSafetyMode.NONE, initializer)


fun isLollipopOrAbove(func: () -> Unit)
{
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        func()

}


inline fun consume(f: () -> Unit): Boolean
{
    f()
    return true
}
fun String.showLog(msg : String) = Log.v(toString(), msg)
