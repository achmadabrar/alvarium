package com.bs.ecommerce.product

import androidx.lifecycle.MutableLiveData
import com.bs.ecommerce.base.BaseViewModel
import com.bs.ecommerce.common.RequestCompleteListener
import com.bs.ecommerce.product.data.AttributeControlValue
import com.bs.ecommerce.product.data.ProductDetail
import com.bs.ecommerce.product.data.ProductDetailResponse
import com.bs.ecommerce.utils.AttributeControlType
import java.util.*

class ProductDetailViewModel : BaseViewModel() {

    var productLiveData = MutableLiveData<ProductDetail>()
    var quantityLiveData = MutableLiveData(1)
    var productPriceLD = MutableLiveData<Double>()
    var toastMessageLD = MutableLiveData("")
    var selectedAttrLD = MutableLiveData<MutableMap<Long, MutableList<AttributeControlValue>>>()

    fun getProductDetail(model: ProductDetailModel) {
        isLoadingLD.postValue(true)

        model.getProductDetail(1, object : RequestCompleteListener<ProductDetailResponse> {
            override fun onRequestSuccess(data: ProductDetailResponse) {
                val attrMap = HashMap<Long, MutableList<AttributeControlValue>>()

                // sorting attribute values
                for(attr in data.data?.productAttributes!!) {
                    attr.values = attr.values.sortedBy { !it.isPreSelected }

                    val list = mutableListOf<AttributeControlValue>()

                    for(value in attr.values) {
                        if(value.isPreSelected) {
                            list.add(value)

                            //
                            if(attr.attributeControlType != AttributeControlType.Checkboxes)
                                break
                        }
                    }

                    attrMap[attr.productAttributeId] = list
                }

                isLoadingLD.postValue(false)

                productLiveData.postValue(data.data)

                val priceInFloat = data.data?.productPrice?.priceValue ?: 0f
                productPriceLD.postValue(priceInFloat.toDouble())
                selectedAttrLD.postValue(attrMap)
            }

            override fun onRequestFailed(errorMessage: String) {
                isLoadingLD.postValue(false)
            }

        })
    }

    fun incrementQuantity() {
        quantityLiveData.postValue(quantityLiveData.value?.plus(1))
    }

    fun decrementQuantity() {
        if(quantityLiveData.value!! > 1) {
            quantityLiveData.postValue(quantityLiveData.value?.minus(1))
        } else {
            toastMessageLD.postValue("Invalid quantity")
        }
    }

    private fun adjustProductPrice() {
        var price: Double = productLiveData.value?.productPrice?.priceValue?.toDouble() ?: 0.toDouble()
        val attrMap = selectedAttrLD.value!!

        for(valueList in attrMap.values) {
            for(i in valueList) {
                price = price.plus(i.priceAdjustmentValue)
            }
        }

        productPriceLD.postValue(price)
    }

    fun isAttrSelected(attrId: Long, value: AttributeControlValue) : Boolean {
        return selectedAttrLD.value?.get(attrId)?.contains(value) ?: false
    }

    fun setAttrSelected(attrId: Long, value: AttributeControlValue,
                        isSelected: Boolean, multipleSelection: Boolean) {
        val attrMap = selectedAttrLD.value!!

        if(isSelected) {
            if (!multipleSelection) attrMap[attrId] = mutableListOf()

            attrMap[attrId]?.add(value)
        } else {
            attrMap[attrId]?.remove(value)
        }

        adjustProductPrice()

        selectedAttrLD.postValue(attrMap)
    }

}