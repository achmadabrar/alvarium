package com.bs.ecommerce.product.data

import com.google.gson.annotations.SerializedName


/**
 * Created by Ashraful on 11/20/2015.
 */
/*class ProductDetail : ProductModel()
{

    @SerializedName("PictureModels") var pictureModels: List<PictureModel>? = null
    @SerializedName("FullDescription")  var fullDescription: String? = null
    @SerializedName("AssociatedProducts")  var associatedProducts: List<ProductDetail>? = null

    @SerializedName("ProductSpecifications") var productSpecifications: List<ProductSpecification>? = null

    @SerializedName("Quantity") var quantity = Quantity()

    @SerializedName("ProductAttributes") lateinit var productAttributes: List<ProductAttribute>
}*/


class ProductDetail(    @SerializedName("PictureModels") var pictureModels: List<PictureModel>,
                        @SerializedName("FullDescription")  var fullDescription: String?,
                        @SerializedName("AssociatedProducts")  var associatedProducts: List<ProductDetail>,

                        @SerializedName("ProductSpecifications") var productSpecifications: List<ProductSpecification>,

                        @SerializedName("Quantity") var quantity : Quantity,

                        @SerializedName("ProductAttributes") var productAttributes: List<ProductAttribute>,

                        @SerializedName("AddToCart") var addToCart: AddToCart


): ProductModel()