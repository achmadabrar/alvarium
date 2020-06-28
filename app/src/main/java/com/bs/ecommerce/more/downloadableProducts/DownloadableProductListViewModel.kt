package com.bs.ecommerce.more.downloadableProducts

import androidx.lifecycle.MutableLiveData
import com.bs.ecommerce.auth.register.data.KeyValuePair
import com.bs.ecommerce.base.BaseViewModel
import com.bs.ecommerce.checkout.CheckoutViewModel
import com.bs.ecommerce.common.RequestCompleteListener
import com.bs.ecommerce.more.downloadableProducts.model.DownloadableProductListModel
import com.bs.ecommerce.more.downloadableProducts.model.data.DownloadableProductList
import com.bs.ecommerce.more.downloadableProducts.model.data.DownloadableProductListResponse
import com.bs.ecommerce.more.model.CustomerAddressModel
import com.bs.ecommerce.product.model.data.AddressModel
import com.bs.ecommerce.product.model.data.CustomerAddressData
import com.bs.ecommerce.product.model.data.EditCustomerAddressData

class DownloadableProductListViewModel: BaseViewModel() {

    val productListLD = MutableLiveData<DownloadableProductList>()

    fun getProductList(model: DownloadableProductListModel) {

        isLoadingLD.value = true

        model.getProductList(object : RequestCompleteListener<DownloadableProductListResponse> {
            override fun onRequestSuccess(data: DownloadableProductListResponse) {
                isLoadingLD.value = false

                productListLD.value = data.downloadableProductList
            }

            override fun onRequestFailed(errorMessage: String) {
                isLoadingLD.value = false
                toast(errorMessage)
            }

        })
    }

    fun updateAddress(address: AddressModel, customAttributes: List<KeyValuePair>, model: DownloadableProductListModel) {

        if(isLoadingLD.value == true)
            return

        isLoadingLD.value = true

        /*model.download(address, customAttributes, object : RequestCompleteListener<Any?> {
            override fun onRequestSuccess(data: Any?) {
                isLoadingLD.value = false
            }

            override fun onRequestFailed(errorMessage: String) {
                isLoadingLD.value = false
                toast(errorMessage)
            }

        })*/
    }


}