package com.bs.ecommerce.more

import android.os.Bundle
import android.view.View
import android.widget.RelativeLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bs.ecommerce.R
import com.bs.ecommerce.base.BaseFragment
import com.bs.ecommerce.base.BaseViewModel
import com.bs.ecommerce.more.model.TopicModel
import com.bs.ecommerce.more.model.TopicModelImpl
import com.bs.ecommerce.more.viewmodel.TopicViewModel
import com.bs.ecommerce.networking.Api
import com.bs.ecommerce.utils.show
import kotlinx.android.synthetic.main.fragment_privacy_policy.*

class PrivacyPolicyFragment : BaseFragment() {

    private lateinit var model: TopicModel

    override fun getFragmentTitle() = R.string.title_privacy_policy

    override fun getLayoutId(): Int = R.layout.fragment_privacy_policy

    override fun getRootLayout(): RelativeLayout? = privacyPolicyLayout

    override fun createViewModel(): BaseViewModel = TopicViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(!viewCreated) {

            model = TopicModelImpl()
            viewModel = ViewModelProvider(this).get(TopicViewModel::class.java)

            (viewModel as TopicViewModel).fetchTopic(Api.topicPrivacyPolicy, model)
        }

        setupLiveDataListener()
    }

    private fun setupLiveDataListener() {

        (viewModel as TopicViewModel).apply {

            topicLD.observe(viewLifecycleOwner, Observer { topic ->

                topic?.body?.also {
                    wv_privacy_policy?.show( it, R.color.fragment_background)
                }
            })

            isLoadingLD.observe(viewLifecycleOwner, Observer { isShowLoader ->

                if (isShowLoader)
                    showLoading()
                else
                    hideLoading()
            })
        }
    }

    /*companion object {
        @JvmStatic
        private val keyTopicName: String = "key_topic_name"

        fun newInstance(topicName: String): PrivacyPolicyFragment {
            val fragment = PrivacyPolicyFragment()

            fragment.arguments = Bundle().apply {
                putString(keyTopicName, topicName)
            }

            return fragment
        }
    }*/
}