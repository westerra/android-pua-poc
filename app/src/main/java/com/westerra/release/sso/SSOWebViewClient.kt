package com.westerra.release.sso

import android.view.View
import android.webkit.CookieManager
import android.webkit.WebView
import android.webkit.WebViewClient
import com.westerra.release.extensions.fadeGone

class SSOWebViewClient(private val progressView: View?) : WebViewClient() {

    override fun onPageFinished(view: WebView?, url: String?) {
        progressView?.fadeGone()
        super.onPageFinished(view, url)
        CookieManager.getInstance().flush()
    }
}
