package com.westerra.release.sso.amplifi

import android.webkit.WebView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.westerra.release.analytics.AnalyticsScreenNames
import com.westerra.release.analytics.WesterraAnalytics
import com.westerra.release.sso.WebViewFragment
import com.westerra.release.sso.model.isError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class AmplifiSSOFragment : WebViewFragment() {

    private val amplifiSSOViewModel: AmplifiSSOViewModel by viewModels()

    override fun onResume() {
        super.onResume()
        WesterraAnalytics.recordScreenView(screenName = AnalyticsScreenNames.AMPLIFI_WEB_VIEW)
    }

    override fun loadUrl(webView: WebView) {
        //TODO Modified below code as per deprecation, follow th same wherever demands or rollback if doesn't work as expected
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(state = Lifecycle.State.STARTED) {
                amplifiSSOViewModel.amplifiSSOToken().collectLatest { ssoTokenResponse ->
                    if (ssoTokenResponse.isError() || ssoTokenResponse.ssoUrl.isEmpty()) {
                        activity?.lifecycleScope?.launch(Dispatchers.Main) {
                            showNetworkRetryAlert(webView = webView)
                        }
                    } else {
                        webView.loadUrl(ssoTokenResponse.ssoUrl)
                    }
                }
            }
        }
    }
}
