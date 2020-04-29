package com.bs.ecommerce.more

import android.os.Bundle
import android.view.View
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat
import com.bs.ecommerce.R
import com.bs.ecommerce.base.BaseFragment
import com.bs.ecommerce.base.BaseViewModel
import com.bs.ecommerce.main.MainViewModel
import com.bs.ecommerce.utils.Language
import com.bs.ecommerce.utils.show
import kotlinx.android.synthetic.main.fragment_privacy_policy.*
import java.util.*

class PrivacyPolicyFragment : BaseFragment() {

    override fun getFragmentTitle() = R.string.title_privacy_policy

    override fun getLayoutId(): Int = R.layout.fragment_privacy_policy

    override fun getRootLayout(): RelativeLayout? = privacyPolicyLayout

    override fun createViewModel(): BaseViewModel = MainViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        wv_privacy_policy?.show(getString(R.string.placeholder_long), R.color.fragment_background)

    }
}