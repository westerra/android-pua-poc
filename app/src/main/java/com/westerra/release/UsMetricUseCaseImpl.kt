package com.westerra.release

import com.backbase.android.client.gen2.metricclient1.api.InteractionApi
import com.backbase.android.client.gen2.metricclient1.api.InteractionApiParams
import com.backbase.android.client.gen2.metricclient1.model.CaptureInteractionRequest
import com.backbase.android.retail.journey.accounts_and_transactions.banner.AccountsBannerMetricInteractionRequest
import com.backbase.android.retail.journey.accounts_and_transactions.banner.AccountsBannerMetricUseCase
import com.backbase.android.retail.journey.accounts_and_transactions.transactions.CallState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Uses the [interactionApi] to communicate with the Metrics backend.
 *
 * @property interactionApi The client that connects to the DBS backend.
 * @property dispatcher The CoroutineDispatcher that requests will run in.
 */
class UsMetricUseCaseImpl constructor(
    private val interactionApi: InteractionApi,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : AccountsBannerMetricUseCase {

    override suspend fun captureInteraction(request: AccountsBannerMetricInteractionRequest): CallState<out Unit> =
        withContext(dispatcher) {
            val response = interactionApi.captureInteraction(request.toCaptureInteractionApiParams()).execute()
            return@withContext if (response.isErrorResponse) {
                CallState.Error(response)
            } else {
                CallState.Success(Unit)
            }
        }

}

internal fun AccountsBannerMetricInteractionRequest.toCaptureInteractionApiParams() =
    InteractionApiParams.CaptureInteraction {
        captureInteractionRequest = CaptureInteractionRequest {
            engagement = this@toCaptureInteractionApiParams.engagementId
            creative = this@toCaptureInteractionApiParams.creativeId
            type = when(this@toCaptureInteractionApiParams.type) {
                AccountsBannerMetricInteractionRequest.Type.CLICKS -> CaptureInteractionRequest.Type.CLICKS
                else -> null
            }
            channel = when(this@toCaptureInteractionApiParams.channel) {
                AccountsBannerMetricInteractionRequest.Channel.BANNER -> CaptureInteractionRequest.Channel.BANNER
                else -> null
            }
        }
    }