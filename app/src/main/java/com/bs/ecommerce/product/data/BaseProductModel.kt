package com.bs.ecommerce.product.data


import com.google.gson.annotations.SerializedName

/**
 * Created by Ashraful on 11/10/2015.
 */
open class BaseProductModel
{
    @SerializedName("DefaultPictureModel") var defaultPictureModel: DefaultPictureModel? = null
    @SerializedName("Id") var id: Long = 0

    @SerializedName("Name") var name: String? = null
}


//open class BaseProductModel(var defaultPictureModel: DefaultPictureModel, var id : Long = 0, var name : String)
