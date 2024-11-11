package com.westerra.release.btp.network

import android.util.Log
import com.backbase.android.Backbase
import com.backbase.android.utils.net.NetworkConnectorBuilder
import com.backbase.android.utils.net.request.RequestMethods
import com.backbase.android.utils.net.response.Response
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.westerra.release.btp.model.BtpAccountsResponse
import com.westerra.release.extensions.backbase.executeAsSuspended
import org.koin.java.KoinJavaComponent

class BtpProfileClient(
    private val clientApiUrl: String,
) {
    private val moshi
        get() = KoinJavaComponent.getKoin().get<Moshi>()

    companion object {
        private val TAG = BtpProfileClient::class.java.simpleName
    }

    suspend fun fetchBtpAccounts(): BtpAccountsResponse {
        val test = getNetworkConnectionResponse()
        val response = test.stringResponse
            ?: return BtpAccountsResponse(errorMessage = "Unexpected response")
        return try {
            KotlinJsonAdapterFactory()
                .create(
                    BtpAccountsResponse::class.java,
                    moshi = moshi,
                    annotations = mutableSetOf(),
                ) ?.fromJson(response) as BtpAccountsResponse
        } catch (e: Exception) {
            Log.d(TAG, "Unexpected response: $response", e)
            BtpAccountsResponse(errorMessage = "Unexpected response")
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
