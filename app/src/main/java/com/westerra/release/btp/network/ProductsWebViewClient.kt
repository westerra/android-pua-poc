package com.westerra.release.btp.network

import android.webkit.WebView
import android.webkit.WebViewClient
import com.westerra.release.databinding.CardWesterraProductBinding
import com.westerra.release.extensions.fadeIn

/*
    Need to prevent following redirects from launching external browser.
 */
class ProductsWebViewClient(private val productCard: CardWesterraProductBinding?) :
    WebViewClient() {
    override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
        view.loadUrl(url)
        return false
    }

    override fun onPageFinished(view: WebView?, url: String?) {
        if (url != null && !url.contains("return_to")) {
            productCard?.cardContainer?.fadeIn()
        }
        super.onPageFinished(view, url)
    }
}
