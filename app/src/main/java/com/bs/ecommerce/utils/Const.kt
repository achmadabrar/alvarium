package com.bs.ecommerce.utils

class Const {

    companion object {

        val PREFIX  = ""


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