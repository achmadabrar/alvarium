package com.bs.ecommerce.product.viewModel

import androidx.lifecycle.MutableLiveData
import com.bs.ecommerce.auth.register.data.KeyValuePair
import com.bs.ecommerce.base.BaseViewModel
import com.bs.ecommerce.common.RequestCompleteListener
import com.bs.ecommerce.product.model.data.AttributeControlValue
import com.bs.ecommerce.product.model.data.ProductDetail
import com.bs.ecommerce.product.model.data.ProductDetailResponse
import com.bs.ecommerce.product.model.ProductDetailModel
import com.bs.ecommerce.utils.AttributeControlType
import com.bs.ecommerce.utils.showLog
import java.util.*

class ProductDetailViewModel : BaseViewModel() {

    var productLiveData = MutableLiveData<ProductDetail>()
    var quantityLiveData = MutableLiveData(1)
    var productPriceLD = MutableLiveData<Double>()
    var toastMessageLD = MutableLiveData("")
    var selectedAttrLD = MutableLiveData<MutableMap<Long, MutableList<AttributeControlValue>>>()

    fun getProductDetail(prodId: Long, model: ProductDetailModel) {
        isLoadingLD.postValue(true)

        model.getProductDetailModel(prodId, object : RequestCompleteListener<ProductDetailResponse>
        {
            override fun onRequestSuccess(data: ProductDetailResponse)
            {
                val attrMap = HashMap<Long, MutableList<AttributeControlValue>>()

                // sorting attribute values
                for(attr in data.data?.productAttributes!!)
                {
                    attr.values = attr.values.sortedBy { !it.isPreSelected }

                    val list = mutableListOf<AttributeControlValue>()

                    for(value in attr.values)
                    {
                        if(value.isPreSelected)
                        {
                            list.add(value)

                            if(attr.attributeControlType != AttributeControlType.Checkboxes)
                                break
                        }
                    }

                    attrMap[attr.productAttributeId] = list
                }

                isLoadingLD.postValue(false)

                productLiveData.postValue(data.data)

                val price = data.data?.productPrice?.priceValue ?: 0.toDouble()
                productPriceLD.postValue(price)
                selectedAttrLD.postValue(attrMap)
            }

            override fun onRequestFailed(errorMessage: String) {
                isLoadingLD.postValue(false)
            }

        })
    }

    fun addProductToCartModel(selectedAttributeMap: MutableMap<Long, MutableList<AttributeControlValue>>, model: ProductDetailModel)
    {

        val productAttributePrefix = "product_attribute"

        val keyValueList = ArrayList<KeyValuePair>()

        for ((key, valueList) in selectedAttributeMap)
        {
            if(valueList.isNotEmpty())
            {
                val selectedIdList = valueList.map { it.id }

                for(i in selectedIdList.indices)
                {
                    val keyValuePair = KeyValuePair()
                    keyValuePair.key = "${productAttributePrefix}_${key}"
                    keyValuePair.value = selectedIdList[i].toString()
                    keyValueList.add(keyValuePair)

                    "key_value".showLog(" Key : $key    values: ${selectedIdList[i]}")
                }
            }
        }


        isLoadingLD.postValue(true)

       /* model.addProductToCartModel(keyValueList, object : RequestCompleteListener<LoginResponse>
        {
            override fun onRequestSuccess(data: LoginResponse)
            {
                isLoadingLD.postValue(false)

                loginResponseLD.postValue(data)
            }

            override fun onRequestFailed(errorMessage: String)
            {
                isLoadingLD.postValue(false)
            }
        })*/
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
        var price: Double = productLiveData.value?.productPrice?.priceValue ?: 0.toDouble()
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

        if(isSelected)
        {
            if (!multipleSelection) attrMap[attrId] = mutableListOf()

            attrMap[attrId]?.add(value)
        }
        else
            attrMap[attrId]?.remove(value)


        adjustProductPrice()

        selectedAttrLD.postValue(attrMap)
    }

}