package com.bs.ecommerce.product.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.bs.ecommerce.auth.register.data.KeyValuePair
import com.bs.ecommerce.base.BaseViewModel
import com.bs.ecommerce.common.RequestCompleteListener
import com.bs.ecommerce.home.homepage.model.data.HomePageProductResponse
import com.bs.ecommerce.product.model.ProductDetailModel
import com.bs.ecommerce.product.model.data.*
import com.bs.ecommerce.utils.AttributeControlType
import com.bs.ecommerce.utils.showLog
import java.util.*

class ProductDetailViewModel : BaseViewModel() {

    var productLiveData = MutableLiveData<ProductDetail>()
    var relatedProductsLD = MutableLiveData<List<ProductSummary>>()
    var similarProductsLD = MutableLiveData<List<ProductSummary>>()

    var quantityLiveData = MutableLiveData(1)
    var productPriceLD = MutableLiveData<Double>()
    var isInvalidProductLD = MutableLiveData<Boolean>()
    var selectedAttrLD = MutableLiveData<MutableMap<Long, MutableList<AttributeControlValue>>>()

    var cartProductsCountLD = MutableLiveData<Int>()

    var addToCartResponseLD = MutableLiveData<AddToCartResponse>()

    fun getProductDetail(prodId: Long, model: ProductDetailModel) {
        isLoadingLD.value = true

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

                isLoadingLD.value = false

                productLiveData.value = data.data

                val price = data.data?.productPrice?.priceValue ?: 0.toDouble()
                productPriceLD.value = price
                selectedAttrLD.value = attrMap
            }

            override fun onRequestFailed(errorMessage: String) {
                isLoadingLD.value = false
                isInvalidProductLD.value = true
            }

        })
    }

    fun getRelatedProducts(prodId: Long, thumbnailSizePx: Int, model: ProductDetailModel) {

        model.getRelatedProducts(prodId, thumbnailSizePx, object: RequestCompleteListener<HomePageProductResponse> {

            override fun onRequestSuccess(data: HomePageProductResponse) {
                relatedProductsLD.value = data.homePageProductList
            }

            override fun onRequestFailed(errorMessage: String) {
                Log.e("", errorMessage)
            }

        })
    }


    fun getSimilarProducts(prodId: Long, thumbnailSizePx: Int, model: ProductDetailModel) {

        model.getAlsoPurchasedProducts(prodId, thumbnailSizePx, object: RequestCompleteListener<HomePageProductResponse> {

            override fun onRequestSuccess(data: HomePageProductResponse) {
                relatedProductsLD.value = data.homePageProductList
            }

            override fun onRequestFailed(errorMessage: String) {
                Log.e("", errorMessage)
            }

        })
    }


    private fun prepareBodyForAttributes(
                                productId : Long,
                                quantity: String,
                                selectedAttributeMap: MutableMap<Long, MutableList<AttributeControlValue>>): AddToCartPostData
    {
        val productAttributePrefix = "product_attribute"

        val allKeyValueList = ArrayList<KeyValuePair>()

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
                    allKeyValueList.add(keyValuePair)

                    "key_value".showLog(" Key : $key    values: ${selectedIdList[i]}")
                }
            }
        }
        val keyValuePair = KeyValuePair()

        keyValuePair.key = "addtocart_$productId.EnteredQuantity"
        keyValuePair.value = quantity
        allKeyValueList.add(keyValuePair)

        val body = AddToCartPostData(Any(), allKeyValueList, Any())
        body.formValues = allKeyValueList

        "key_value".showLog(body.toString())

        return body
    }


    fun addProductToCartModel(productId : Long,
                              quantity: String,
                              model: ProductDetailModel)
    {

        isLoadingLD.value = true

        model.addProductToCartModel(productId,
                                    1,
                                    prepareBodyForAttributes(productId, quantity, selectedAttrLD.value!!),

            object : RequestCompleteListener<AddToCartResponse>
        {
            override fun onRequestSuccess(data: AddToCartResponse)
            {
                isLoadingLD.value = false

                addToCartResponseLD.value = data
                cartProductsCountLD.value = data.cartRootData.cart.items.size
            }

            override fun onRequestFailed(errorMessage: String)
            {
                isLoadingLD.value = false
            }
        })
    }

    fun incrementQuantity() {
        quantityLiveData.value = quantityLiveData.value?.plus(1)
    }

    fun decrementQuantity() {
        if(quantityLiveData.value!! > 1) {
            quantityLiveData.setValue(quantityLiveData.value?.minus(1))
        } else {
            // FIXME how to show toast?
            // toastMessageLD.setValue("Invalid quantity")
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

        productPriceLD.value = price
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

        selectedAttrLD.value = attrMap
    }

}