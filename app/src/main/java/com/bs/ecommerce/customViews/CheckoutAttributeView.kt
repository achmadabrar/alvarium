package com.bs.ecommerce.customViews

import android.content.Context
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import com.bs.ecommerce.auth.data.ProductAttribute

/**
 * Created by Ashraful on 12/10/2015.
 */
class CheckoutAttributeView(context: Context, attributes: List<ProductAttribute>, layout: LinearLayout, currentFragment: Fragment) : ProductAttributeViews(context, attributes, layout, currentFragment)
{

    internal val checkoutAttributePrefix = "checkout_attribute"

    override fun getKey(productAttribute: ProductAttribute): String
    {

        return "${checkoutAttributePrefix}_${productAttribute.id}"
    }

    override fun callPriceWebservice()
    {
       // currentFragment?.view?.let {    RetroClient.api.applyCheckoutAttribute(productAttribute).enqueue(CustomCB(it))  }
    }
}
