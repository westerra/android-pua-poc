package com.westerra.release.sso.network

import android.util.Log
import com.backbase.android.Backbase
import com.backbase.android.utils.net.NetworkConnectorBuilder
import com.backbase.android.utils.net.request.RequestMethods
import com.backbase.android.utils.net.response.Response
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.westerra.release.extensions.backbase.executeAsSuspended
import com.westerra.release.sso.model.SSOApplicationResponse
import org.koin.java.KoinJavaComponent.getKoin

class SSOClient(
    private val clientApiUrl: String,
) {

    private val moshi
        get() = getKoin().get<Moshi>()

    companion object {
        private val TAG = SSOClient::class.java.simpleName
    }

    suspend fun fetchSSOToken(): SSOApplicationResponse {
        val response = getNetworkConnectionResponse().stringResponse
            ?: return SSOApplicationResponse(errorMessage = "SSO Login attempt failed")
        return try {
            KotlinJsonAdapterFactory()
                .create(
                    SSOApplicationResponse::class.java,
                    moshi = moshi,
                    annotations = mutableSetOf(),
                ) ?.fromJson(response) as SSOApplicationResponse
        } catch (e: Exception) {
            Log.d(TAG, "Unexpected response: $response", e)
            SSOApplicationResponse(errorMessage = "Unexpected response")
        }
    }

    private suspend fun getNetworkConnectionResponse(): Response {
        val edgeDomain: String =
            Backbase.getInstance()!!.configuration.experienceConfiguration.serverURL

        return NetworkConnectorBuilder(edgeDomain.plus(clientApiUrl))
            .addRequestMethod(RequestMethods.GET)
            .buildConnection()
            .executeAsSuspended()
    }
}
