package com.westerra.release.sso.network

import com.backbase.android.utils.net.NetworkConnectorBuilder
import com.backbase.android.utils.net.request.RequestMethods
import com.backbase.android.utils.net.response.Response
import com.westerra.release.BuildConfig
import com.westerra.release.constants.Constants
import com.westerra.release.extensions.backbase.executeAsSuspended
import com.westerra.release.sso.model.DMIResponse

class MTGSVCClient {

    companion object {
        fun getDMIDomain(): String {
            return if (BuildConfig.FLAVOR == Constants.BUILD_FLAVOR_PROD) {
                Constants.DMI_PROD_DOMAIN
            } else {
                Constants.DMI_TEST_DOMAIN
            }
        }
    }

    suspend fun postLogin(ssoToken: String, isPayments: Boolean): DMIResponse {
        val response = postLoginHelper(ssoToken = ssoToken, isPayments = isPayments)
        return DMIResponse(
            location = response.headers?.get("Location")?.first(),
            errorMessage = response.errorMessage,
            error = null,
            status = response.responseCode,
        )
    }

    private suspend fun postLoginHelper(ssoToken: String, isPayments: Boolean): Response {
        val params = mutableMapOf<String, String>()
        if (isPayments) {
            params["DisplayPage"] = "OneTimeScheduledPayment"
        }
        params["KEY"] = ssoToken
        return NetworkConnectorBuilder(getDMIDomain() + "SSO/X61Login.aspx")
            .addRequestMethod(RequestMethods.POST)
            .addParameters(params)
            .buildConnection()
            .executeAsSuspended()
    }
}
