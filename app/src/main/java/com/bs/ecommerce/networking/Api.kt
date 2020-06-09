package com.bs.ecommerce.networking

import com.bs.ecommerce.auth.login.data.ChangePasswordModel
import com.bs.ecommerce.auth.login.data.ForgotPasswordResponse
import com.bs.ecommerce.auth.login.data.LoginPostData
import com.bs.ecommerce.auth.login.data.LoginResponse
import com.bs.ecommerce.auth.register.data.GetRegistrationResponse
import com.bs.ecommerce.cart.model.data.CartResponse
import com.bs.ecommerce.cart.model.data.CartRootData
import com.bs.ecommerce.checkout.model.data.*
import com.bs.ecommerce.home.homepage.model.data.HomePageProductResponse
import com.bs.ecommerce.home.homepage.model.data.SliderResponse
import com.bs.ecommerce.main.model.data.AppLandingSettingResponse
import com.bs.ecommerce.main.model.data.CategoryTreeResponse
import com.bs.ecommerce.networking.common.BaseResponse
import com.bs.ecommerce.networking.common.ExistingAddress
import com.bs.ecommerce.networking.common.KeyValueFormData
import com.bs.ecommerce.product.model.data.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*


/**
 * Created by bs156 on 09-Dec-16.
 */

interface Api {

    @GET("common/setCurrency/{id}")
    fun setCurrency(@Path("id") id: Long): Call<BaseResponse>

    @GET("common/SetLanguage/{id}")
    fun setLanguage(@Path("id") id: Long): Call<BaseResponse>


    @GET("home/categorytree")
    fun getHomeCategoryTree(): Call<CategoryTreeResponse>


    @GET("customer/info")
    fun getCustomerInfoAPI(): Call<GetRegistrationResponse>

    @POST("customer/info")
    fun saveCustomerInfo(@Body customerInfo: GetRegistrationResponse): Call<GetRegistrationResponse>

    @GET("customer/register")
    fun getRegisterAPI(): Call<GetRegistrationResponse>

    @POST("customer/register")
    fun postRegisterAPI(@Body registerPostData: GetRegistrationResponse): Call<GetRegistrationResponse>

    @GET("shoppingcart/cart")
    fun getCartData(): Call<CartResponse>

    @POST("shoppingcart/updatecart")
    fun updateCartData(@Body KeyValueFormData: KeyValueFormData): Call<CartResponse>


    @POST("shoppingcart/applydiscountcoupon")
    fun applyDiscountCoupon(@Body request: KeyValueFormData): Call<CartResponse>

    @POST("shoppingcart/removediscountcoupon")
    fun removeDiscountCoupon(@Body request: KeyValueFormData): Call<CartResponse>

    @POST("shoppingcart/applygiftcard")
    fun applyGiftCardCoupon(@Body request: KeyValueFormData): Call<CartResponse>

    @POST("shoppingcart/removegiftcardcode")
    fun removeGiftCardCoupon(@Body request: KeyValueFormData): Call<CartResponse>


    @POST("customer/login") fun postLoginAPI(@Body loginPostData: LoginPostData): Call<LoginResponse>


    @POST("customer/passwordrecovery")
    fun forgetPassword(@Body forgetData: ForgotPasswordResponse): Call<ForgotPasswordResponse>

    @GET("customer/changepassword")
    fun getChangePasswordModel(): Call<ChangePasswordModel>

    @POST("customer/changepassword")
    fun changePassword(@Body userData: ChangePasswordModel): Call<ChangePasswordModel>

    //checkout

    @GET("checkout/getbilling")
    fun getBillingFormAPI(): Call<BillingAddressResponse>

    @GET("country/getstatesbycountryid/{countryId}")
    fun getStatesAPI(@Path("countryId") id: String): Call<StateListResponse>

    @POST("checkout/savebilling")
    fun saveNewBillingAPI(@Body billingAddressPostData: SaveBillingPostData): Call<CheckoutSaveResponse>

    @POST("checkout/savebilling")
    fun saveExistingBillingAPI(@Body postModel: ExistingAddress): Call<CheckoutSaveResponse>

    @POST("checkout/saveshipping")
    fun saveNewShippingAPI(@Body shippingAddressPostData: SaveShippingPostData): Call<CheckoutSaveResponse>

    @POST("checkout/saveshipping")
    fun saveExistingShippingAPI(@Body postModel: ExistingAddress): Call<CheckoutSaveResponse>

    @POST("checkout/saveshippingmethod")
    fun saveShippingMethodAPI(@Body postModel: KeyValueFormData): Call<CheckoutSaveResponse>

    @POST("checkout/savepaymentmethod")
    fun savePaymentMethodAPI(@Body postModel: KeyValueFormData): Call<CheckoutSaveResponse>

    @GET("checkout/confirmorder")
    fun getCheckoutConfirmInformationAPI(): Call<ConfirmOrderResponse>

    @POST("checkout/confirmorder")
    fun submitConfirmOrderAPI(): Call<CheckoutSaveResponse>

    @GET("checkout/completed")
    fun completeOrderAPI(): Call<CheckoutSaveResponse>

    @POST("shoppingcart/checkoutattributechange")
    fun updateCheckOutAttribute(@Body kvFormData: KeyValueFormData): Call<CartRootData>


    //homepage

    @GET("home/applandingsetting")
    fun getAppLandingSettings(): Call<AppLandingSettingResponse>

    @GET("home/featureproducts")
    fun getHomeFeaturedProducts(): Call<HomePageProductResponse>

    @GET("home/homepagecategorieswithproducts")
    fun getHomePageCategoriesWithProducts(): Call<HomePageCategoryResponse>

    @GET("home/bestsellerproducts")
    fun getHomeBestSellerProducts(): Call<HomePageProductResponse>

    @GET("home/manufacturers")
    fun getHomeManufacturer(): Call<ManufacturerResponse>

    @GET("slider/homepageslider")
    fun getHomeSlider(): Call<SliderResponse>

    // Product Details
    @GET("product/productdetails/{id}/0")
    fun getProductDetails(@Path("id") id: Long): Call<ProductDetailResponse>

    @GET("product/relatedproducts/{id}/{thumbnailSize}")
    fun getRelatedProducts(
        @Path("id") productId: Long,
        @Path("thumbnailSize") thumbSize: Int
    ): Call<HomePageProductResponse>

    @GET("product/productsalsopurchased/{id}/{thumbnailSize}")
    fun getSimilarProducts(
        @Path("id") productId: Long,
        @Path("thumbnailSize") thumbSize: Int
    ): Call<HomePageProductResponse>

    @POST("shoppingCart/AddProductToCart/details/{productId}/{shoppingCartTypeId}")
    fun addProductIntoCartAPI(@Path("productId") id: Long,
                           @Path("shoppingCartTypeId") shoppingCartTypeId: Long,
                           @Body KeyValueFormData: KeyValueFormData
    ): Call<AddToCartResponse>

    @GET("catalog/category/{id}")
    fun getProductList(@Path("id") id: Long, @QueryMap options: Map<String, String>): Call<CategoryResponse>

    @GET("catalog/vendor/{id}")
    fun getProductByVendor(
        @Path("id") id: Long,
        @QueryMap options: Map<String, String>
    ): Call<ProductByVendorResponse>

    @GET("catalog/producttag/{id}")
    fun getProductByTag(
        @Path("id") id: Long,
        @QueryMap options: Map<String, String>
    ): Call<ProductByTagResponse>

    @GET("catalog/manufacturer/{manufacturerId}")
    fun getProductListByManufacturer(
        @Path("manufacturerId") id: Long,
        @QueryMap options: Map<String, String>
    ): Call<ProductByManufacturerResponse>

    @GET("catalog/manufacturer/all")
    fun getAllManufacturers(): Call<ManufacturerResponse>

    @GET
    fun applyFilter(@Url endpoint: String): Call<CategoryResponse>

    @GET("catalog/search")
    fun searchProduct(
        @Query("pageNumber") pageNumber: Int,
        @Query("pageSize") pageSize: Int,
        @QueryMap query: Map<String, String>
    ): Call<SearchResponse>

    // Order

    @GET("order/orderdetails/{id}")
    fun getOrderDetails(@Path("id") id: Int): Call<OrderDetailsResponse>

    @GET("order/history")
    fun getOrderHistory(): Call<OrderHistoryResponse>

    // WishList

    @GET("shoppingcart/wishlist")
    fun getWishList(): Call<WishListResponse>

    @POST("shoppingcart/updatewishlist")
    fun updateWishList(@Body kvFormData: KeyValueFormData): Call<WishListResponse>

    @POST("shoppingcart/additemstocartfromwishlist")
    fun moveWishListItemsToCart(@Body body: BaseResponse): Call<WishListResponse>

    @POST("shoppingCart/addproducttocart/catalog/{productId}/{cartType}")
    fun addToCartFromList(
        @Path("productId") productId: Long,
        @Path("cartType") type: Long,
        @Body kvFormData: KeyValueFormData
    ): Call<AddToWishListResponse>

    // Customer Address

    @GET("customer/addresses")
    fun getCustomerAddresses(): Call<CustomerAddressResponse>

    @GET("customer/addressadd")
    fun getAddAddressData(): Call<EditCustomerAddressResponse>

    @POST("customer/addressadd")
    fun saveCustomerAddress(@Body body: EditCustomerAddressResponse): Call<ResponseBody>

    @POST("customer/addressdelete/{addressId}")
    fun deleteCustomerAddress(@Path("addressId") id: Int): Call<ResponseBody>

    @GET("customer/addressedit/{addressId}")
    fun getEditAddressData(@Path("addressId") id: Int): Call<EditCustomerAddressResponse>

    @POST("customer/addressedit/{addressId}")
    fun editAddress(
        @Path("addressId") id: Int,
        @Body body: EditCustomerAddressResponse
    ): Call<ResponseBody>

    // TOPIC

    @GET("topic/details/{systemName}")
    fun getTopic(@Path("systemName") systemName: String) : Call<TopicResponse>

    @GET("topic/detailsbyid/{id}")
    fun getTopicById(@Path("id") topicId: Int) : Call<TopicResponse>

    @POST("common/contactus")
    fun contactUs(@Body userData: ContactUsResponse) : Call<ContactUsResponse>

    // Review

    @GET("product/productreviews")
    fun getMyReviews(@Query("pageNumber") page: Int) : Call<MyReviewsResponse>

    @GET("product/productreviews/{productId}")
    fun getProductReview(@Path("productId") id: Long) : Call<ProductReviewResponse>

    @POST("product/productreviewsadd/{productId}")
    fun postProductReview(
        @Path("productId") id: Long,
        @Body userData: ProductReviewResponse
    ) : Call<ProductReviewResponse>

    @POST("product/setproductreviewhelpfulness/{reviewId}")
    fun postReviewHelpfulness(
        @Path("reviewId") id: Long,
        @Body kvFormData: KeyValueFormData
    ) : Call<HelpfulnessResponse>

    // Reward Point

    @GET("order/customerrewardpoints")
    fun getRewardPoints(@Query("pageNumber") page: Int) : Call<RewardPointResponse>

    // Common

    @GET("common/getstringresources/{languageId}")
    fun getStringResource(@Path("languageId") language: Int) : Call<StringResourceResponse>

    @GET("catalog/vendor/all")
    fun getAllVendors(): Call<GetAllVendorsResponse>

    @GET("common/contactvendor/{vendorId}")
    fun getContactVendorModel(@Path("vendorId") vendorId: Int) : Call<GetContactVendorResponse>

    @POST("api/??")
    fun postContactVendorModel(@Path("vendorId") vendorId: Int) : Call<GetContactVendorResponse>

/*  @get:GET("ShoppingCart")
    val shoppingCart: Call<CartProductListResponse>

    @get:GET("ShoppingCart/OrderTotal")
    val orderTotal: Call<OrderTotalResponse>

    @get:GET("checkout/billingform")
    val billingAddress: Call<BillingAddressResponse>

    @get:GET("checkout/getcheckoutpickuppoints")
    val storeAddress: Call<StoreAddressResponse>

    @get:GET("checkout/checkoutgetshippingmethods")
    val shippingMethod: Call<ShippingMethodRetrievalResponse>

    @get:GET("checkout/checkoutgetpaymentmethod")
    val paymentMethod: Call<PaymentMethodRetrievalResponse>

    // Customer

    @get:GET("shoppingcart/checkoutorderinformation")
    val checkoutOrderSummary: Call<CheckoutOrderSummaryResponse>

    @get:GET("order/customerorders")
    val customerOrders: Call<CustomerOrdersResponse>

    @get:GET("checkout/opccheckoutforguest")
    val isGuestCheckout: Call<IsGuestCheckoutResponse>


    @get:GET("categoriesNmanufactures/search")
    val advanceSearchOptions: Call<AdvanceSearchSpinnerOptionResponse>

    @POST("AppStart")
    fun initApp(@Body appStartRequest: AppStartRequest): Call<AppThemeResponse>


    @GET("categoryfeaturedproductandsubcategory/{id}")
    fun getCategoryFeaturedProductAndSubcategory(@Path("id") id: Long): Call<CategoryFeaturedProductAndSubcategoryResponse>


    @POST("ShoppingCart/ApplyDiscountCoupon")
    fun applyDiscountCoupon(@Body request: DiscountCouponRequest): Call<DiscountCouponApplyResponse>

    @POST("ShoppingCart/ApplyGiftCard")
    fun applyDiscount(@Body request: AddGiftRequest): Call<AddGiftVoucherResponse>

    @GET("ShoppingCart/RemoveDiscountCoupon")
    fun removeDiscountCoupon(): Call<BaseResponse>

    @POST("ShoppingCart/applycheckoutattribute")
    fun applyCheckoutAttribute(@Body list: List<KeyValuePair>): Call<OrderTotalResponse>

    @POST("checkout/checkoutsaveadress/1") fun saveBillingAddress(@Body list: List<KeyValuePair>): Call<BillingAddressSaveResponse>

    @POST("checkout/checkoutsaveadressid/1")
    fun saveBillingAddressFromAddress(@Body valuePost: ValuePost): Call<BillingAddressSaveResponse>

    @GET("checkout/savecheckoutpickuppoint")
    fun saveStoreAddress(@Query("pickupPointId") pickupPointId: String): Call<StoreSaveResponse>

    @POST("checkout/checkoutsaveadressid/2")
    fun saveShippingAddressFromAddress(@Body valuePost: ValuePost): Call<ShippingAddressSaveResponse>

    @POST("checkout/checkoutsaveadress/2")
    fun saveShippingAddressByForm(@Body list: List<KeyValuePair>): Call<ShippingAddressSaveResponse>

    @POST("checkout/checkoutsetshippingmethod")
    fun setShippingMethod(@Body valuePost: ValuePost): Call<ShippingMethodSelttingResponse>

    @POST("checkout/checkoutsavepaymentmethod")
    fun saveCheckoutPaymentMethod(@Body valuePost: ValuePost): Call<PaymentMethodSaveResponse>

    @GET("checkout/checkoutcomplete")
    fun confirmCheckout(): Call<CheckoutConfirmResponse>

    @POST("checkout/checkpaypalaccount")
    fun confirmPayPalCheckout(@Body request: PaypalCheckoutRequest): Call<ConfirmPayPalCheckoutResponse>

    @GET("order/reorder/{id}")
    fun getReOrder(@Path("id") id: Int): Call<ReOrderResponse>

    @POST("ShoppingCart/UpdateWishlist")
    fun updateWishList(@Body keyValuePairs: List<KeyValuePair>): Call<WishistUpdateResponse>

    @POST("ShoppingCart/AddItemsToCartFromWishlist")
    fun addItemsToCartFromWishList(@Body keyValuePairs: List<KeyValuePair>): Call<WishListProductResponse>

    @POST("ShoppingCart/AddItemsToCartFromWishlist")
    fun addAllItemsToCartFromWishList(@Body keyValuePairs: List<KeyValuePair>): Call<WishListProductResponse>

    @POST("checkout/checkauthorizepayment")
    fun checkAuthorizePayment(@Body authorizePayment: AuthorizePayment): Call<ConfirmAutorizeDotNetCheckoutResponse>*/


    companion object
    {
        val thumbnailImageSize = 320
        val manufactureImageSize = "800"
        const val queryString = "thumbPictureSize"
        val qs_price = "price"
        val qs_page_number = "PageNumber"
        val qs_page_size = "PageSize"
        val qs_order_by = "orderby"
        val qs_spec = "specs"

        const val removeFromCartOrWishList: String = "removefromcart"
        const val addToCart: String = "addtocart"
        const val typeShoppingCart: Long = 1
        const val typeWishList: Long = 2

        const val rentalStart: String = "rental_start_date_"
        const val rentalEnd: String = "rental_end_date_"

        const val productReviewId: String = "productReviewId"
        const val wasReviewHelpful: String = "washelpful"

        const val productAttributePrefix = "product_attribute"
        const val checkOutAttributePrefix = "checkout_attribute"
        const val addressAttributePrefix = "address_attribute"
        const val customerAttributePrefix = "customer_attribute"

        fun getKeyForGiftCardMessage(productId: Long): String = "giftcard_$productId.Message"
        fun getKeyForGiftCardSender(productId: Long): String = "giftcard_$productId.SenderName"
        fun getKeyForGiftCardRecipient(productId: Long): String = "giftcard_$productId.RecipientName"

        const val topicAboutUs = "AboutUs"
        const val topicPrivacyPolicy = "PrivacyInfo"

        @JvmStatic
        val DEFAULT_PAGE_SIZE = 9
    }
}
