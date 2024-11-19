package com.westerra.release.payments.usecase

import android.util.Log
import androidx.core.net.toUri
import com.backbase.android.Backbase
import com.backbase.android.client.gen2.paymentorderv2client2.api.PaymentOrdersApi
import com.backbase.android.client.paymentorderclient2.model.IdentifiedPaymentOrder
import com.backbase.android.client.paymentorderclient2.model.IdentifiedPaymentOrderJsonAdapter
import com.backbase.android.client.paymentorderclient2.model.PaymentMode
import com.backbase.android.client.paymentorderclient2.model.Schedule
import com.backbase.android.client.paymentorderclient2.model.Status
import com.backbase.android.retail.journey.payments.upcoming.UpcomingPaymentsUseCase
import com.backbase.android.retail.journey.payments.upcoming.datasource.Amount
import com.backbase.android.retail.journey.payments.upcoming.datasource.PaymentOrderStatus
import com.backbase.android.retail.journey.payments.upcoming.datasource.PaymentOrderViewData
import com.backbase.android.retail.journey.payments.upcoming.datasource.ScheduleViewData
import com.backbase.android.retail.journey.payments.upcoming.gen2_paymentorder_v2_client_2.PaymentOrderV2Client2UpcomingPaymentsServiceUseCase
import com.backbase.android.retail.journey.payments.upcoming.model.Cancelled
import com.backbase.android.retail.journey.payments.upcoming.model.Expired
import com.backbase.android.retail.journey.payments.upcoming.model.Failed
import com.backbase.android.retail.journey.payments.upcoming.model.InProcess
import com.backbase.android.retail.journey.payments.upcoming.model.P2PTransferData
import com.backbase.android.retail.journey.payments.upcoming.model.Pending
import com.backbase.android.retail.journey.payments.upcoming.model.Sent
import com.backbase.android.retail.journey.payments.upcoming.model.Unknown
import com.backbase.android.utils.net.NetworkConnectorBuilder
import com.backbase.android.utils.net.request.RequestMethods
import com.google.gson.JsonParser
import com.squareup.moshi.Moshi
import com.westerra.release.extensions.backbase.executeAsSuspended
import com.westerra.release.extensions.toDate
import java.math.BigDecimal
import java.time.ZoneId
import java.util.Date
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CustomUpcomingPaymentsUseCase(
    private val moshi: Moshi,
    private val paymentOrdersApi: PaymentOrdersApi,
) : UpcomingPaymentsUseCase {

    private val edgeDomain: String get() =
        Backbase.getInstance()!!.configuration.experienceConfiguration.serverURL

    companion object {
        private val TAG = CustomUpcomingPaymentsUseCase::class.java.simpleName

        private const val PAYMENT_ORDER_SERVICE_URL_ENDPOINT =
            "/api/payment-order-service/client-api/v2/payment-orders"

        private const val STATUS_QUERY_PARAM = "status"
        private const val PAYMENT_TYPES_QUERY_PARAM = "paymentTypes"

        // private const val EXECUTION_DATE_FROM_QUERY_PARAM = "executionDateFrom"
        // use this when required
        private const val FROM_QUERY_PARAM = "from"
        private const val SIZE_QUERY_PARAM = "size"
        private const val ORDER_BY_QUERY_PARAM = "orderBy"
        private const val EXPECTED_EXECUTION_DATE_FROM_QUERY_PARAM = "expectedExecutionDateFrom"
        private const val REQUESTED_EXECUTION_DATE_QUERY_PARAM_VALUE = "requestedExecutionDate"
        private const val DIRECTION_QUERY_PARAM = "direction"
        private const val DIRECTION_QUERY_PARAM_VALUE = "DESC"

        private const val UPCOMING_PAYMENT_STATUS_ACCEPTED = "ACCEPTED"
        private const val P2P_TRANSFER_PAYMENT_TYPE = "P2P_TRANSFER"
        private const val REASON_CODE_PP01 = "PP01"
        private const val REASON_CODE_PP02 = "PP02"
        internal const val REASON_CODE_PP14 = "PP14"

    }

    private val paymentOrderClient2UpcomingPaymentsServiceUseCase
        get() = PaymentOrderV2Client2UpcomingPaymentsServiceUseCase(paymentOrdersApi)

    override suspend fun getPayments(
        vararg requestParams: UpcomingPaymentsUseCase.RequestParams,
    ): UpcomingPaymentsUseCase.RetrieveResult {
        val paymentOrderServiceUri = edgeDomain.plus(
            PAYMENT_ORDER_SERVICE_URL_ENDPOINT,
        ).toUri().buildUpon()
        paymentOrderServiceUri.appendQueryParameter(
            ORDER_BY_QUERY_PARAM,
            REQUESTED_EXECUTION_DATE_QUERY_PARAM_VALUE,
        )

        requestParams.forEach { param ->
            when (param) {
                is UpcomingPaymentsUseCase.RequestParams.FilterByStatus -> {
                    if (param.statuses.contains("CANCELLED") &&
                        !param.statuses.contains("CANCELLATION_PENDING")
                    ) {
                        paymentOrderServiceUri.appendQueryParameter(
                            STATUS_QUERY_PARAM,
                            "CANCELLATION_PENDING",
                        )
                    }
                    param.statuses.forEach { status ->
                        paymentOrderServiceUri.appendQueryParameter(STATUS_QUERY_PARAM, status)
                    }
                }
                is UpcomingPaymentsUseCase.RequestParams.PageFrom -> {
                    paymentOrderServiceUri.appendQueryParameter(FROM_QUERY_PARAM, "${param.index}")
                }
                is UpcomingPaymentsUseCase.RequestParams.PageSize -> {
                    paymentOrderServiceUri.appendQueryParameter(SIZE_QUERY_PARAM, "${param.size}")
                }
                is UpcomingPaymentsUseCase.RequestParams.Direction -> {
                    paymentOrderServiceUri.appendQueryParameter(
                        DIRECTION_QUERY_PARAM,
                        DIRECTION_QUERY_PARAM_VALUE,
                    )
                }
                is UpcomingPaymentsUseCase.RequestParams.PaymentTypes -> {
                    var paymentTypes = ""
                    param.paymentTypes.forEachIndexed { index, paymentType ->
                        paymentTypes += paymentType
                        if (index < param.paymentTypes.size - 1) {
                            paymentTypes += ","
                        }
                    }
                    if (paymentTypes.isNotEmpty()) {
                        paymentOrderServiceUri.appendQueryParameter(
                            PAYMENT_TYPES_QUERY_PARAM,
                            paymentTypes,
                        )
                    }
                }
                /*
                is UpcomingPaymentsUseCase.RequestParams.ExecutionDateFrom -> {
                    val dateFromString = param.date.toYMDString()
                    paymentOrderServiceUri.appendQueryParameter(
                    EXPECTED_EXECUTION_DATE_FROM_QUERY_PARAM,
                    dateFromString)
                }
                 */
                else -> Unit
            }
        }

        val paymentOrderServiceConnection = NetworkConnectorBuilder(
            paymentOrderServiceUri.build().toString(),
        )
            .addRequestMethod(RequestMethods.GET)
            .buildConnection()

        val paymentOrderServiceResponse = paymentOrderServiceConnection.executeAsSuspended()

        if (paymentOrderServiceResponse.isErrorResponse) {
            return UpcomingPaymentsUseCase.RetrieveResult.ServerError
        }

        var identifiedPaymentOrderList: List<IdentifiedPaymentOrder>
        withContext(Dispatchers.IO) {
            val jsonArray = JsonParser.parseString(
                paymentOrderServiceResponse.stringResponse,
            ).asJsonArray
            val jsonAdapter = IdentifiedPaymentOrderJsonAdapter(moshi)
            val results = mutableListOf<IdentifiedPaymentOrder>()
            jsonArray.forEach { jsonElement ->
                try {
                    jsonAdapter.fromJson(
                        jsonElement.asJsonObject.toString(),
                    ) ?. let {
                        results.add(it)
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "Unexpected Json conversion error.", e)
                }
            }
            identifiedPaymentOrderList = results
        }

        return UpcomingPaymentsUseCase.RetrieveResult.Success(
            identifiedPaymentOrderList.map { identifiedPaymentOrder ->
                PaymentOrderViewData(
                    id = identifiedPaymentOrder.id,
                    fromAccountName = identifiedPaymentOrder.originator?.name ?: "",
                    iconText = identifiedPaymentOrder.transferTransactionInformation?.counterparty
                        ?.name ?: "",
                    requestedExecutionDate = Date.from(
                        identifiedPaymentOrder.requestedExecutionDate?.atStartOfDay()?.atZone(
                            ZoneId.systemDefault(),
                        )?.toInstant(),
                    ),
                    amount = identifiedPaymentOrder.transferTransactionInformation
                        ?.instructedAmount?.let {
                            Amount(
                                BigDecimal(it.amount),
                                it.currencyCode,
                            )
                        }!!,
                    recipientAccountName = identifiedPaymentOrder.transferTransactionInformation
                        ?.counterparty?.name ?: "",
                    recipientAccountNumber = identifiedPaymentOrder.transferTransactionInformation
                        ?.counterpartyAccount?.identification?.identification,
                    mode = getPaymentMode(
                        identifiedPaymentOrder.paymentMode,
                        identifiedPaymentOrder.schedule,
                    ),
                    schedule = getScheduleViewData(identifiedPaymentOrder.schedule),
                    description = identifiedPaymentOrder.transferTransactionInformation
                        ?.remittanceInformation?.content,
                    isAccepted = identifiedPaymentOrder.status == Status.ACCEPTED,
                    version = identifiedPaymentOrder.version,
                    status = getPaymentOrderStatus(identifiedPaymentOrder.status),
                    p2pTransferData = mapData(identifiedPaymentOrder),
                )
            },
        )
    }

    override suspend fun cancelUpcomingPayment(
        paymentOrderViewData: PaymentOrderViewData,
    ): UpcomingPaymentsUseCase.CancelResult {
        return paymentOrderClient2UpcomingPaymentsServiceUseCase.cancelUpcomingPayment(
            paymentOrderViewData,
        )
    }

    private fun getPaymentMode(
        paymentMode: PaymentMode?,
        schedule: Schedule?,
    ): com.backbase.android.retail.journey.payments.upcoming.datasource.PaymentMode? {
        return paymentMode?.let {
            if (paymentMode != PaymentMode.SINGLE &&
                schedule?.transferFrequency != Schedule.TransferFrequency.ONCE
            ) {
                com.backbase.android.retail.journey.payments.upcoming.datasource.PaymentMode
                    .RECURRING
            } else {
                com.backbase.android.retail.journey.payments.upcoming.datasource.PaymentMode
                    .SINGLE
            }
        }
    }
    private fun getScheduleViewData(schedule: Schedule?): ScheduleViewData? {
        return schedule?.let {
            ScheduleViewData(
                startDate = schedule.startDate.toDate(),
                endDate = schedule.endDate?.toDate(),
                nextExecutionDate = schedule.nextExecutionDate?.toDate(),
                frequency = schedule.transferFrequency.name.lowercase(),
                numberOfPayments = schedule.repeat,
            )
        }
    }

    private fun getPaymentOrderStatus(status: Status): PaymentOrderStatus? {
        return when (status) {
            Status.ACCEPTED -> PaymentOrderStatus.ACCEPTED
            Status.CANCELLED -> PaymentOrderStatus.CANCELLED
            Status.CANCELLATIONPENDING -> PaymentOrderStatus.CANCELLATION_PENDING
            Status.REJECTED -> PaymentOrderStatus.REJECTED
            Status.PROCESSED -> PaymentOrderStatus.PROCESSED
            Status.DRAFT -> PaymentOrderStatus.DRAFT
            Status.ENTERED -> PaymentOrderStatus.ENTERED
            Status.CONFIRMATIONDECLINED -> PaymentOrderStatus.CONFIRMATION_DECLINED
            Status.CONFIRMATIONPENDING -> PaymentOrderStatus.CONFIRMATION_PENDING
            Status.READY -> PaymentOrderStatus.READY
            else -> null
        }
    }

    private fun mapData(identifiedPaymentOrder: IdentifiedPaymentOrder): P2PTransferData? {
        return if (!identifiedPaymentOrder.paymentType.isNullOrBlank() &&
            identifiedPaymentOrder.paymentType == P2P_TRANSFER_PAYMENT_TYPE
        ) {
            P2PTransferData
                .Builder().apply {
                    confirmationNumber = identifiedPaymentOrder.paymentSetupId
                    paymentStatus = when {
                        identifiedPaymentOrder.status == Status.ACCEPTED && identifiedPaymentOrder.reasonCode == REASON_CODE_PP01 -> Pending
                        identifiedPaymentOrder.status == Status.ACCEPTED && identifiedPaymentOrder.reasonCode == REASON_CODE_PP02 -> InProcess
                        identifiedPaymentOrder.status == Status.REJECTED && identifiedPaymentOrder.reasonCode == REASON_CODE_PP14 -> Expired
                        identifiedPaymentOrder.status == Status.REJECTED -> Failed
                        identifiedPaymentOrder.status == Status.PROCESSED -> Sent
                        identifiedPaymentOrder.status == Status.CANCELLED -> Cancelled
                        else -> Unknown
                    }
//                    isPending = (
//                        identifiedPaymentOrder.status == Status.ACCEPTED &&
//                            identifiedPaymentOrder.reasonCode == REASON_CODE_PP01
//                        )
//                    isInProcess = (
//                        identifiedPaymentOrder.status == Status.ACCEPTED &&
//                            identifiedPaymentOrder.reasonCode == REASON_CODE_PP02
//                        )
                }
                .build()
        } else {
            null
        }
    }
}
