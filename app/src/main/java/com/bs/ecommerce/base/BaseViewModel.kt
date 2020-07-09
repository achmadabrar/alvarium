package com.bs.ecommerce.base

import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bs.ecommerce.cart.model.CartModel
import com.bs.ecommerce.cart.model.data.CartResponse
import com.bs.ecommerce.cart.model.data.CartRootData
import com.bs.ecommerce.networking.common.RequestCompleteListener
import com.bs.ecommerce.db.DbHelper
import com.bs.ecommerce.networking.Api
import com.bs.ecommerce.catalog.product.model.ProductDetailModelImpl
import com.bs.ecommerce.catalog.common.AddToWishListResponse
import com.bs.ecommerce.catalog.common.ProductSummary
import com.bs.ecommerce.utils.Const
import com.bs.ecommerce.MyApplication
import com.bs.ecommerce.R
import com.bs.ecommerce.account.orders.model.data.UploadFileData
import com.bs.ecommerce.account.orders.model.data.UploadFileResponse
import com.bs.ecommerce.main.model.MainModelImpl
import com.bs.ecommerce.networking.NetworkConstants
import com.bs.ecommerce.utils.OneTimeEvent
import com.bs.ecommerce.utils.showLog
import com.bs.ecommerce.utils.toast
import com.downloader.Error
import com.downloader.OnDownloadListener
import com.downloader.PRDownloader


open class BaseViewModel : ViewModel() {
    var isLoadingLD = MutableLiveData<Boolean>()
    var addedToWishListLD = MutableLiveData<OneTimeEvent<ProductSummary>?>()

    var cartLD = MutableLiveData<CartRootData>()


    val uploadFileLD = MutableLiveData<UploadFileData?>()
    var uploadedFileGuid: String = ""
    var fileDownloadUrl: String = ""

    fun getCartVM(model: CartModel) {
        isLoadingLD.value = true

        model.getCartData(object :
            RequestCompleteListener<CartResponse> {
            override fun onRequestSuccess(data: CartResponse) {
                isLoadingLD.value = false

                cartLD.value = data.cartRootData
            }

            override fun onRequestFailed(errorMessage: String) {
                isLoadingLD.value = false
            }
        })
    }

    fun addToWishList(product: ProductSummary) {

        val model =
            ProductDetailModelImpl()

        model.addProductToWishList(product.id!!.toLong(), Api.typeWishList,
            object :
                RequestCompleteListener<AddToWishListResponse> {

                override fun onRequestSuccess(data: AddToWishListResponse) {

                    if (data.redirectionModel?.redirectToDetailsPage == true) {
                        addedToWishListLD.value = OneTimeEvent(product) // goto product details page
                    } else {
                        toast(DbHelper.getString(Const.PRODUCT_ADDED_TO_WISHLIST))
                        addedToWishListLD.value = null // success. do nothing
                    }
                }

                override fun onRequestFailed(errorMessage: String) {
                    toast(errorMessage)
                    addedToWishListLD.value = null // error. do nothing
                }

            })
    }

    fun uploadFileCheckoutAttribute(fileInfo: FileWithMimeType)
    {
        val model = MainModelImpl()
        val file  = fileInfo.file

        model.uploadFileCheckoutAttribute(file, fileInfo.mimeType, object : RequestCompleteListener<UploadFileResponse> {

            override fun onRequestSuccess(response: UploadFileResponse) {
                uploadFileLD.value = response.data
                uploadedFileGuid = response.data?.downloadGuid ?: ""
                fileDownloadUrl = response.data?.downloadUrl ?: ""

                toast(response.message)

                try { file.delete() } catch (e: Exception) { e.printStackTrace() }
            }
            override fun onRequestFailed(errorMessage: String) {
                uploadFileLD.value = null
                uploadedFileGuid = ""

                toast(errorMessage)

                try { file.delete() } catch (e: Exception) { e.printStackTrace() }
            }
        })
    }

    fun downloadFile(downloadUrl: String?)
    {
/*        val prefix = "${NetworkConstants.SITE_URL}${downloadUrl}"
        val thisFilePath = "$dirPath/${data?.fileName}.pdf"

        PRDownloader.download("$prefix${data?.downloadGuid}", dirPath, "${data?.fileName}.pdf" )
            .build()
            .setOnStartOrResumeListener {}
            .setOnPauseListener {}
            .setOnCancelListener { data?.downloadId = 0 }
            .setOnProgressListener { progress ->    holder.downloadButton.text = context.getString(
                R.string.file_downloading)    }
            .start(object : OnDownloadListener {
                override fun onDownloadComplete()
                {
                    holder.downloadButton.text = context.getString(R.string.file_open)

                    saveFilePathOnPrefs(data, thisFilePath)

                    "downloadFile".showLog("$prefix${data?.downloadGuid}   ${data?.fileName}.pdf")

                    holder.downloadButton.setOnClickListener {  openPDF(thisFilePath)  }
                }
                override fun onError(error: Error) {

                }
            })*/
    }



    protected fun toast(msg: String?) {
        if (MyApplication.mAppContext != null && msg != null)
            Toast.makeText(MyApplication.mAppContext, msg, Toast.LENGTH_SHORT).show()
    }


}