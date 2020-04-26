package com.bs.ecommerce.product.model.data

import com.google.gson.annotations.SerializedName

class ProductDetail(
    @SerializedName("PictureModels") var pictureModels: List<PictureModel>,
    @SerializedName("FullDescription") var fullDescription: String?,
    @SerializedName("AssociatedProducts") var associatedProducts: List<ProductDetail>,

    @SerializedName("ProductSpecifications") var productSpecifications: List<ProductSpecification>,

    @SerializedName("Quantity") var quantity: Quantity,

    @SerializedName("ProductAttributes") var productAttributes: List<ProductAttribute>,

    @SerializedName("AddToCart") var addToCart: AddToCart,

    @SerializedName("ShortDescription") var shortDescription: String? = null,
    @SerializedName("ProductPrice") var productPrice: ProductPrice? = null,

    @SerializedName("ReviewOverviewModel") var reviewOverviewModel: ReviewModel? = null,

    @SerializedName("StockAvailability") var stockAvailability: String? = null,

    @SerializedName("DefaultPictureModel") var defaultPictureModel: DefaultPictureModel? = null,
    @SerializedName("Id") var id: Long = 0,

    @SerializedName("Name") var name: String? = null

)