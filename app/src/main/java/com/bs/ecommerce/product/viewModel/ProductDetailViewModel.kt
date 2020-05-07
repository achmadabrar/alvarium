package com.bs.ecommerce.product.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.bs.ecommerce.auth.register.data.KeyValuePair
import com.bs.ecommerce.base.BaseViewModel
import com.bs.ecommerce.common.RequestCompleteListener
import com.bs.ecommerce.home.homepage.model.data.HomePageProductResponse
import com.bs.ecommerce.networking.Api
import com.bs.ecommerce.networking.common.KeyValueFormData
import com.bs.ecommerce.product.model.ProductDetailModel
import com.bs.ecommerce.product.model.data.AddToCartResponse
import com.bs.ecommerce.product.model.data.ProductDetail
import com.bs.ecommerce.product.model.data.ProductDetailResponse
import com.bs.ecommerce.product.model.data.ProductSummary
import com.bs.ecommerce.utils.showLog

class ProductDetailViewModel : BaseViewModel() {

    var productLiveData = MutableLiveData<ProductDetail>()
    var relatedProductsLD = MutableLiveData<List<ProductSummary>>()
    var similarProductsLD = MutableLiveData<List<ProductSummary>>()

    var quantityLiveData = MutableLiveData(1)

    var isInvalidProductLD = MutableLiveData<Boolean>()

    var cartProductsCountLD = MutableLiveData<Int>()

    var addToCartResponseLD = MutableLiveData<AddToCartResponse>()

    fun getProductDetail(prodId: Long, model: ProductDetailModel) {
        isLoadingLD.value = true

        model.getProductDetailModel(prodId, object : RequestCompleteListener<ProductDetailResponse>
        {
            override fun onRequestSuccess(data: ProductDetailResponse)
            {
                isLoadingLD.value = false

                productLiveData.value = data.data
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
                relatedProductsLD.value = data.homePageProductList ?: listOf()
            }

            override fun onRequestFailed(errorMessage: String) {
                Log.e("", errorMessage)
            }

        })
    }


    fun getSimilarProducts(prodId: Long, thumbnailSizePx: Int, model: ProductDetailModel) {

        model.getAlsoPurchasedProducts(prodId, thumbnailSizePx, object: RequestCompleteListener<HomePageProductResponse> {

            override fun onRequestSuccess(data: HomePageProductResponse) {
                similarProductsLD.value = data.homePageProductList ?: listOf()
            }

            override fun onRequestFailed(errorMessage: String) {
                Log.e("", errorMessage)
            }

        })
    }

    /**
     * Attributes are already added. Add product quantity attribute here
     */
    private fun prepareBodyForAttributes(
                                productId : Long,
                                quantity: String,
                                keyValueFormData: KeyValueFormData): KeyValueFormData {
        val keyValuePair = KeyValuePair()

        keyValuePair.key = "addtocart_$productId.EnteredQuantity"
        keyValuePair.value = quantity

        val v = keyValueFormData.formValues as MutableList
        v.add(keyValuePair)


        keyValueFormData.formValues = v

        "key_value".showLog(keyValueFormData.toString())

        return keyValueFormData
    }


    fun addProductToCartModel(productId : Long,
                              quantity: String,
                              keyValueFormData: KeyValueFormData,
                              model: ProductDetailModel)
    {

        isLoadingLD.value = true

        model.addProductToCartModel(productId,
                                    Api.typeShoppingCart,
                                    prepareBodyForAttributes(productId, quantity, keyValueFormData),

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
            toast("Invalid quantity")
        }
    }

}