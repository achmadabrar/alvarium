package com.bs.ecommerce.home.homepage.model.data

import com.bs.ecommerce.product.data.ProductSummary
import com.google.gson.annotations.SerializedName


data class HomePageProductResponse(
    @SerializedName("Data") var homePageProductList: List<ProductSummary> = listOf(),
    @SerializedName("ErrorList") var errorList: List<Any> = listOf(),
    @SerializedName("Message") var message: Any = Any()
)

data class HomePageProduct(
    @SerializedName("DefaultPictureModel") var defaultPictureModel: DefaultPictureModel = DefaultPictureModel(),
    @SerializedName("FullDescription") var fullDescription: String = "",
    @SerializedName("Id") var id: Int = 0,
    @SerializedName("MarkAsNew") var markAsNew: Boolean = false,
    @SerializedName("Name") var name: String = "",
    @SerializedName("ProductPrice") var productPrice: ProductPrice = ProductPrice(),
    @SerializedName("ProductType") var productType: Int = 0,
    @SerializedName("ReviewOverviewModel") var reviewOverviewModel: ReviewOverviewModel = ReviewOverviewModel(),
    @SerializedName("SeName") var seName: String = "",
    @SerializedName("ShortDescription") var shortDescription: String = "",
    @SerializedName("Sku") var sku: String = "",
    @SerializedName("SpecificationAttributeModels") var specificationAttributeModels: List<Any> = listOf()
)


data class DefaultPictureModel(
    @SerializedName("AlternateText") var alternateText: String = "",
    @SerializedName("FullSizeImageUrl") var fullSizeImageUrl: String = "",
    @SerializedName("ImageUrl") var imageUrl: String = "",
    @SerializedName("ThumbImageUrl") var thumbImageUrl: Any = Any(),
    @SerializedName("Title") var title: String = ""
)

data class ProductPrice(
    @SerializedName("AvailableForPreOrder") var availableForPreOrder: Boolean = false,
    @SerializedName("BasePricePAngV") var basePricePAngV: Any = Any(),
    @SerializedName("DisableAddToCompareListButton") var disableAddToCompareListButton: Boolean = false,
    @SerializedName("DisableBuyButton") var disableBuyButton: Boolean = false,
    @SerializedName("DisableWishlistButton") var disableWishlistButton: Boolean = false,
    @SerializedName("DisplayTaxShippingInfo") var displayTaxShippingInfo: Boolean = false,
    @SerializedName("ForceRedirectionAfterAddingToCart") var forceRedirectionAfterAddingToCart: Boolean = false,
    @SerializedName("IsRental") var isRental: Boolean = false,
    @SerializedName("OldPrice") var oldPrice: Any = Any(),
    @SerializedName("PreOrderAvailabilityStartDateTimeUtc") var preOrderAvailabilityStartDateTimeUtc: Any = Any(),
    @SerializedName("Price") var price: String = "",
    @SerializedName("PriceValue") var priceValue: Double = 0.0
)

data class ReviewOverviewModel(
    @SerializedName("AllowCustomerReviews") var allowCustomerReviews: Boolean = false,
    @SerializedName("ProductId") var productId: Int = 0,
    @SerializedName("RatingSum") var ratingSum: Int = 0,
    @SerializedName("TotalReviews") var totalReviews: Int = 0
)
