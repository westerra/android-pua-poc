package com.westerra.release.awswaf

import android.util.Log
import com.amazonaws.waf.mobilesdk.publicmodel.SDKError
import com.amazonaws.waf.mobilesdk.publicmodel.WAFToken
import com.backbase.android.core.utils.BBLogger
import com.backbase.android.retail.journey.app.us.UsApplication
import com.westerra.release.identity.WafReadySignal

abstract class WafApplication : UsApplication() {

    companion object {
        private val TAG = WafApplication::class.java.simpleName
    }

    override fun onCreate() {
        super.onCreate()
        initAwsWafTokenProvider()
    }

    /*
        Initialize AWS WAF token provider singleton instance
     */
    private fun initAwsWafTokenProvider() {
        WesterraWafToken(applicationContext)
        WesterraWafToken.getWafTokenProvider()
            .onTokenReady { wafToken: WAFToken, error: SDKError? ->
                if (!wafToken.isTokenNullOrEmpty) {
                    WafReadySignal.isTokenAvailable = true
                    Log.d(TAG, "WAF token available")
                } else {
                    WafReadySignal.isTokenAvailable = false
                    BBLogger.error(TAG, error?.value ?: "Token Error was Null!")
                }
            }
    }
}
