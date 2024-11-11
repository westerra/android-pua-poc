package com.westerra.release.sso.dmi

import android.webkit.WebView
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.westerra.release.analytics.AnalyticsScreenNames
import com.westerra.release.analytics.WesterraAnalytics
import com.westerra.release.constants.Constants.KEY_INTERNAL_ARRANGEMENT_ID
import com.westerra.release.constants.Constants.KEY_IS_PAYMENTS
import com.westerra.release.sso.WebViewFragment
import com.westerra.release.sso.model.isError
import com.westerra.release.sso.network.MTGSVCClient

class DMISSOFragment : WebViewFragment() {
    private val dmiSSOViewModel: DMISSOViewModel by viewModels {
        DMISSOViewModelFactory(
            internalArrangementsId = arguments?.getString(KEY_INTERNAL_ARRANGEMENT_ID) ?: "",
            isPayments = arguments?.getBoolean(KEY_IS_PAYMENTS) ?: false,
        )
    }

    override fun onResume() {
        super.onResume()
        WesterraAnalytics.recordScreenView(screenName = AnalyticsScreenNames.DMI_WEB_VIEW)
    }

    override fun loadUrl(webView: WebView) {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            dmiSSOViewModel.dmiResponse.observe(viewLifecycleOwner) { dmiResponse ->
                val location = dmiResponse.location
                if (dmiResponse.isError() || location == null) {
                    showNetworkRetryAlert(webView = webView)
                    return@observe
                }
                webView.loadUrl(MTGSVCClient.getDMIDomain() + location)
            }
        }
    }
}
