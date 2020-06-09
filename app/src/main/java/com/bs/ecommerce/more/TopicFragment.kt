package com.bs.ecommerce.more

import android.os.Bundle
import android.view.View
import android.widget.RelativeLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bs.ecommerce.R
import com.bs.ecommerce.base.BaseFragment
import com.bs.ecommerce.base.BaseViewModel
import com.bs.ecommerce.more.model.TopicModel
import com.bs.ecommerce.more.model.TopicModelImpl
import com.bs.ecommerce.more.viewmodel.TopicViewModel
import com.bs.ecommerce.utils.show
import kotlinx.android.synthetic.main.fragment_topic.*

class TopicFragment : BaseFragment() {

    private lateinit var model: TopicModel

    override fun getFragmentTitle() = ""

    override fun getLayoutId(): Int = R.layout.fragment_topic

    override fun getRootLayout(): RelativeLayout? = privacyPolicyLayout

    override fun createViewModel(): BaseViewModel = TopicViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(!viewCreated) {

            model = TopicModelImpl()
            viewModel = ViewModelProvider(this).get(TopicViewModel::class.java)

            arguments?.getString(keyTopicName)?.let {
                (viewModel as TopicViewModel).fetchTopic(it, model)
            }

            arguments?.getInt(keyTopicId)?.let {
                (viewModel as TopicViewModel).fetchTopic(it, model)
            }
        }

        setupLiveDataListener()
    }

    private fun setupLiveDataListener() {

        (viewModel as TopicViewModel).apply {

            topicLD.observe(viewLifecycleOwner, Observer { topic ->

                // Set Toolbar title
                if(isAdded && lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)) {
                    requireActivity().title = topic?.title ?: ""
                    arguments?.putString(keyTopicName, topic?.title ?: "")
                }


                topic?.body?.also {
                    wv_privacy_policy?.show( it, R.color.fragment_background)

                    /*webViewClient = object : WebViewClient() {
                        override fun onPageFinished(view: WebView, url: String) {
                            "nop_".showLog("onPageFinished")
                            hideLoading()
                        }
                    }*/
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

    companion object {
        @JvmStatic private val keyTopicName: String = "key_topic_name"
        @JvmStatic private val keyTopicId: String = "key_topic_id"

        fun newInstance(topicName: String): TopicFragment {
            val fragment = TopicFragment()

            fragment.arguments = Bundle().apply {
                putString(keyTopicName, topicName)
            }

            return fragment
        }

        fun newInstance(topicId: Int): TopicFragment {
            val fragment = TopicFragment()

            fragment.arguments = Bundle().apply {
                putInt(keyTopicName, topicId)
            }

            return fragment
        }
    }
}