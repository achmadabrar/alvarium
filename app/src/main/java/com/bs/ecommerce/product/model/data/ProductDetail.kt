package com.bs.ecommerce.product.model.data

import com.google.gson.annotations.SerializedName

data class ProductDetail(
    @SerializedName("AddToCart")
    val addToCart: AddToCart?,
    @SerializedName("AssociatedProducts")
    val associatedProducts: List<ProductDetail>?,
    @SerializedName("Breadcrumb")
    val breadcrumb: ProductBreadcrumb?,
    @SerializedName("CompareProductsEnabled")
    val compareProductsEnabled: Boolean?,
    @SerializedName("CurrentStoreName")
    val currentStoreName: String?,
    @SerializedName("CustomProperties")
    val customProperties: CustomProperties?,
    @SerializedName("DefaultPictureModel")
    val defaultPictureModel: DefaultPictureModel?,
    @SerializedName("DefaultPictureZoomEnabled")
    val defaultPictureZoomEnabled: Boolean?,
    @SerializedName("DeliveryDate")
    val deliveryDate: Any?,
    @SerializedName("DisplayBackInStockSubscription")
    val displayBackInStockSubscription: Boolean?,
    @SerializedName("DisplayDiscontinuedMessage")
    val displayDiscontinuedMessage: Boolean?,
    @SerializedName("EmailAFriendEnabled")
    val emailAFriendEnabled: Boolean?,
    @SerializedName("FreeShippingNotificationEnabled")
    val freeShippingNotificationEnabled: Boolean?,
    @SerializedName("FullDescription")
    val fullDescription: String?,
    @SerializedName("GiftCard")
    val giftCard: GiftCard?,
    @SerializedName("Gtin")
    val gtin: Any?,
    @SerializedName("HasSampleDownload")
    val hasSampleDownload: Boolean?,
    @SerializedName("Id")
    val id: Long?,
    @SerializedName("IsFreeShipping")
    val isFreeShipping: Boolean?,
    @SerializedName("IsRental")
    val isRental: Boolean?,
    @SerializedName("IsShipEnabled")
    val isShipEnabled: Boolean?,
    @SerializedName("ManageInventoryMethod")
    val manageInventoryMethod: Int?,
    @SerializedName("ManufacturerPartNumber")
    val manufacturerPartNumber: Any?,
    @SerializedName("MetaDescription")
    val metaDescription: String?,
    @SerializedName("MetaKeywords")
    val metaKeywords: Any?,
    @SerializedName("MetaTitle")
    val metaTitle: Any?,
    @SerializedName("Name")
    val name: String?,
    @SerializedName("PageShareCode")
    val pageShareCode: String?,
    @SerializedName("PictureModels")
    val pictureModels: List<PictureModel>,
    @SerializedName("ProductAttributes")
    val productAttributes: List<ProductAttribute>,
    @SerializedName("ProductManufacturers")
    val productManufacturers: List<ProductManufacturers>?,
    @SerializedName("ProductPrice")
    val productPrice: ProductPrice?,
    @SerializedName("ProductReviewOverview")
    val productReviewOverview: ProductReviewOverview?,
    @SerializedName("ProductSpecifications")
    val productSpecifications: List<ProductSpecification>?,
    @SerializedName("ProductTags")
    val productTags: List<ProductTag>?,
    @SerializedName("ProductType")
    val productType: Int?,
    @SerializedName("RentalEndDate")
    var rentalEndDate: Any?,
    @SerializedName("RentalStartDate")
    var rentalStartDate: Any?,
    @SerializedName("SeName")
    val seName: String?,
    @SerializedName("ShortDescription")
    val shortDescription: String?,
    @SerializedName("ShowGtin")
    val showGtin: Boolean?,
    @SerializedName("ShowManufacturerPartNumber")
    val showManufacturerPartNumber: Boolean?,
    @SerializedName("ShowSku")
    val showSku: Boolean?,
    @SerializedName("ShowVendor")
    val showVendor: Boolean?,
    @SerializedName("Sku")
    val sku: String?,
    @SerializedName("StockAvailability")
    val stockAvailability: String?,
    @SerializedName("TierPrices")
    val tierPrices: List<Any>?,
    @SerializedName("VendorModel")
    val vendorModel: VendorModel?,
    @SerializedName("ReviewOverviewModel")
    val reviewOverviewModel: ReviewModel? = null
)