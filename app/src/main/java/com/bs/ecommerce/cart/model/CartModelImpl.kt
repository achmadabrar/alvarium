package com.bs.ecommerce.cart.model

import com.bs.ecommerce.account.auth.register.data.KeyValuePair
import com.bs.ecommerce.account.orders.model.data.UploadFileData
import com.bs.ecommerce.account.orders.model.data.UploadFileResponse
import com.bs.ecommerce.base.BaseFragment
import com.bs.ecommerce.cart.model.data.CartResponse
import com.bs.ecommerce.cart.model.data.CartRootData
import com.bs.ecommerce.db.DbHelper
import com.bs.ecommerce.networking.RetroClient
import com.bs.ecommerce.networking.common.KeyValueFormData
import com.bs.ecommerce.networking.common.RequestCompleteListener
import com.bs.ecommerce.utils.Const
import com.bs.ecommerce.utils.ProgressRequestBody
import com.bs.ecommerce.utils.TextUtils
import com.bs.ecommerce.utils.showLog
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class CartModelImpl: CartModel
{

    override fun getCartData(callback: RequestCompleteListener<CartResponse>)
    {
        RetroClient.api.getCartData().enqueue(object : Callback<CartResponse>
        {
            override fun onResponse(call: Call<CartResponse>, response: Response<CartResponse>)
            {
                if (response.body() != null)
                    callback.onRequestSuccess(response.body() as CartResponse)
                else
                    callback.onRequestFailed(TextUtils.getErrorMessage(response))
            }


            override fun onFailure(call: Call<CartResponse>, t: Throwable) {
                callback.onRequestFailed(t.localizedMessage ?: DbHelper.getString(Const.COMMON_SOMETHING_WENT_WRONG))
            }
        })
    }
    override fun updateCartData(keyValueFormData: KeyValueFormData, callback: RequestCompleteListener<CartResponse>)
    {

        RetroClient.api.updateCartData(keyValueFormData).enqueue(object : Callback<CartResponse>
        {
            override fun onResponse(call: Call<CartResponse>, response: Response<CartResponse>)
            {
                if (response.body() != null)
                    callback.onRequestSuccess(response.body() as CartResponse)
                else
                    callback.onRequestFailed(TextUtils.getErrorMessage(response))
            }


            override fun onFailure(call: Call<CartResponse>, t: Throwable) {
                callback.onRequestFailed(t.localizedMessage ?: DbHelper.getString(Const.COMMON_SOMETHING_WENT_WRONG))
            }
        })
    }


    override fun applyCouponModel(keyValueFormData: KeyValueFormData, callback: RequestCompleteListener<CartResponse>)
    {

        RetroClient.api.applyDiscountCoupon(keyValueFormData).enqueue(object : Callback<CartResponse>
        {
            override fun onResponse(call: Call<CartResponse>, response: Response<CartResponse>)
            {
                if (response.body() != null)
                    callback.onRequestSuccess(response.body() as CartResponse)
                else
                    callback.onRequestFailed(TextUtils.getErrorMessage(response))
            }


            override fun onFailure(call: Call<CartResponse>, t: Throwable) {
                callback.onRequestFailed(t.localizedMessage ?: DbHelper.getString(Const.COMMON_SOMETHING_WENT_WRONG))
            }
        })
    }

    override fun removeCouponModel(keyValueFormData: KeyValueFormData, callback: RequestCompleteListener<CartResponse>)
    {
        RetroClient.api.removeDiscountCoupon(keyValueFormData).enqueue(object : Callback<CartResponse>
        {
            override fun onResponse(call: Call<CartResponse>, response: Response<CartResponse>)
            {
                if (response.body() != null)
                    callback.onRequestSuccess(response.body() as CartResponse)
                else
                    callback.onRequestFailed(TextUtils.getErrorMessage(response))
            }


            override fun onFailure(call: Call<CartResponse>, t: Throwable) {
                callback.onRequestFailed(t.localizedMessage ?: DbHelper.getString(Const.COMMON_SOMETHING_WENT_WRONG))
            }
        })
    }

    override fun applyGiftCardModel(keyValueFormData: KeyValueFormData, callback: RequestCompleteListener<CartResponse>)
    {

        RetroClient.api.applyGiftCardCoupon(keyValueFormData).enqueue(object : Callback<CartResponse>
        {
            override fun onResponse(call: Call<CartResponse>, response: Response<CartResponse>)
            {
                if (response.body() != null)
                    callback.onRequestSuccess(response.body() as CartResponse)
                else
                    callback.onRequestFailed(TextUtils.getErrorMessage(response))
            }


            override fun onFailure(call: Call<CartResponse>, t: Throwable) {
                callback.onRequestFailed(t.localizedMessage ?: DbHelper.getString(Const.COMMON_SOMETHING_WENT_WRONG))
            }
        })
    }
    override fun removeGiftCardModel(keyValueFormData: KeyValueFormData, callback: RequestCompleteListener<CartResponse>)
    {
        RetroClient.api.removeGiftCardCoupon(keyValueFormData).enqueue(object : Callback<CartResponse>
        {
            override fun onResponse(call: Call<CartResponse>, response: Response<CartResponse>)
            {
                if (response.body() != null)
                    callback.onRequestSuccess(response.body() as CartResponse)
                else
                    callback.onRequestFailed(TextUtils.getErrorMessage(response))
            }


            override fun onFailure(call: Call<CartResponse>, t: Throwable) {
                callback.onRequestFailed(t.localizedMessage ?: DbHelper.getString(Const.COMMON_SOMETHING_WENT_WRONG))
            }
        })
    }

    override fun applyCheckoutAttributes(
        list: List<KeyValuePair>,
        callback: RequestCompleteListener<CartRootData>
    ) {
        RetroClient.api.updateCheckOutAttribute(KeyValueFormData(list)).enqueue(object : Callback<CartRootData> {
            override fun onResponse(call: Call<CartRootData>, response: Response<CartRootData>) {
                if (response.body() != null)
                    callback.onRequestSuccess(response.body() as CartRootData)
                else
                    callback.onRequestFailed(TextUtils.getErrorMessage(response))
            }


            override fun onFailure(call: Call<CartRootData>, t: Throwable) {
                callback.onRequestFailed(t.localizedMessage ?: DbHelper.getString(Const.COMMON_SOMETHING_WENT_WRONG))
            }
        })
    }


    override fun uploadFile(file: File, mimeType: String?, callback: RequestCompleteListener<UploadFileData>) {

        val requestBody = ProgressRequestBody(
            file, mimeType, object: ProgressRequestBody.ProgressCallback {
                override fun onProgress(progress: Long, total: Long) {
                    "upload_percent".showLog("$progress - $total")
                }
            })

        val body: MultipartBody.Part =
            MultipartBody.Part.createFormData("file", file.name, requestBody)


        RetroClient.api.uploadFileCheckoutAttribute(body, BaseFragment.fileUploadAttributeId).enqueue(object :
            Callback<UploadFileResponse> {

            override fun onFailure(call: Call<UploadFileResponse>, t: Throwable) {
                callback.onRequestFailed(t.localizedMessage ?: "Unknown")
            }

            override fun onResponse(
                call: Call<UploadFileResponse>,
                response: Response<UploadFileResponse>
            ) {
                if (response.body()?.data != null && response.code() == 200)
                    callback.onRequestSuccess(response.body()?.data as UploadFileData)
                else
                    callback.onRequestFailed(TextUtils.getErrorMessage(response))
            }

        })
    }
}