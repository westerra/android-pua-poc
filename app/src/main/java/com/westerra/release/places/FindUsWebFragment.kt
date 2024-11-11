package com.westerra.release.places

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import com.westerra.release.R
import com.westerra.release.databinding.FindUsWebViewBinding
import com.westerra.release.extensions.fadeGone

class FindUsWebFragment : Fragment() {

    // This property is only valid between onCreateView and onDestroyView.
    private var _binding: FindUsWebViewBinding? = null
    val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FindUsWebViewBinding.inflate(inflater, container, false)

        binding.toolbar.apply {
            setNavigationIcon(R.drawable.backbase_ic_close)
            setNavigationOnClickListener {
                activity?.onBackPressedDispatcher?.onBackPressed()
            }
        }

        return binding.root
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.webView.apply webView@{
            this@webView.settings.javaScriptEnabled = true
            this@webView.settings.javaScriptCanOpenWindowsAutomatically = true
            this@webView.settings.builtInZoomControls = true
            this@webView.settings.setGeolocationEnabled(true)
            this@webView.webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    _binding?.webviewProgressbar?.fadeGone()
                }
            }
        }

        binding.webView.loadUrl("https://www.westerracu.com/locations")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
