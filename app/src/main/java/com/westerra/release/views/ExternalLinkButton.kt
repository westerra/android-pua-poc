package com.westerra.release.views

import android.content.Context
import android.util.AttributeSet
import com.westerra.release.WesterraApplication
import com.westerra.release.constants.Constants

class ExternalLinkButton
@JvmOverloads
constructor(
    context: Context,
    attrs: AttributeSet? = null,
) : androidx.appcompat.widget.AppCompatButton(context, attrs) {
    init {
        setOnClickListener {
            launchToWebView()
        }
    }

    private fun launchToWebView() {
        WesterraApplication.getInstance().launchExternalBrowser(url = Constants.WESTERRA_HOME_URL)
    }
}
