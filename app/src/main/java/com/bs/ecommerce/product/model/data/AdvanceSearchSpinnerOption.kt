package com.bs.ecommerce.product.model.data

import com.bs.ecommerce.utils.TextUtils
import com.google.gson.annotations.SerializedName

/**
 * Created by bs206 on 3/16/18.
 */
class AdvanceSearchSpinnerOption(@field:SerializedName("Id")
                                 var id: Int, name: String)
{

/*    @SerializedName("Name")
    var name: String? = null
        get() = TextUtils.getNullSafeString(field)

    init {
        this.name = name
    }

    override fun toString(): String = name.toString()*/
}
