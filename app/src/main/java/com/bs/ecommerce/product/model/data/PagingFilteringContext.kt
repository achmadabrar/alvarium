package com.bs.ecommerce.product.model.data


import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class PagingFilteringContext(
    @SerializedName("AllowCustomersToSelectPageSize")
    val allowCustomersToSelectPageSize: Boolean? = false,
    @SerializedName("AllowProductSorting")
    val allowProductSorting: Boolean? = false,
    @SerializedName("AllowProductViewModeChanging")
    val allowProductViewModeChanging: Boolean? = false,
    @SerializedName("AvailableSortOptions")
    val availableSortOptions: List<AvailableSortOption>? = listOf(),
    @SerializedName("AvailableViewModes")
    val availableViewModes: List<AvailableViewMode>? = listOf(),
    @SerializedName("CustomProperties")
    val customProperties: CustomProperties? = CustomProperties(),
    @SerializedName("FirstItem")
    val firstItem: Int? = 0,
    @SerializedName("HasNextPage")
    val hasNextPage: Boolean? = false,
    @SerializedName("HasPreviousPage")
    val hasPreviousPage: Boolean? = false,
    @SerializedName("LastItem")
    val lastItem: Int? = 0,
    @SerializedName("PageIndex")
    val pageIndex: Int? = 0,
    @SerializedName("PageNumber")
    val pageNumber: Int? = 0,
    @SerializedName("PageSize")
    val pageSize: Int? = 0,
    @SerializedName("PageSizeOptions")
    val pageSizeOptions: List<PageSizeOption>? = listOf(),
    @SerializedName("PriceRangeFilter")
    val priceRangeFilter: PriceRangeFilter? = PriceRangeFilter(),
    @SerializedName("SpecificationFilter")
    val specificationFilter: SpecificationFilter? = SpecificationFilter(),
    @SerializedName("TotalItems")
    val totalItems: Int? = 0,
    @SerializedName("TotalPages")
    val totalPages: Int? = 0,
    @SerializedName("ViewMode")
    val viewMode: String? = ""
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
        parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
        parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
        parcel.createTypedArrayList(AvailableSortOption),
        parcel.createTypedArrayList(AvailableViewMode),
        parcel.readParcelable(CustomProperties::class.java.classLoader),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
        parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.createTypedArrayList(PageSizeOption),
        parcel.readParcelable(PriceRangeFilter::class.java.classLoader),
        parcel.readParcelable(SpecificationFilter::class.java.classLoader),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(allowCustomersToSelectPageSize)
        parcel.writeValue(allowProductSorting)
        parcel.writeValue(allowProductViewModeChanging)
        parcel.writeTypedList(availableSortOptions)
        parcel.writeTypedList(availableViewModes)
        parcel.writeParcelable(customProperties, flags)
        parcel.writeValue(firstItem)
        parcel.writeValue(hasNextPage)
        parcel.writeValue(hasPreviousPage)
        parcel.writeValue(lastItem)
        parcel.writeValue(pageIndex)
        parcel.writeValue(pageNumber)
        parcel.writeValue(pageSize)
        parcel.writeTypedList(pageSizeOptions)
        parcel.writeParcelable(priceRangeFilter, flags)
        parcel.writeParcelable(specificationFilter, flags)
        parcel.writeValue(totalItems)
        parcel.writeValue(totalPages)
        parcel.writeString(viewMode)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PagingFilteringContext> {
        override fun createFromParcel(parcel: Parcel): PagingFilteringContext {
            return PagingFilteringContext(parcel)
        }

        override fun newArray(size: Int): Array<PagingFilteringContext?> {
            return arrayOfNulls(size)
        }
    }
}