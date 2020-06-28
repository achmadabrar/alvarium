package com.bs.ecommerce.more.downloadableProducts.model

import com.bs.ecommerce.auth.register.data.KeyValuePair
import com.bs.ecommerce.common.RequestCompleteListener
import com.bs.ecommerce.more.downloadableProducts.model.data.DownloadableProductListResponse
import com.bs.ecommerce.product.model.data.AddressModel
import com.bs.ecommerce.product.model.data.CustomerAddressData
import com.bs.ecommerce.product.model.data.EditCustomerAddressData

interface DownloadableProductListModel {

    fun getProductList(callback: RequestCompleteListener<DownloadableProductListResponse>)

    fun download(addressId: Int, callback: RequestCompleteListener<Any?>)
}