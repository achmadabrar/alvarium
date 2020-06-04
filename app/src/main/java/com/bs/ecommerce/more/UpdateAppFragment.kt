package com.bs.ecommerce.more

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.RelativeLayout
import com.bs.ecommerce.R
import com.bs.ecommerce.base.BaseFragment
import com.bs.ecommerce.base.BaseViewModel
import com.bs.ecommerce.db.DbHelper
import com.bs.ecommerce.utils.Const
import com.bs.ecommerce.utils.toast
import kotlinx.android.synthetic.main.fragment_update_app.*


class UpdateAppFragment : BaseFragment() {

    override fun getFragmentTitle() = ""

    override fun getLayoutId(): Int = R.layout.fragment_update_app

    override fun getRootLayout(): RelativeLayout? = updateAppRootLayout

    override fun createViewModel(): BaseViewModel = BaseViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvMessage.text = DbHelper.getString(Const.APP_UPDATE_MSG)
        btnUpdate.text = DbHelper.getString(Const.APP_UPDATE_BTN)

        btnUpdate.setOnClickListener {

            arguments?.getString(KEY_PLAY_STORE_URL)?.let {
                if(it.isNotEmpty()) {

                    try {
                        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(it)))
                    } catch (e: ActivityNotFoundException) {
                        toast(DbHelper.getString(Const.COMMON_SOMETHING_WENT_WRONG))
                    }
                }
            }
        }
    }

    companion object {
        @JvmStatic
        private val KEY_PLAY_STORE_URL: String = "KEY_PLAY_STORE_URL"

        fun newInstance(url: String): UpdateAppFragment {
            val fragment = UpdateAppFragment()

            fragment.arguments = Bundle().apply {
                putString(KEY_PLAY_STORE_URL, url)
            }

            return fragment
        }
    }
}
