package com.bs.ecommerce.utils

class Const {

    companion object {
        const val TITLE_FORGOT_PASS = "account.passwordrecovery"
        const val TITLE_CHANGE_PASS = "account.changepassword"
        const val TITLE_REGISTER = "pagetitle.register"
        const val TITLE_REVIEW = "reviews"
        const val TITLE_ORDER_DETAILS = "pagetitle.orderdetails"
        const val TITLE_SEARCH = "pagetitle.search"
        const val TITLE_ACCOUNT = "pagetitle.account"

        const val HOME_NAV_HOME = "common.home"
        const val HOME_NAV_CATEGORY = "nopstation.webapi.common.category"
        const val HOME_NAV_SEARCH = "common.search"
        const val HOME_NAV_ACCOUNT = "nopstation.webapi.common.account"
        const val HOME_NAV_MORE = "nopstation.webapi.common.more"

        const val HOME_MANUFACTURER = "manufacturers"
        const val HOME_FEATURED_PRODUCT = "homepage.products"
        const val HOME_BESTSELLER = "bestsellers"

        const val CHANGE_PASS_BTN = "account.changepassword.button"
        const val CHANGE_PASS_OLD = "account.changepassword.fields.oldpassword"
        const val CHANGE_PASS_OLD_REQ = "account.changepassword.fields.oldpassword.required"
        const val CHANGE_PASS_NEW = "account.changepassword.fields.newpassword"
        const val CHANGE_PASS_NEW_REQ = "account.changepassword.fields.confirmnewpassword.required"
        const val CHANGE_PASS_CONFIRM = "account.changepassword.fields.confirmnewpassword"
        const val CHANGE_PASS_CONFIRM_REQ = "account.changepassword.fields.confirmnewpassword.required"
        const val CHANGE_PASS_MISMATCH = "account.changepassword.fields.newpassword.enteredpasswordsdonotmatch"

        const val FORGOT_PASS_BTN = "account.passwordrecovery.changepasswordbutton"
        const val FORGOT_PASS_EMAIL = "account.passwordrecovery.email"
        const val FORGOT_PASS_EMAIL_REQ = "account.passwordrecovery.email.required"

        const val REVIEW_WRITE = "reviews.write"
        const val REVIEW_HELPFUL = "reviews.helpfulness.washelpful?"
        const val REVIEW_TITLE = "reviews.fields.title"
        const val REVIEW_TITLE_REQ = "reviews.fields.title.required"
        const val REVIEW_TEXT = "reviews.fields.reviewtext"
        const val REVIEW_TEXT_REQ = "reviews.fields.reviewtext.required"
        const val REVIEW_SUBMIT_BTN = "reviews.submitbutton"
        const val REVIEW_RATING = "reviews.fields.rating"

        const val SETTINGS_URL = "nopstation.webapi.settings.nopcommerceurl"
        const val SETTINGS_LANGUAGE = "nopstation.webapi.settings.language"
        const val SETTINGS_CURRENCY = "nopstation.webapi.settings.currency"
        const val SETTINGS_THEME = "nopstation.webapi.settings.darktheme"
        const val SETTINGS_INVALID_URL = "nopstation.webapi.settings.invalidurl"
        const val SETTINGS_BTN_TEST = "nopstation.webapi.settings.test"
        const val SETTINGS_BTN_SET_DEFAULT = "nopstation.webapi.settings.setdefault"

        const val ENTER_VALID_EMAIL = "nopstation.webapi.common.entervalidemail"
        const val ENTER_PRICE = "nopstation.webapi.shoppingcart.donation.enterprice"
        const val ENTER_PRICE_REQ = "nopstation.webapi.shoppingcart.donation.enterprice.required"


        const val LOGIN_EMAIL = "account.login.fields.email"
        const val LOGIN_EMAIL_REQ = "account.login.fields.email.required"
        const val LOGIN_PASS = "account.login.fields.password"
        const val LOGIN_PASS_REQ = "nopstation.webapi.login.password.required"
        const val LOGIN_NEW_CUSTOMER = "account.login.newcustomer"
        const val LOGIN_FORGOT_PASS = "account.login.forgotpassword"
        const val LOGIN_LOGIN_BTN = "account.login.loginbutton"
        const val LOGIN_OR = "nopstation.webapi.login.or"
        const val LOGIN_LOGIN_WITH_FB = "nopstation.webapi.loginwithfb"
        const val LOGIN_LOGIN_SUCCESS = "nopstation.webapi.loginsuccess"

        const val REWARD_NO_HISTORY = "rewardpoints.nohistory"
        const val REWARD_POINT_DATE = "rewardpoints.fields.createddate"
        const val REWARD_POINT_END_DATE = "rewardpoints.fields.enddate"
        const val REWARD_POINT_MSG = "rewardpoints.fields.message"
        const val REWARD_POINT_ = "rewardpoints.fields.points"
        const val REWARD_POINT_BALANCE = "rewardpoints.fields.pointsbalance"
        const val REWARD_POINT_BALANCE_MIN = "rewardpoints.minimumbalance"
        const val REWARD_POINT_BALANCE_CURRENT = "rewardpoints.currentbalance"

        const val ORDER_NUMBER = "account.customerorders.ordernumber"
        const val ORDER_STATUS = "account.customerorders.orderstatus"
        const val ORDER_TOTAL = "account.customerorders.ordertotal"
        const val ORDER_DATE = "account.customerorders.orderdate"
        const val ORDER_PRICE = "order.product(s).price"
        const val ORDER_QUANTITY = "order.product(s).quantity"
        const val ORDER_TOTAL_ = "order.product(s).sku"

        const val CATALOG_ITEMS_PER_PAGE = "catalog.pagesize.label"
        const val CATALOG_ORDER_BY = "catalog.orderby"

        const val MORE_SCAN_BARCODE = "nopstation.webapi.scanbarcode"
        const val MORE_SETTINGS = "nopstation.webapi.settings"
        const val MORE_PRIVACY_POLICY = "nopstation.webapi.privacypolicy"
        const val MORE_ABOUT_US = "nopstation.webapi.aboutus"
        const val MORE_CONTACT_US = "contactus"

        const val ACCOUNT_MY_REVIEW = "account.customerproductreviews"
        const val ACCOUNT_INFO = "nopstation.webapi.account.info"
        const val ACCOUNT_CUSTOMER_ADDRESS = "account.customeraddresses"
        const val ACCOUNT_SHOPPING_CART = "account.shoppingcart"
        const val ACCOUNT_REWARD_POINT = "account.rewardpoints"
        const val ACCOUNT_LOGIN = "account.login"
        const val ACCOUNT_LOGOUT = "account.logout"
        const val ACCOUNT_LOGOUT_CONFIRM = "nopstation.webapi.account.logoutconfirmation"
        const val ACCOUNT_ORDERS = "account.customerorders"
        const val ACCOUNT_WISHLIST = "wishlist"

        const val CONTACT_US_FULLNAME = "contactus.fullname.hint"
        const val CONTACT_US_EMAIL = "contactus.email.hint"
        const val CONTACT_US_ENQUIRY = "contactus.enquiry.hint"
        const val CONTACT_US_SUBJECT = "contactus.subject.hint"
        const val CONTACT_US_BUTTON = "contactus.button"
        const val CONTACT_US_REQUIRED_FULLNAME = "contactus.fullname.required"
        const val CONTACT_US_REQUIRED_EMAIL = "contactvendor.email.required"
        const val CONTACT_US_REQUIRED_ENQUIRY = "contactus.enquiry.required"

        const val VENDOR_CONTACT_VENDOR = "contactvendor"
        const val VENDOR_FULLNAME = "contactvendor.fullname.hint"
        const val VENDOR_EMAIL = "contactvendor.email.hint"
        const val VENDOR_ENQUIRY = "contactvendor.enquiry.hint"
        const val VENDOR_SUBJECT = "contactvendor.subject.hint"
        const val VENDOR_BUTTON = "contactvendor.button"
        const val VENDOR_REQUIRED_FULLNAME = "contactvendor.fullname.required"
        const val VENDOR_REQUIRED_EMAIL = "contactvendor.email.required"
        const val VENDOR_REQUIRED_ENQUIRY = "contactvendor.enquiry.required"
        const val VENDOR_REQUIRED_SUBJECT = "contactvendor.subject.required"

        const val ADDRESS_COMPANY_NAME = "account.fields.company"
        const val ADDRESS_FAX = "account.fields.fax"
        const val ADDRESS_PHONE = "account.fields.phone"
        const val ADDRESS_EMAIL = "account.fields.email"

        const val FILTER = "nopstation.webapi.filtering.filter"
        const val FILTER_REMOVE = "filtering.pricerangefilter.remove"
        const val FILTER_PRICE_RANGE = "filtering.pricerangefilter"
        const val FILTER_SPECIFICATION = "filtering.specificationfilter"
        const val FILTER_FILTER_BY = "filtering.specificationfilter.currentlyfilteredby"

        const val SEARCH_QUERY_LENGTH = "search.searchtermminimumlengthisncharacters"
        const val SEARCH_NO_RESULT = "search.noresultstext"

        const val PRODUCT_GROUPED_PRODUCT = "enums.nop.core.domain.catalog.producttype.groupedproduct"
        const val PRODUCT_MANUFACTURER = "products.manufacturer"
        const val PRODUCT_TAG = "products.tags"
        const val PRODUCT_VENDOR = "products.vendor"
        const val PRODUCT_AVAILABILITY = "products.availability"
        const val PRODUCT_QUANTITY = "products.tierprices.quantity"
        const val PRODUCT_FREE_SHIPPING = "products.freeshipping"
        const val PRODUCT_INVALID_PRODUCT = "nopstation.webapi.sliders.fields.entityid.invalidproduct"
        const val PRODUCT_BTN_BUY_NOW = "nopstation.webapi.shoppingcart.buynow"
        const val PRODUCT_BTN_RENT_NOW = "nopstation.webapi.shoppingcart.rentnow"
        const val PRODUCT_WISHLIST_DISABLED = "shoppingcart.wishlistdisabled"
        const val PRODUCT_BUY_DISABLED = "shoppingcart.buyingdisabled"
        const val PRODUCT_BTN_ADDTOCART = "shoppingcart.addtocart"
        const val PRODUCT_BTN_ADDTO_WISHLIST = "shoppingcart.addtowishlist"
        const val PRODUCT_ADDED_TO_WISHLIST = "products.producthasbeenaddedtothewishlist"
        const val PRODUCT_ALSO_PURCHASED = "products.alsopurchased"
        const val PRODUCT_DESCRIPTION = "account.vendorinfo.description"
        const val PRODUCT_RELATED_PRODUCT = "products.relatedproducts"
        const val PRODUCT_RENTAL_START = "shoppingcart.rental.enterstartdate"
        const val PRODUCT_RENTAL_END = "shoppingcart.rental.enterenddate"
        const val PRODUCT_RENT = "shoppingcart.rent"
        const val PRODUCT_GIFT_CARD_SENDER = "products.giftcard.sendername"
        const val PRODUCT_GIFT_CARD_RECIEPIENT = "products.giftcard.recipientname"
        const val PRODUCT_GIFT_CARD_MESSAGE = "products.giftcard.message"
        const val PRODUCT_QUANTITY_POSITIVE = "shoppingcart.quantityshouldpositive"



        const val WISHLIST_NO_ITEM = "wishlist.cartisempty"
        const val WISHLIST_ADD_ALL_TO_CART = "nopstation.webapi.wishlist.addall"

        const val APP_UPDATE_MSG = "nopstation.webapi.update.msg"
        const val APP_UPDATE_BTN = "nopstation.webapi.update.label"

        const val COMMON_SOMETHING_WENT_WRONG = "nopstation.webapi.common.somethingwrong"
        const val COMMON_NO_DATA = "nopstation.webapi.common.nodata"
        const val COMMON_SEE_ALL = "nopstation.webapi.home.seeall"
        const val COMMON_DONE = "nopstation.webapi.common.done"
        const val COMMON_PLEASE_WAIT = "nopstation.webapi.common.pleasewait"
        const val COMMON_SELECT = "nopstation.webapi.common.select"
        const val COMMON_AGAIN_PRESS_TO_EXIT = "nopstation.webapi.home.pressagaintoexit"
        const val COMMON_YES = "common.yes"
        const val COMMON_NO = "common.no"


        //SHOPPING CART
        const val SHOPPING_CART_TITLE = "pagetitle.shoppingcart"

        const val PRODUCTS = "shoppingcart.product(s)"
        const val ITEMS = "shoppingcart.mini.items"
        const val ENTER_YOUR_COUPON = "shoppingcart.discountcouponcode.tooltip"
        const val ENTER_GIFT_CARD = "shoppingcart.giftcardcouponcode.tooltip"

        const val APPLY_COUPON = "shoppingcart.discountcouponcode.button"
        const val ADD_GIFT_CARD = "shoppingcart.giftcardcouponcode.button"

        const val ENTERED_COUPON_CODE = "shoppingcart.discountcouponcode.currentcode"

        const val SUB_TOTAL = "shoppingcart.totals.subtotal"
        const val SHIPPING = "shoppingcart.totals.shipping"
        const val DISCOUNT = "shoppingcart.totals.subtotaldiscount"
        const val GIFT_CARD = "shoppingcart.totals.giftcardinfo"
        const val GIFT_CARD_REMAINING = "shoppingcart.totals.giftcardinfo.remaining"
        const val TAX = "shoppingcart.totals.tax"
        const val TOTAL = "shoppingcart.totals.ordertotal"

        const val CART_EMPTY = "shoppingcart.cartisempty"
        const val CHECKOUT = "checkout.button"



        const val CALCULATED_DURING_CHECKOUT = "shoppingcart.totals.calculatedduringcheckout"


        // GUEST CHECOUT DIALOG

        const val CHECKOUT_AS_GUEST_TITLE = "account.login.checkoutasguestorregister"
        const val CHECKOUT_AS_GUEST = "account.login.checkoutasguest"
        const val REGISTER_AND_SAVE_TIME = "nopstation.webapi.registersavetime"
        const val CREATE_ACCOUNT_LONG_TEXT = "nopstation.webapi.creatingaccountlongtext"
        const val RETURNING_CUSTOMER = "nopstation.webapi.returningcustomer"




        //CHECKOUT
        const val PLEASE_COMPLETE_PREVIOUS_STEP = "nopstation.webapi.checkout.completepreviousstep"

        const val ADDRESS_TAB = "checkout.progress.address"
        const val SHIPPING_TAB = "checkout.progress.shipping"
        const val PAYMENT_TAB = "checkout.progress.payment"
        const val CONFIRM_TAB = "checkout.progress.confirm"

        const val BILLING_ADDRESS_TAB = "checkout.billingaddress"
        const val SHIPPING_ADDRESS_TAB = "checkout.shippingaddress"

        const val SHIP_TO_SAME_ADDRESS = "checkout.shiptosameaddress"

        const val NEW_ADDRESS = "checkout.newaddress"

        //Payment method
        const val USE_REWARD_POINTS = "checkout.userewardpoints"


        const val FIRST_NAME = "address.fields.firstname"
        const val LAST_NAME = "address.fields.lastname"
        const val COMPANY = "address.fields.company"
        const val CITY = "address.fields.city"
        const val PHONE = "address.fields.phonenumber"
        const val FAX = "address.fields.fax"
        const val ADDRESS_1 = "address.fields.address1"
        const val ADDRESS_2 = "address.fields.address2"
        const val EMAIL = "address.fields.email"


        const val REGISTRATION_PERSONAL_DETAILS = "nopstation.webapi.registration.personaldetails"
        const val REGISTRATION_PASSWORD = "nopstation.webapi.registration.password"
        const val GENDER = "account.fields.gender"
        const val GENDER_MALE = "account.fields.gender.male"
        const val GENDER_FEMALE = "account.fields.gender.female"


        const val ENTER_PASSWORD = "nopstation.webapi.account.fields.enterpassword"
        const val CONFIRM_PASSWORD = "account.fields.confirmpassword"
        const val OPTIONS = "account.options"
        const val CHANGE_PASSWORD = "account.changepassword"
        const val NEWSLETTER = "account.fields.newsletter"

        const val DATE_OF_BIRTH = "account.fields.dateofbirth"

        const val USERNAME = "account.fields.username"

        const val SELECT_COUNTRY = "address.selectcountry"

        const val SELECT_STATE = "address.selectstate"

        const val STATE_PROVINCE = "address.fields.stateprovince"
        const val ZIP_CODE = "address.fields.zippostalcode"

        const val ADD_NEW_ADDRESS = "account.customeraddresses.addnew"

        const val UPDATED_SUCCESSFULLY = "nopstation.webapi.updated"

        const val ADDRESS_UPDATED_SUCCESSFULLY = "nopstation.webapi.address.fields.addressupdated"
        const val ADDRESS_SAVED_SUCCESSFULLY = "nopstation.webapi.address.fields.addresssaved"


        const val CONFIRM_DELETE_ADDRESS = "nopstation.webapi.address.fields.confirmdeleteaddress"
        const val DELETE_ADDRESS = "nopstation.webapi.address.fields.deleteaddress"

        const val STREET_ADDRESS = "account.fields.streetaddress"
        const val STREET_ADDRESS_2 = "account.fields.streetaddress2"
        const val COUNTRY = "account.fields.country"


        const val IS_REQUIRED = "nopstation.webapi.common.isrequired"


        //Confirm Order
        const val PICK_UP_POINT_ADDRESS = "order.pickupaddress"
        const val SHIPPING_METHOD = "checkout.shippingmethod"
        const val PAYMENT_METHOD = "checkout.paymentmethod"
        const val SELECTED_ATTRIBUTES = "nopstation.webapi.checkout.selectedattributes"
        const val ORDER_CALCULATION = "nopstation.webapi.checkout.ordercalculation"
        const val WILL_EARN = "shoppingcart.totals.rewardpoints.willearn"
        const val POINTS = "shoppingcart.totals.rewardpoints.willearn.point"
        const val QUANTITY = "order.product(s).quantity"
        const val PRICE = "order.product(s).price"
        const val CHECKOUT_ORDER_NUMBER = "checkout.ordernumber"
        const val CHECKOUT_ORDER_PLACED = "nopstation.webapi.order.confirmed" // Your Order is Confirmed

        const val CONTINUE = "common.continue"
        const val REGISTER_BUTTON = "account.register.button"
        const val SAVE_BUTTON = "common.save"
        const val CONFIRM_BUTTON = "checkout.confirmbutton"

        const val ONLINE_PAYMENT = "nopstation.webapi.checkout.onlinepayment"


        // Barcode
        const val PUT_BARCODE_HERE = "nopstation.webapi.barcode.putbarcodehere"
        const val INVALID_PRODUCT = "nopstation.webapi.barcode.invalidproduct"

        //Order Complete Dialog

        const val ORDER_PROCESSED = "checkout.yourorderhasbeensuccessfullyprocessed"
        const val THANK_YOU = "checkout.thankyou"

        const val NO_INTERNET = "nopstation.webapi.nointernet"
        const val SETTINGS = "nopstation.webapi.settings"
        const val TRY_AGAIN = "nopstation.webapi.tryagain"

        const val READ_BEFORE_CONTINUE = "nopstation.webapi.readbeforecontinue"
        const val I_READ_I_ACCEPT = "nopstation.webapi.accept"

        //search
        const val ADVANCED_SEARCH = "nopstation.webapi.search.advancedsearch"
        const val SEARCH_ALL = "nopstation.webapi.search.all"
        const val AUTOMATICALLY_SEARCH_SUBCATEGORIES = "nopstation.webapi.search.subcategorysearchautomatically"
        const val PRICE_RANGE = "nopstation.webapi.search.pricerange"
        const val TO = "nopstation.webapi.search.to"
        const val SEARCH_IN_PRODUCT_DISCRIPTIONS = "nopstation.webapi.search.searchindiscriptions"

    }

}