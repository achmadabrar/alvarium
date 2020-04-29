package com.bs.ecommerce.networking

import com.bs.ecommerce.auth.login.data.LoginPostData
import com.bs.ecommerce.auth.login.data.LoginResponse
import com.bs.ecommerce.auth.register.data.GetRegistrationResponse
import com.bs.ecommerce.auth.register.data.KeyValuePair
import com.bs.ecommerce.cart.model.data.AddDiscountPostData
import com.bs.ecommerce.cart.model.data.CartResponse
import com.bs.ecommerce.cart.model.data.CartRootData
import com.bs.ecommerce.checkout.model.data.BillingAddressResponse
import com.bs.ecommerce.checkout.model.data.CheckoutPostData
import com.bs.ecommerce.checkout.model.data.StateListResponse
import com.bs.ecommerce.home.homepage.model.data.HomePageProductResponse
import com.bs.ecommerce.home.homepage.model.data.SliderResponse
import com.bs.ecommerce.main.model.data.AppLandingSettingResponse
import com.bs.ecommerce.main.model.data.CategoryTreeResponse
import com.bs.ecommerce.product.model.data.*
import retrofit2.Call
import retrofit2.http.*


/**
 * Created by bs156 on 09-Dec-16.
 */

interface Api {

    @GET("home/categorytree")
    fun getHomeCategoryTree(): Call<CategoryTreeResponse>


    @GET("customer/info")
    fun getCustomerInfoAPI(): Call<GetRegistrationResponse>

    @GET("customer/register")
    fun getRegisterAPI(): Call<GetRegistrationResponse>

    @POST("customer/register")
    fun postRegisterAPI(@Body registerPostData: GetRegistrationResponse): Call<GetRegistrationResponse>

    @GET("shoppingcart/cart")
    fun getCartData(): Call<CartResponse>

    @POST("shoppingcart/updatecart")
    fun updateCartData(@Body list: List<KeyValuePair>): Call<CartResponse>


    @POST("shoppingcart/applydiscountcoupon")
    fun applyDiscountCoupon(@Body request: AddDiscountPostData): Call<CartResponse>

    @POST("shoppingcart/applygiftcard")
    fun applyGiftCardCoupon(@Body request: AddDiscountPostData): Call<CartResponse>


    @POST("customer/login") fun postLoginAPI(@Body loginPostData: LoginPostData): Call<LoginResponse>




    //checkout

    @GET("checkout/getbilling")
    fun getBillingFormAPI(): Call<BillingAddressResponse>

    @GET("country/getstatesbycountryid/{countryId}")
    fun getStatesAPI(@Path("countryId") id: String): Call<StateListResponse>

    @GET("checkout/checkoutgetshippingmethods")
    fun getShippingMethodAPI(): Call<CartResponse>

    @POST("checkout/checkoutsetshippingmethod")
    fun saveShippingMethodAPI(@Body checkoutPostData: CheckoutPostData): Call<CartResponse>

    @GET("checkout/checkoutgetpaymentmethod")
    fun getPaymentMethodAPI(): Call<CartResponse>

    @POST("checkout/checkoutsavepaymentmethod")
    fun savePaymentMethodAPI(@Body checkoutPostData: CheckoutPostData): Call<CartResponse>

    @GET("shoppingcart/checkoutorderinformation ")
    fun getCheckoutConfirmInformationAPI(): Call<CartResponse>

    @POST("shoppingcart/checkoutattributechange")
    fun updateCheckOutAttribute(@Body list: List<KeyValuePair>): Call<CartRootData>


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
                           @Body addToCartPostData: AddToCartPostData): Call<AddToCartResponse>

    @GET("catalog/category/{id}")
    fun getProductList(@Path("id") id: Long, @QueryMap options: Map<String, String>): Call<CategoryResponse>

    @GET("catalog/manufacturer/{manufacturerId}")
    fun getProductListByManufacturer(@Path("manufacturerId") id: Long, @QueryMap options: Map<String, String>): Call<ProductByManufacturerResponse>

    @GET("catalog/manufacturer/all")
    fun getAllManufacturers(): Call<ManufacturerResponse>

    @GET
    fun applyFilter(@Url endpoint: String): Call<CategoryResponse>

    @GET("catalog/search/{pageNumber}/{pageSize}/{orderBy}/{viewMode}")
    fun searchProduct(
        @Path("pageNumber") pageNumber: Int,
        @Path("pageSize") pageSize: Int,
        @Path("orderBy") orderBy: String,
        @Path("viewMode") viewMode: String,
        @QueryMap query: Map<String, String>
        ): Call<SearchResponse>

    // Order

    @GET("order/orderdetails/{id}")
    fun getOrderDetails(@Path("id") id: Int): Call<OrderDetailsResponse>

    @GET("order/history")
    fun getOrderHistory(): Call<OrderHistoryResponse>

/*    @POST("setcurrency/{id}")
    fun setCurrency(@Path("id") id: Long): Call<LanguageResponse>

    @get:GET("getcurrency")
    val getCurrency : Call<GetCurrencyResponse>


    @POST("SetLanguage/{id}")
    fun setLanguage(@Path("id") id: Long): Call<LanguageResponse>

    @get:GET("GetLanguage")
    val getLanguage : Call<GetLanguageResponse>


    @get:GET("v1/categories")
    val newCategory: Call<CategoryNewResponse>

    @get:GET("categories")
    val category: Call<CategoryResponse>

    @get:GET("catalog/homepagecategorieswithproduct")
    val homePageCategoriesWithProduct: Call<FeaturedCategoryResponse>

    @get:GET("ShoppingCart")
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
    @get:GET("customer/info")
    val customerInfo: Call<CustomerInfo>

    @get:GET("shoppingcart/checkoutorderinformation")
    val checkoutOrderSummary: Call<CheckoutOrderSummaryResponse>

    @get:GET("customer/addresses")
    val customerAddresses: Call<CustomerAddressResponse>

    @get:GET("order/customerorders")
    val customerOrders: Call<CustomerOrdersResponse>

    @get:GET("checkout/opccheckoutforguest")
    val isGuestCheckout: Call<IsGuestCheckoutResponse>

    @get:GET("shoppingCart/wishlist")
    val wishList: Call<WishListProductResponse>

    @get:GET("categoriesNmanufactures/search")
    val advanceSearchOptions: Call<AdvanceSearchSpinnerOptionResponse>

    @GET("topics/AboutUs")
    fun aboutUs(): Call<AboutUsResponse>

    @GET("topics/PrivacyInfo")
    fun privacyPolicy() : Call<PrivacyPolicyResponse>

    @POST("AppStart")
    fun initApp(@Body appStartRequest: AppStartRequest): Call<AppThemeResponse>

    @GET("homepagebanner")
    fun getHomePageBanner(@Query(queryString) query: String): Call<HomePageBannerResponse>

    @GET("homepagecategories")
    fun getHomePageCategories(@Query(queryString) query: String): Call<HomePageCategoryResponse>

    @GET("homepageproducts")
    fun getHomePageProducts(@Query(queryString) query: String): Call<HomePageProductResponse>

    @GET("homepagemanufacture")
    fun getHomePageManufacturer(@Query(queryString) query: String): Call<HomePageManufacturerResponse>

    @GET("categoryfeaturedproductandsubcategory/{id}")
    fun getCategoryFeaturedProductAndSubcategory(@Path("id") id: Long): Call<CategoryFeaturedProductAndSubcategoryResponse>

    @GET("productdetails/{id}")
    fun getProductDetails(@Path("id") id: Long): Call<ProductDetailResponse>



    @POST("ProductDetailsPagePrice/{productId}")
    fun getUpdatedPrice(@Path("productId") id: Long, @Body list: List<KeyValuePair>): Call<PriceResponse>

    // Get shopping cart
    @POST("AddProductToCart/{productId}/{shoppingCartTypeId}")
    fun addProductIntoCart(@Path("productId") id: Long, @Path("shoppingCartTypeId") shoppingCartTypeId: Long, @Body list: List<KeyValuePair>): Call<AddtoCartResponse>

    @POST("ShoppingCart/UpdateCart")
    fun updateCartProductList(@Body list: List<KeyValuePair>): Call<CartProductListResponse>

    @GET("productdetails/{id}")
    fun getCartItemProductDetailResponse(@Path("id") id: Long, @QueryMap options: Map<String, String>): Call<ProductDetailResponse>

    @POST("ShoppingCart/ApplyDiscountCoupon")
    fun applyDiscountCoupon(@Body request: DiscountCouponRequest): Call<DiscountCouponApplyResponse>

    @POST("ShoppingCart/ApplyGiftCard")
    fun applyDiscount(@Body request: AddGiftRequest): Call<AddGiftVoucherResponse>


    @GET("ShoppingCart/RemoveDiscountCoupon")
    fun removeDiscountCoupon(): Call<BaseResponse>

    @POST("ShoppingCart/applycheckoutattribute")
    fun applyCheckoutAttribute(@Body list: List<KeyValuePair>): Call<OrderTotalResponse>

    @GET("customer/attributes")
    fun getCustomerAttribute(): Call<CustomerAtrributeResponse>

    @POST("customer/attributes")
    fun applyCustomerAttribute(@Body list: List<KeyValuePair>): Call<ShoppingCartCheckoutAttributeApplyResponse>

    @GET("country/getstatesbycountryid/{countryId}")
    fun getStates(@Path("countryId") id: String): Call<StateListResponse>

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

    @POST("customer/info")
    fun saveCustomerInfo(@Body customerInfo: CustomerInfo): Call<CustomerInfo>

    @POST("login") fun performLogin(@Body loginData: LoginData): Call<LoginResponse>


    @POST("customer/passwordrecovery")
    fun forgetPassword(@Body forgetData: ForgetData): Call<ForgetResponse>

    @POST("customer/register")
    fun preformRegistration(@Body customerRegistrationInfo: CustomerRegistrationInfo): Call<RegistrationResponse>

    @GET("checkout/checkoutcomplete")
    fun confirmCheckout(): Call<CheckoutConfirmResponse>

    @POST("checkout/checkpaypalaccount")
    fun confirmPayPalCheckout(@Body request: PaypalCheckoutRequest): Call<ConfirmPayPalCheckoutResponse>

    @POST("customer/address/edit/{id}")
    fun editAddress(@Path("id") id: Int, @Body list: List<KeyValuePair>): Call<EditAddressResponse>

    @POST("customer/address/add")
    fun addAddress(@Body keyValuePairs: List<KeyValuePair>): Call<AddAddressResponse>

    @GET("customer/address/remove/{id}")
    fun removeCustomerAddresses(@Path("id") id: Int): Call<RemoveCustomerAddressResponse>

    @GET("order/reorder/{id}")
    fun getReOrder(@Path("id") id: Int): Call<ReOrderResponse>

    @POST("customer/changepass")
    fun changePassword(@Body changePassword: ChangePasswordModel): Call<ChangePasswordResponse>

    @POST("facebookLogin")
    fun loginUsingFaceBook(@Body facebookLogin: FacebookLogin): Call<LoginResponse>

    @POST("ShoppingCart/UpdateWishlist")
    fun updateWishList(@Body keyValuePairs: List<KeyValuePair>): Call<WishistUpdateResponse>

    @POST("ShoppingCart/AddItemsToCartFromWishlist")
    fun addItemsToCartFromWishList(@Body keyValuePairs: List<KeyValuePair>): Call<WishListProductResponse>

    @POST("ShoppingCart/AddItemsToCartFromWishlist")
    fun addAllItemsToCartFromWishList(@Body keyValuePairs: List<KeyValuePair>): Call<WishListProductResponse>

    @POST("checkout/checkauthorizepayment")
    fun checkAuthorizePayment(@Body authorizePayment: AuthorizePayment): Call<ConfirmAutorizeDotNetCheckoutResponse>

    @POST("catalog/search")
    fun searchProduct(@Body q: Search, @QueryMap options : Map<String, String>): Call<ProductsResponse>*/


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
        val shoppingCartTypeCart = 1
        val shoppingCartTypeWishlist = 2
        val cartProductId: Long = 0

        @JvmStatic
        val DEFAULT_PAGE_SIZE = 9
    }
}
