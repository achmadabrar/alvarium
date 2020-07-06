package com.bs.ecommerce.catalog.common

import com.google.gson.annotations.SerializedName

/**
 * Created by bs156 on 22-Dec-16.
 */

class ProductSpecification {
    @SerializedName("SpecificationAttributeId") var id: Int = 0

    @SerializedName("SpecificationAttributeName") var name: String? = null

    @SerializedName("ValueRaw") var value: String? = null
}
