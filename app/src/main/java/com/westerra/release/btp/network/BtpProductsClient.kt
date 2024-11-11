package com.westerra.release.btp.network

import android.util.Log
import com.backbase.android.Backbase
import com.backbase.android.utils.net.NetworkConnectorBuilder
import com.backbase.android.utils.net.request.RequestMethods
import com.backbase.android.utils.net.response.Response
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.westerra.release.btp.model.AddProductResponse
import com.westerra.release.btp.model.BtpAccountsResponse
import com.westerra.release.btp.model.BtpProductItem
import com.westerra.release.btp.model.BtpProductsResponse
import com.westerra.release.btp.model.ProfileResponse
import com.westerra.release.btp.model.WesterraProduct
import com.westerra.release.constants.Constants
import com.westerra.release.custom.MySharedPreferences
import com.westerra.release.extensions.backbase.executeAsSuspended
import com.westerra.release.firebase.extensions.westerraProductList
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import org.json.JSONObject
import org.koin.java.KoinJavaComponent

class BtpProductsClient {
    private val moshi
        get() = KoinJavaComponent.getKoin().get<Moshi>()

    companion object {
        private val TAG = BtpProductsClient::class.java.simpleName
        private const val CREATE_PRODUCTS_API =
            "/api/payment-order-service/client-api/v2/payment-orders"
        private const val ACCOUNTS_API =
            "/api/arrangement-manager/client-api/v2/productsummary"
        private const val PROFILE_API_URL =
            "/api/user-manager/client-api/v2/users/me/profile"
    }

    suspend fun fetchProducts(): BtpProductsResponse {
        val profileResponseString = getProfileNetworkConnectionResponse().stringResponse
            ?: return BtpProductsResponse(errorMessage = "Unexpected response")
        val profileResponse = try {
            KotlinJsonAdapterFactory()
                .create(
                    ProfileResponse::class.java,
                    moshi = moshi,
                    annotations = mutableSetOf(),
                ) ?.fromJson(profileResponseString) as ProfileResponse
        } catch (e: Exception) {
            Log.d(TAG, "Unexpected response: $profileResponseString", e)
            return BtpProductsResponse(errorMessage = "Unexpected response")
        }

        val memberNumber = profileResponse.additions?.customerCode
        if (memberNumber.isNullOrBlank()) {
            Log.d(TAG, "Unexpected missing member number: $profileResponseString")
            return BtpProductsResponse(errorMessage = "Unexpected response")
        } else {
            MySharedPreferences.storeMemberNumber(memberNumber = memberNumber)
        }

        val response = getNetworkConnectionResponse().stringResponse
            ?: return BtpProductsResponse(errorMessage = "Unexpected response")
        val accountsResponse = try {
            KotlinJsonAdapterFactory()
                .create(
                    BtpAccountsResponse::class.java,
                    moshi = moshi,
                    annotations = mutableSetOf(),
                ) ?.fromJson(response) as BtpAccountsResponse
        } catch (e: Exception) {
            Log.d(TAG, "Unexpected response: $response", e)
            return BtpProductsResponse(errorMessage = "Unexpected response")
        }

        return BtpProductsResponse(
            products = FirebaseRemoteConfig.getInstance().westerraProductList().map { product ->
                product.currentAccountCount = accountsResponse.countAccountsForProduct(product.type)
                return@map product
            },
            profile = profileResponse,
            errorMessage = null,
            error = null,
        )
    }

    suspend fun postAddProduct(
        product: WesterraProduct,
        fromAccount: BtpProductItem,
        transferAmount: Number,
        transferNotes: String?,
    ): AddProductResponse {
        val body = makeAddProductBody(
            product = product,
            fromAccount = fromAccount,
            transferAmount = transferAmount,
            transferNotes = transferNotes,
        )
        val response = postNetworkConnectionResponse(body.toString()).stringResponse
            ?: return AddProductResponse(errorMessage = "Unexpected response")
        return try {
            KotlinJsonAdapterFactory()
                .create(
                    AddProductResponse::class.java,
                    moshi = moshi,
                    annotations = mutableSetOf(),
                ) ?.fromJson(response) as AddProductResponse
        } catch (e: Exception) {
            Log.d(TAG, "Unexpected response: $response", e)
            AddProductResponse(errorMessage = "Unexpected response")
        }
    }

    private suspend fun getProfileNetworkConnectionResponse(): Response {
        val edgeDomain: String =
            Backbase.getInstance()!!.configuration.experienceConfiguration.serverURL
        return NetworkConnectorBuilder(edgeDomain.plus(PROFILE_API_URL))
            .addRequestMethod(RequestMethods.GET)
            .buildConnection()
            .executeAsSuspended()
    }

    private fun makeAddProductBody(
        product: WesterraProduct,
        fromAccount: BtpProductItem,
        transferAmount: Number,
        transferNotes: String?,
    ): JSONObject {
        val requestedExecutionDate = SimpleDateFormat(
            Constants.SIMPLE_YMD_PATTERN,
            Locale.getDefault(),
        ).format(Date())
        val result = JSONObject(
            mapOf(
                "paymentType" to "INTERNAL_TRANSFER",
                "originatorAccount" to mapOf(
                    "identification" to mapOf(
                        "identification" to fromAccount.id,
                        "schemeName" to "ID",
                    ),
                    "name" to fromAccount.name,
                ),
                "requestedExecutionDate" to requestedExecutionDate,
                "paymentMode" to "SINGLE",
                "instructionPriority" to "NORM",
                "transferTransactionInformation" to mapOf(
                    "instructedAmount" to mapOf(
                        "amount" to transferAmount.toString(),
                        "currencyCode" to "USD",
                    ),
                    "counterparty" to mapOf(
                        "name" to product.type,
                        "role" to "CREDITOR",
                    ),
                    "purposeOfPayment" to mapOf(
                        "code" to product.typeId,
                        "freeText" to "NewAccount",
                    ),
                    "counterpartyAccount" to mapOf(
                        "identification" to mapOf(
                            "identification" to "6e980ddb-6a80-4907-8d24-2a94603302d8",
                            "schemeName" to "ID",
                        ),
                    ),
                ),
            ),
        )
        transferNotes ?. let {
            if (it.isNotBlank()) {
                result.put("remittanceInformation", it)
            }
        }
        return result
    }

    private suspend fun getNetworkConnectionResponse(): Response {
        val edgeDomain: String =
            Backbase.getInstance()!!.configuration.experienceConfiguration.serverURL
        return NetworkConnectorBuilder(edgeDomain.plus(ACCOUNTS_API))
            .addRequestMethod(RequestMethods.GET)
            .buildConnection()
            .executeAsSuspended()
    }

    private suspend fun postNetworkConnectionResponse(body: String): Response {
        val edgeDomain: String =
            Backbase.getInstance()!!.configuration.experienceConfiguration.serverURL
        return NetworkConnectorBuilder(edgeDomain.plus(CREATE_PRODUCTS_API))
            .addRequestMethod(RequestMethods.POST)
            .addHeaders(mapOf(Pair("Content-Type", "application/json")))
            .addBody(body)
            .buildConnection()
            .executeAsSuspended()
    }
}
