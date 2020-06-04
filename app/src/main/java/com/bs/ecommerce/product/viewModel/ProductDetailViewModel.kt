package com.bs.ecommerce.product.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.bs.ecommerce.auth.register.data.KeyValuePair
import com.bs.ecommerce.base.BaseViewModel
import com.bs.ecommerce.common.RequestCompleteListener
import com.bs.ecommerce.db.DbHelper
import com.bs.ecommerce.home.homepage.model.data.HomePageProductResponse
import com.bs.ecommerce.networking.Api
import com.bs.ecommerce.networking.common.KeyValueFormData
import com.bs.ecommerce.product.model.ProductDetailModel
import com.bs.ecommerce.product.model.data.AddToCartResponse
import com.bs.ecommerce.product.model.data.ProductDetail
import com.bs.ecommerce.product.model.data.ProductDetailResponse
import com.bs.ecommerce.product.model.data.ProductSummary
import com.bs.ecommerce.utils.TextUtils
import com.bs.ecommerce.utils.showLog
import java.util.*


class ProductDetailViewModel : BaseViewModel() {

    var productLiveData = MutableLiveData<ProductDetail>()
    var relatedProductsLD = MutableLiveData<List<ProductSummary>>()
    var similarProductsLD = MutableLiveData<List<ProductSummary>>()

    var quantityLiveData = MutableLiveData<Int>()

    var isInvalidProductLD = MutableLiveData<Boolean>()

    var addToCartResponseLD = MutableLiveData<AddToCartResponse>()

    var gotoCartPage = false
    var rentDateFrom: Long? = null
    var rentDateTo: Long? = null

    fun getProductDetail(prodId: Long, model: ProductDetailModel) {
        isLoadingLD.value = true

        model.getProductDetailModel(prodId, object : RequestCompleteListener<ProductDetailResponse>
        {
            override fun onRequestSuccess(data: ProductDetailResponse)
            {
                isLoadingLD.value = false

                productLiveData.value = data.data
                quantityLiveData.value = data.data?.addToCart?.enteredQuantity ?: 1
            }

            override fun onRequestFailed(errorMessage: String) {
                isLoadingLD.value = false
                isInvalidProductLD.value = true
            }

        })
    }

    fun setAssociatedProduct(product: ProductDetail?) {
        productLiveData.value = product
        quantityLiveData.value = product?.addToCart?.enteredQuantity ?: 1
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
     * Custom Attributes are already added from {@link CustomAttributeManager#getFormData}
     * Add product quantity, Price (donation), Rental date here
     */
    private fun prepareBodyForAttributes(
                                productId : Long,
                                quantity: String,
                                keyValueFormData: KeyValueFormData): KeyValueFormData {


        val formValues = keyValueFormData.formValues as MutableList

        val keyPrefix = "addtocart_$productId."

        formValues.add(KeyValuePair().apply {
            key = keyPrefix.plus("EnteredQuantity")
            value = quantity
        })

        if(productLiveData.value?.addToCart?.customerEntersPrice == true) {

            formValues.add(KeyValuePair().apply {
                key = keyPrefix.plus("CustomerEnteredPrice")
                value = productLiveData.value?.addToCart?.customerEnteredPrice?.toString() ?: ""
            })
        }

        // Add rental start & end date for Rental Product
        if (productLiveData.value?.isRental == true) {

            formValues.add(KeyValuePair().apply {
                key = Api.rentalStart.plus(productId)
                value = TextUtils().epoch2DateString(rentDateFrom ?: 0L, "MM/dd/yyyy")
            })

            formValues.add(KeyValuePair().apply {
                key = Api.rentalEnd.plus(productId)
                value = TextUtils().epoch2DateString(rentDateTo ?: 0L, "MM/dd/yyyy")
            })
        }

        // Add gift card info
        productLiveData.value?.giftCard?.let {

            if (it.isGiftCard == true) {
                formValues.add(KeyValuePair().apply {
                    key = Api.getKeyForGiftCardMessage(productId)
                    value = it.message ?: ""
                })

                formValues.add(KeyValuePair().apply {
                    key = Api.getKeyForGiftCardSender(productId)
                    value = it.senderName ?: ""
                })

                formValues.add(KeyValuePair().apply {
                    key = Api.getKeyForGiftCardRecipient(productId)
                    value = it.recipientName ?: ""
                })
            }
        }

        keyValueFormData.formValues = formValues

        "key_value".showLog(keyValueFormData.toString())

        return keyValueFormData
    }


    fun addProductToCartModel(productId : Long,
                              quantity: String,
                              keyValueFormData: KeyValueFormData,
                              model: ProductDetailModel,
                              cartType: Long = Api.typeShoppingCart)
    {

        isLoadingLD.value = true

        model.addProductToCartModel(productId, cartType, prepareBodyForAttributes(productId, quantity, keyValueFormData),

            object : RequestCompleteListener<AddToCartResponse>
        {
            override fun onRequestSuccess(data: AddToCartResponse)
            {
                isLoadingLD.value = false

                addToCartResponseLD.value = data
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
            toast(DbHelper.getString("shoppingcart.quantityshouldpositive"))
        }
    }

    fun setRentDate(d: Int, m: Int, y: Int, isStartTime: Boolean) {

        val cal = Calendar.getInstance()
        cal[Calendar.DAY_OF_MONTH] = d
        cal[Calendar.MONTH] = m
        cal[Calendar.YEAR] = y

        if (isStartTime) {
            rentDateFrom = cal.timeInMillis
            rentDateTo = cal.timeInMillis
        }
        else {
            rentDateTo = cal.timeInMillis
        }
    }

    fun getRentDate(isStartTime: Boolean): Long {

        return if(isStartTime) {
            rentDateFrom ?: Calendar.getInstance().timeInMillis.also { rentDateFrom = it }
        } else {

            if(rentDateTo == null) {
                rentDateTo = getRentDate(true)
                rentDateTo!!
            } else {
                rentDateTo!!
            }
        }
    }

}