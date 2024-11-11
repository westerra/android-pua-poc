package com.westerra.release.awswaf

import android.content.Context
import android.util.Log
import com.amazonaws.waf.mobilesdk.publicmodel.WAFToken
import com.amazonaws.waf.mobilesdk.token.WAFConfiguration
import com.amazonaws.waf.mobilesdk.token.WAFTokenProvider
import com.westerra.release.constants.BuildFlavors
import com.westerra.release.identity.WafReadySignal
import java.net.CookieHandler
import java.net.URI
import java.net.URISyntaxException
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

object WesterraWafToken {
    private val WesterraWafTokenTag: String = WesterraWafToken::class.java.simpleName

    private const val AWS_WAF_TOKEN = "aws-waf-token"
    private const val COOKIE_KEY = "Cookie"
    private const val SET_COOKIE_KEY = "Set-Cookie"

    private lateinit var wafTokenProvider: WAFTokenProvider

    operator fun invoke(context: Context) {
        val buildFlavor = BuildFlavors.fromBuildConfig() ?: BuildFlavors.DEV
        val wafConfiguration = WAFConfiguration
            .builder()
            .applicationIntegrationURL(buildFlavor.integrationUrl())
            .domainName(buildFlavor.domainName())
            .setTokenCookie(true)
            .backgroundRefreshEnabled(true)
            .build()

        val provider = WAFTokenProvider(context, wafConfiguration)
        wafTokenProvider = provider
    }

    fun getWafTokenProvider() = wafTokenProvider

    fun refreshToken() {
        // Create an executor that executes tasks in a background thread.
        Executors.newSingleThreadScheduledExecutor().schedule(
            {
                refreshTokenHelper()
            },
            1,
            TimeUnit.SECONDS,
        )
    }

    private fun refreshTokenHelper() {
        WafReadySignal.isTokenAvailable = false
        wafTokenProvider.onTokenReady { wafToken, sdkError ->
            if (sdkError != null) {
                WafReadySignal.isTokenAvailable = false
                Log.e(
                    WesterraWafTokenTag,
                    "RefreshToken: onTokenReady: sdkError (${sdkError.value}): $sdkError",
                )
            } else if (wafToken.value.isEmpty()) {
                WafReadySignal.isTokenAvailable = false
                Log.e(
                    WesterraWafTokenTag,
                    "RefreshToken: onTokenReady: Unexpected empty waf token string",
                )
            } else {
                WafReadySignal.isTokenAvailable = true
                updateCookie(wafToken = wafToken)
            }
        }
    }

    private fun updateCookie(wafToken: WAFToken) {
        val buildFlavor = BuildFlavors.fromBuildConfig() ?: BuildFlavors.DEV
        val uri: URI
        try {
            uri = URI(buildFlavor.wafUri())
        } catch (e: URISyntaxException) {
            Log.e(
                WesterraWafTokenTag,
                "Unexpected URISyntaxException from domain name: ${buildFlavor.domainName()}",
            )
            return
        }

        if (cookieExists(wafToken = wafToken, uri = uri)) {
            Log.d(WesterraWafTokenTag, "Skipping waf token cookie update, cookie already exists.")
            return
        }

        makeWafCookie(wafToken = wafToken, uri = uri)
    }

    private fun cookieExists(wafToken: WAFToken, uri: URI): Boolean {
        val currentCookies = CookieHandler.getDefault()?.get(uri, mutableMapOf())
        currentCookies?.forEach { cookie ->
            if (cookie.key == COOKIE_KEY) {
                cookie.value.forEach { cookieString ->
                    if (cookieString.startsWith(AWS_WAF_TOKEN)) {
                        val tokenValue = cookieString.substring(cookieString.indexOf("=") + 1)
                        if (tokenValue == wafToken.value) {
                            return true
                        }
                    }
                }
            }
        }
        return false
    }

    private fun makeWafCookie(wafToken: WAFToken, uri: URI) {
        val cookies = listOf(AWS_WAF_TOKEN + "=" + wafToken.value)
        val responseHeaders = mapOf(SET_COOKIE_KEY to cookies)
        CookieHandler.getDefault()?.put(uri, responseHeaders)
    }
}
