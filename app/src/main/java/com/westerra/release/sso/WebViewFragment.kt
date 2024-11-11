package com.westerra.release.sso

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import androidx.fragment.app.Fragment
import com.westerra.release.alerts.NetworkRetryAlert
import com.westerra.release.constants.Constants.KEY_TOOLBAR_TITLE
import com.westerra.release.databinding.FragmentWebViewBinding
import com.westerra.release.extensions.applyDefaultSettings

abstract class WebViewFragment : Fragment() {

    // This property is only valid between onCreateView and onDestroyView.
    private var _binding: FragmentWebViewBinding? = null
    val binding get() = _binding!!

    abstract fun loadUrl(webView: WebView)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentWebViewBinding.inflate(inflater, container, false)
        binding.backButton.setOnClickListener {
            goBack()
        }
        arguments?.getString(KEY_TOOLBAR_TITLE, null) ?. let {
            binding.toolbarTitleText.text = it
        }
        return binding.root
    }

    private fun goBack() {
        binding.backButton.isEnabled = false
        activity?.onBackPressedDispatcher?.onBackPressed()
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.webView.apply webView@{
            this@webView.applyDefaultSettings()
            this@webView.webViewClient = SSOWebViewClient(progressView = binding.webviewProgressbar)
        }
        loadUrl(binding.webView)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    protected fun showNetworkRetryAlert(webView: WebView) {
        activity ?.let {
            NetworkRetryAlert(it) {
                loadUrl(webView = webView)
            }.show()
        }
    }
}
