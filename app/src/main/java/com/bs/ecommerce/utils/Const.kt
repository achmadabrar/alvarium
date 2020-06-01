package com.bs.ecommerce.utils

class Const {

    companion object {
        val TITLE_FORGOT_PASS = "account.passwordrecovery"
        val TITLE_CHANGE_PASS = "account.changepassword"
        val TITLE_REGISTER = "pagetitle.register"
        val TITLE_REVIEW = "reviews"

        val CHANGE_PASS_BTN = "account.changepassword.button"
        val CHANGE_PASS_OLD = "account.changepassword.fields.oldpassword"
        val CHANGE_PASS_OLD_REQ = "account.changepassword.fields.oldpassword.required"
        val CHANGE_PASS_NEW = "account.changepassword.fields.newpassword"
        val CHANGE_PASS_NEW_REQ = "account.changepassword.fields.confirmnewpassword.required"
        val CHANGE_PASS_CONFIRM = "account.changepassword.fields.confirmnewpassword"
        val CHANGE_PASS_CONFIRM_REQ = "account.changepassword.fields.confirmnewpassword.required"
        val CHANGE_PASS_MISMATCH = "account.changepassword.fields.newpassword.enteredpasswordsdonotmatch"

        val FORGOT_PASS_BTN = "account.passwordrecovery.changepasswordbutton"
        val FORGOT_PASS_EMAIL = "account.passwordrecovery.email"
        val FORGOT_PASS_EMAIL_REQ = "account.passwordrecovery.email.required"

        val REVIEW_WRITE = "reviews.write"
        val REVIEW_HELPFUL = "reviews.helpfulness.washelpful?"
        val REVIEW_TITLE = "reviews.fields.title"
        val REVIEW_TITLE_REQ = "reviews.fields.title.required"
        val REVIEW_TEXT = "reviews.fields.reviewtext"
        val REVIEW_TEXT_REQ = "reviews.fields.reviewtext.required"
        val REVIEW_SUBMIT_BTN = "reviews.submitbutton"
        val REVIEW_RATING = "reviews.fields.rating"

        val ENTER_VALID_EMAIL = "nopstation.webapi.common.entervalidemail"
        val ENTER_PRICE = "nopstation.webapi.shoppingcart.donation.enterprice"
        val ENTER_PRICE_REQ = "nopstation.webapi.shoppingcart.donation.enterprice.required"


        val LOGIN_EMAIL = "account.login.fields.email"
        val LOGIN_EMAIL_REQ = "account.login.fields.email.required"
        val LOGIN_PASS = "account.login.fields.password"
        val LOGIN_PASS_REQ = "nopstation.webapi.login.password.required"
        val LOGIN_NEW_CUSTOMER = "account.login.newcustomer"
        val LOGIN_FORGOT_PASS = "account.login.forgotpassword"
        val LOGIN_LOGIN_BTN = "account.login.loginbutton"
        val LOGIN_OR = "nopstation.webapi.login.or"
        val LOGIN_LOGIN_WITH_FB = "nopstation.webapi.loginwithfb"

        val REWARD_NO_HISTORY = "rewardpoints.nohistory"
        val REWARD_POINT_DATE = "rewardpoints.fields.createddate"
        val REWARD_POINT_END_DATE = "rewardpoints.fields.enddate"
        val REWARD_POINT_MSG = "rewardpoints.fields.message"
        val REWARD_POINT_ = "rewardpoints.fields.points"
        val REWARD_POINT_BALANCE = "rewardpoints.fields.pointsbalance"
        val REWARD_POINT_BALANCE_MIN = "rewardpoints.minimumbalance"
        val REWARD_POINT_BALANCE_CURRENT = "rewardpoints.currentbalance"

        val ORDER_NUMBER = "account.customerorders.ordernumber"
        val ORDER_STATUS = "account.customerorders.orderstatus"
        val ORDER_TOTAL = "account.customerorders.ordertotal"
        val ORDER_DATE = "account.customerorders.orderdate"

        val ACCOUNT_MY_REVIEW = "account.customerproductreviews"
        val ACCOUNT_INFO = "nopstation.webapi.account.info"
        val ACCOUNT_REWARD_POINT = "account.rewardpoints"
        val ACCOUNT_LOGIN = "activitylog.publicstore.login"
        val ACCOUNT_LOGOUT = "activitylog.publicstore.logout"
        val ACCOUNT_LOGOUT_CONFIRM = "nopstation.webapi.account.logoutconfirmation"
        val ACCOUNT_ORDERS = "account.customerorders"
        val ACCOUNT_WISHLIST = "wishlist"

        val COMMON_NO_DATA = "nopstation.webapi.common.nodata"
        val COMMON_YES = "common.yes"
        val COMMON_NO = "common.no"

        val PREFIX  = ""

        //SHOPPING CART
        val SHOPPING_CART_TITLE = "${PREFIX}pagetitle.shoppingcart"

        val PRODUCTS = "${PREFIX}shoppingcart.product(s)"
        val ITEMS = "${PREFIX}shoppingcart.mini.items"
        val ENTER_YOUR_COUPON = "${PREFIX}shoppingcart.discountcouponcode.tooltip"
        val ENTER_GIFT_CARD = "${PREFIX}shoppingcart.giftcardcouponcode.tooltip"

        val APPLY_COUPON = "${PREFIX}shoppingcart.discountcouponcode.button"
        val ADD_GIFT_CARD = "${PREFIX}shoppingcart.giftcardcouponcode.button"

        val SUB_TOTAL = "${PREFIX}shoppingcart.totals.subtotal"
        val SHIPPING = "${PREFIX}shoppingcart.totals.shipping"
        val DISCOUNT = "${PREFIX}shoppingcart.totals.subtotaldiscount"
        val TAX = "${PREFIX}shoppingcart.totals.tax"
        val TOTAL = "${PREFIX}shoppingcart.totals.ordertotal"

        val CHECKOUT = "${PREFIX}checkout.button"



        val CALCULATED_DURING_CHECKOUT = "${PREFIX}shoppingcart.totals.calculatedduringcheckout"



        //CHECKOUT
        val ADDRESS_TAB = "${PREFIX}checkout.progress.address"
        val SHIPPING_TAB = "${PREFIX}checkout.progress.shipping"
        val PAYMENT_TAB = "${PREFIX}checkout.progress.payment"
        val CONFIRM_TAB = "${PREFIX}checkout.progress.confirm"
    }

}