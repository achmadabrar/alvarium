package com.bs.ecommerce.more

import android.os.Bundle
import android.view.View
import android.widget.RelativeLayout
import com.bs.ecommerce.R
import com.bs.ecommerce.base.BaseFragment
import com.bs.ecommerce.base.BaseViewModel
import com.bs.ecommerce.main.SplashScreenActivity
import kotlinx.android.synthetic.main.fragment_generic_error.*

class GenericErrorFragment: BaseFragment() {

    override fun getFragmentTitle() = ""

    override fun getLayoutId(): Int = R.layout.fragment_generic_error

    override fun getRootLayout(): RelativeLayout? = rootLayout

    override fun createViewModel(): BaseViewModel = BaseViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // No setting texts from DB. Because language may not be present at this point
        /*tvErrorMessage.text = DbHelper.getString(Const.COMMON_SOMETHING_WENT_WRONG)
        btnTryAgain.text = DbHelper.getString(Const.TRY_AGAIN)*/

        btnTryAgain.setOnClickListener {

            if(requireActivity() is SplashScreenActivity) {
                (requireActivity() as SplashScreenActivity).loadAppLandingData()
            }
        }
    }
}