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

class BtpAccountsClient() {
    private val moshi
        get() = KoinJavaComponent.getKoin().get<Moshi>()

    companion object {
        private val TAG = BtpAccountsClient::class.java.simpleName
        private const val ACCOUNTS_API =
            "/api/arrangement-manager/client-api/v2/productsummary"
    }

    suspend fun fetchBtpAccounts(): BtpAccountsResponse {
        val response = getNetworkConnectionResponse().stringResponse
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

        return NetworkConnectorBuilder(edgeDomain.plus(ACCOUNTS_API))
            .addRequestMethod(RequestMethods.GET)
            .buildConnection()
            .executeAsSuspended()
    }
}
