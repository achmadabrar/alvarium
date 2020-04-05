package com.bs.ecommerce.customViews

import android.content.Context
import android.widget.LinearLayout
import androidx.fragment.app.Fragment

import com.bs.ecommerce.auth.data.ProductAttribute

/**
 * Created by Ashraful on 11/26/2015.
 */
class CustomerAttributeViews(context: Context,
                             attributes: List<ProductAttribute>,
                             layout: LinearLayout,
                             currentFragment: Fragment) : ProductAttributeViews(context, attributes, layout, currentFragment)
{


    internal val customerAttributePrefix = "customer_attribute"

    override fun getKey(productAttribute: ProductAttribute): String = String.format("%s_%s", customerAttributePrefix, productAttribute.id)


    override fun callPriceWebservice()
    {
        //RetroClient.api.applyCustomerAttribute(productAttribute).enqueue(CustomCB())
    }



}
