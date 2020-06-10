package com.bs.ecommerce.checkout

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.bs.ecommerce.R
import com.bs.ecommerce.base.BaseActivity
import com.bs.ecommerce.db.DbHelper
import com.bs.ecommerce.main.MainActivity
import com.bs.ecommerce.main.MainViewModel
import com.bs.ecommerce.utils.Const
import kotlinx.android.synthetic.main.activity_payment_toolbar.*
import kotlinx.android.synthetic.main.activity_result.*


class ResultActivity : BaseActivity()
{

    override fun getLayoutId(): Int = R.layout.activity_result

    override fun createViewModel(): MainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)


        intent?.extras?.let {

            if(it.getInt(CheckoutConstants.CHECKOUT_STEP) == CheckoutConstants.Completed)
            {
                toolbarTitle.text =  DbHelper.getString(Const.CHECKOUT_ORDER_PLACED)

                resultText.text = DbHelper.getString(Const.CHECKOUT_ORDER_NUMBER)
                    .plus(" ")
                    .plus(it.getInt(CheckoutConstants.ORDER_ID))

                resultButton.text = DbHelper.getString(Const.COMMON_DONE)
            }
        }


        resultButton?.setOnClickListener {  goToMainActivity() }

        back_arrow?.setOnClickListener {    goToMainActivity() }
    }

    private fun goToMainActivity()
    {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }


}
