package com.westerra.release.extensions

import android.annotation.SuppressLint
import android.webkit.WebView
import com.westerra.release.constants.Constants.TEXT_HTML_UTF8_MIME_TYPE
import com.westerra.release.constants.Constants.UTF8_ENCODING

fun WebView.loadHtmlData(baseUrl: String?, data: String) {
    this.loadDataWithBaseURL(
        baseUrl,
        data,
        TEXT_HTML_UTF8_MIME_TYPE,
        UTF8_ENCODING,
        null,
    )
}

@SuppressLint("SetJavaScriptEnabled")
fun WebView.applyDefaultSettings() {
    this.settings.javaScriptEnabled = true
    this.settings.loadsImagesAutomatically = true
    this.settings.domStorageEnabled = true
}
