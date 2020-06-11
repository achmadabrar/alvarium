package com.bs.ecommerce.home.manufaturer

import androidx.lifecycle.MutableLiveData
import com.bs.ecommerce.base.BaseViewModel
import com.bs.ecommerce.common.RequestCompleteListener
import com.bs.ecommerce.more.model.CommonModel
import com.bs.ecommerce.product.model.data.Manufacturer
import com.bs.ecommerce.product.model.data.ManufacturerResponse

class ManufacturerViewModel: BaseViewModel() {

    var manufacturerLD = MutableLiveData<List<Manufacturer>?>()

    fun getAllManufacturers(model: CommonModel) {

        isLoadingLD.value = true

        model.getAllManufacturers(object: RequestCompleteListener<ManufacturerResponse> {

            override fun onRequestSuccess(data: ManufacturerResponse) {
                isLoadingLD.value = false
                manufacturerLD.value = data.manufacturerList
            }

            override fun onRequestFailed(errorMessage: String) {
                isLoadingLD.value = false
                toast(errorMessage)
            }
        })
    }
}