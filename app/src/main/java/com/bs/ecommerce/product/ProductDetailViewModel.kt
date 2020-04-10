package com.bs.ecommerce.product

import androidx.lifecycle.MutableLiveData
import com.bs.ecommerce.base.BaseViewModel
import com.bs.ecommerce.common.RequestCompleteListener
import com.bs.ecommerce.product.data.ProductDetail
import com.bs.ecommerce.product.data.ProductDetailResponse

class ProductDetailViewModel : BaseViewModel() {

    var productLiveData = MutableLiveData<ProductDetail>()

    fun getProductDetail(model: ProductDetailModel) {
        isLoadingLD.postValue(true)

        model.getProductDetail(1, object : RequestCompleteListener<ProductDetailResponse> {
            override fun onRequestSuccess(data: ProductDetailResponse) {
                isLoadingLD.postValue(false)

                productLiveData.postValue(data.data)
            }

            override fun onRequestFailed(errorMessage: String) {
                isLoadingLD.postValue(false)
            }

        })
    }

}