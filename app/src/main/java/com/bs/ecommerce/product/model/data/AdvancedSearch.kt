package com.bs.ecommerce.product.model.data

import com.google.gson.annotations.SerializedName

/**
 * Created by bs-110 on 1/13/2016.
 */
class AdvancedSearch {

    @SerializedName("q") var query: String? = null

    @SerializedName("adv") var isAdvanceSearchSelected: Boolean = false

    @SerializedName("cid") var categoryId: Int = 0

    @SerializedName("isc") var isSearchInSubcategory: Boolean = false

    @SerializedName("mid") var manufacturerId: Int = 0

    @SerializedName("pf") var priceFrom: String? = null

    @SerializedName("pt") var priceTo: String? = null

    @SerializedName("sid") var isSearchInDescription: Boolean = false

    @SerializedName("asv") var isSearchVendor: Boolean = false

    @SerializedName("vid") var vendorId: Int = 0

    constructor() {}

    constructor(query: String) {
        this.query = query
    }
}
