package com.westerra.release.sso.payveris

import android.webkit.WebView
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.westerra.release.BuildConfig
import com.westerra.release.analytics.AnalyticsScreenNames
import com.westerra.release.analytics.WesterraAnalytics
import com.westerra.release.constants.Constants
import com.westerra.release.constants.Constants.KEY_SSO_LINK
import com.westerra.release.sso.WebViewFragment
import com.westerra.release.sso.model.isError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PayverisSSOFragment : WebViewFragment() {

    private val payverisSSOViewModel: PayverisSSOViewModel by viewModels()

    override fun onResume() {
        super.onResume()
        WesterraAnalytics.recordScreenView(screenName = AnalyticsScreenNames.PAYVERIS_WEB_VIEW)
    }

    override fun loadUrl(webView: WebView) {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            payverisSSOViewModel.payverisSSOToken.observe(viewLifecycleOwner) { response ->
                if (response.isError() || response.ssoUrl.isEmpty()) {
                    activity?.lifecycleScope?.launch(Dispatchers.Main) {
                        showNetworkRetryAlert(webView = webView)
                    }
                } else {
                    val url = getDomain() + arguments?.getString(KEY_SSO_LINK) + response.ssoUrl
                    webView.loadUrl(url)
                }
            }
        }
    }

    private fun getDomain(): String {
        return if (BuildConfig.FLAVOR == Constants.BUILD_FLAVOR_PROD) {
            Constants.PAYVERIS_PROD_DOMAIN
        } else {
            Constants.PAYVERIS_TEST_DOMAIN
        }
    }
}
