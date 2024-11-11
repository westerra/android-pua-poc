package com.westerra.release.payments.usecase

import android.util.Log
import com.backbase.android.Backbase
import com.backbase.android.client.paymentorderclient2.model.AccountIdentification
import com.backbase.android.client.paymentorderclient2.model.Currency
import com.backbase.android.client.paymentorderclient2.model.Identification
import com.backbase.android.client.paymentorderclient2.model.InitiateCounterpartyAccount
import com.backbase.android.client.paymentorderclient2.model.InitiateTransaction
import com.backbase.android.client.paymentorderclient2.model.InstructionPriority
import com.backbase.android.client.paymentorderclient2.model.InvolvedParty
import com.backbase.android.client.paymentorderclient2.model.InvolvedPartyRole
import com.backbase.android.client.paymentorderclient2.model.PaymentMode
import com.backbase.android.client.paymentorderclient2.model.PaymentOrdersPost
import com.backbase.android.client.paymentorderclient2.model.PaymentOrdersPostResponse
import com.backbase.android.client.paymentorderclient2.model.Schedule
import com.backbase.android.client.paymentorderclient2.model.SchemeNames
import com.backbase.android.client.paymentorderclient2.model.Status.ACCEPTED
import com.backbase.android.client.paymentorderclient2.model.Status.CANCELLATIONPENDING
import com.backbase.android.client.paymentorderclient2.model.Status.CANCELLED
import com.backbase.android.client.paymentorderclient2.model.Status.CONFIRMATIONDECLINED
import com.backbase.android.client.paymentorderclient2.model.Status.CONFIRMATIONPENDING
import com.backbase.android.client.paymentorderclient2.model.Status.DRAFT
import com.backbase.android.client.paymentorderclient2.model.Status.ENTERED
import com.backbase.android.client.paymentorderclient2.model.Status.PROCESSED
import com.backbase.android.client.paymentorderclient2.model.Status.READY
import com.backbase.android.client.paymentorderclient2.model.Status.REJECTED
import com.backbase.android.retail.journey.payments.PaymentUseCase
import com.backbase.android.retail.journey.payments.model.PaymentOrder
import com.backbase.android.retail.journey.payments.model.PaymentOrderResponse
import com.backbase.android.retail.journey.payments.model.PaymentSchedule
import com.backbase.android.retail.journey.payments.model.Status
import com.backbase.android.utils.net.NetworkConnectorBuilder
import com.backbase.android.utils.net.request.RequestMethods
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonSyntaxException
import com.westerra.release.extensions.backbase.cacheErrorMessage
import com.westerra.release.extensions.backbase.executeAsSuspended
import com.westerra.release.extensions.backbase.requestedExecutionDate
import com.westerra.release.gson.extension.createPaymentsGson
import java.time.LocalDate

abstract class CustomPaymentUseCaseBase : PaymentUseCase {

    abstract fun createInitiateCounterpartyAccount(
        paymentOrder: PaymentOrder,
    ): InitiateCounterpartyAccount

    companion object {
        private const val PAYMENT_ORDER_URL_ENDPOINT =
            "/api/payment-order-service/client-api/v2/payment-orders"
        private const val PAYMENT_ORDER_PROGRESS_STATUS_ENDPOINT = "/progress-status"
    }

    private val edgeDomain: String get() = Backbase.getInstance()!!.configuration
        .experienceConfiguration.serverURL

    override suspend fun createPaymentOrder(
        paymentOrder: PaymentOrder,
    ): PaymentUseCase.CreatePaymentOrderResult {
        val paymentOrdersConnection = NetworkConnectorBuilder(
            edgeDomain.plus(PAYMENT_ORDER_URL_ENDPOINT),
        )
            .addRequestMethod(RequestMethods.POST)
            .addHeaders(
                mutableMapOf(
                    "Content-Type" to "application/json",
                ),
            )
            .addBody(createPaymentOrdersRequestBodyString(paymentOrder))
            .buildConnection()

        val paymentOrdersResponse = paymentOrdersConnection.executeAsSuspended()

        if (paymentOrdersResponse.isErrorResponse) {
            // Note: This causes a toast message to be shown and the user stays on the transfer
            // summary screen. The user can try again. A generic message is shown in the toast.
            return PaymentUseCase.CreatePaymentOrderResult.ServerError(
                errorMessage = paymentOrdersResponse.errorMessage,
            )
        }

        val paymentOrderPostResponse = GsonBuilder().createPaymentsGson().fromJson(
            paymentOrdersResponse.stringResponse,
            PaymentOrdersPostResponse::class.java,
        )

        if (paymentOrderPostResponse.status == CONFIRMATIONPENDING) {
            // Note: This causes a toast message to be shown and the user stays on the transfer
            // summary screen. The user can try again. A generic message is shown in the toast.
            return PaymentUseCase.CreatePaymentOrderResult.ConfirmationPending(
                paymentOrderId = paymentOrderPostResponse.id,
            )
        }

        paymentOrderPostResponse.cacheErrorMessage()

        // Note: If status is rejected, user goes to the results screen and a generic error message
        // is shown. The user can navigate back to the transfer summary screen and try again.
        // The message is defined in InternalA2AConfig createCompleteScreenConfig
        return PaymentUseCase.CreatePaymentOrderResult.Success(
            PaymentOrderResponse(
                id = paymentOrderPostResponse.id,
                payload = paymentOrderPostResponse,
                status = convertPaymentStatus(value = paymentOrderPostResponse.status),
                additions = null,
                isAccepted = true,
            ),
        )
    }

    override suspend fun getConfirmedPaymentOrder(
        paymentOrderId: String,
    ): PaymentUseCase.ConfirmPaymentOrderResult {
        val confirmedPaymentOrderConnection = NetworkConnectorBuilder(
            "$edgeDomain/$paymentOrderId/$PAYMENT_ORDER_PROGRESS_STATUS_ENDPOINT",
        )
            .addRequestMethod(RequestMethods.GET)
            .addHeaders(
                mutableMapOf(
                    "Content-Type" to "application/json",
                ),
            )
            .buildConnection()

        val confirmedPaymentOrderResponse = confirmedPaymentOrderConnection.executeAsSuspended()

        if (confirmedPaymentOrderResponse.isErrorResponse) {
            return PaymentUseCase.ConfirmPaymentOrderResult.ServerError(
                errorMessage = confirmedPaymentOrderResponse.errorMessage,
            )
        }

        val paymentOrderResponse = parsePaymentOrderResponse(
            response = confirmedPaymentOrderResponse.stringResponse,
        ) ?: return PaymentUseCase.ConfirmPaymentOrderResult.ServerError(
            errorMessage = confirmedPaymentOrderResponse.errorMessage,
        )

        if (paymentOrderResponse.status == Status.ConfirmationPending) {
            return PaymentUseCase.ConfirmPaymentOrderResult.ConfirmationPending(
                paymentOrderId = paymentOrderId,
            )
        }

        return PaymentUseCase.ConfirmPaymentOrderResult.Confirmed(paymentOrderResponse)
    }

    private fun parsePaymentOrderResponse(response: String?): PaymentOrderResponse? {
        response ?: return null
        try {
            return Gson().fromJson(
                response,
                PaymentOrderResponse::class.java,
            )
        } catch (e: JsonSyntaxException) {
            Log.e(
                "CustomPaymentUseCase",
                "Unexpected Error parsing PaymentOrderResponse",
                e,
            )
            return null
        }
    }

    private fun createPaymentOrdersRequestBodyString(paymentOrder: PaymentOrder): String {
        return GsonBuilder().createPaymentsGson().toJson(
            createPaymentOrdersRequestBody(paymentOrder),
        )
    }

    private fun createPaymentOrdersRequestBody(paymentOrder: PaymentOrder): PaymentOrdersPost {
        return PaymentOrdersPost {
            paymentType = paymentOrder.paymentType
            paymentMode = if (paymentOrder.paymentSchedule is PaymentSchedule.Recurring) {
                PaymentMode.RECURRING
            } else {
                PaymentMode.SINGLE
            }
            schedule = convertSchedule(paymentSchedule = paymentOrder.paymentSchedule)
            requestedExecutionDate = paymentOrder.requestedExecutionDate()
            originatorAccount = convertFromAccount(paymentOrder = paymentOrder)
            instructionPriority = InstructionPriority.NORM
            transferTransactionInformation = InitiateTransaction {
                counterparty = convertToParty(paymentOrder = paymentOrder)
                counterpartyAccount = createInitiateCounterpartyAccount(paymentOrder = paymentOrder)
                instructedAmount = convertCurrency(paymentOrder = paymentOrder)
            }
        }
    }

    private fun convertPaymentStatus(
        value: com.backbase.android.client.paymentorderclient2.model.Status,
    ): Status {
        return when (value) {
            DRAFT -> Status.Draft
            ENTERED -> Status.Entered
            READY -> Status.Ready
            ACCEPTED -> Status.Accepted
            PROCESSED -> Status.Processed
            REJECTED -> Status.Rejected
            CANCELLED -> Status.Cancelled
            CANCELLATIONPENDING -> Status.CancellationPending
            CONFIRMATIONPENDING -> Status.ConfirmationPending
            CONFIRMATIONDECLINED -> Status.ConfirmationDeclined
            else -> Status.Rejected
        }
    }

    private fun convertSchedule(paymentSchedule: PaymentSchedule): Schedule? {
        if (paymentSchedule is PaymentSchedule.Recurring) {
            return Schedule {
                every = Schedule.Every._1
                on = convertOn(paymentSchedule = paymentSchedule)
                repeat = convertRepeat(paymentSchedule = paymentSchedule)
                startDate = paymentSchedule.startDate
                endDate = convertEndDate(paymentSchedule = paymentSchedule)
                transferFrequency = convertTransferFrequency(
                    frequency = paymentSchedule.transferFrequency,
                )
            }
        }
        return null
    }

    // https://backbase.io/developers/apis/specs/payment/payment-order-client-api/3.4.0/models/Schedule/
    private fun convertOn(paymentSchedule: PaymentSchedule.Recurring): Int {
        // Day of week value is 1-7 on web
        val convertedDayOfWeek = ((paymentSchedule.startDate.dayOfWeek.value % 7) + 1)
        return when (paymentSchedule.transferFrequency) {
            PaymentSchedule.Recurring.TransferFrequency.Daily ->
                1 // UI doesn't support choosing this value.
            PaymentSchedule.Recurring.TransferFrequency.Weekly ->
                convertedDayOfWeek // Docs say 1-7 value
            PaymentSchedule.Recurring.TransferFrequency.Biweekly ->
                convertedDayOfWeek // Docs say 1-14 value but web does day of week.
            PaymentSchedule.Recurring.TransferFrequency.Monthly ->
                paymentSchedule.startDate.dayOfMonth
            PaymentSchedule.Recurring.TransferFrequency.Quarterly ->
                convertedDayOfWeek // Not documented, web does day of week.
            PaymentSchedule.Recurring.TransferFrequency.Yearly ->
                paymentSchedule.startDate.monthValue
        }
    }

    private fun convertRepeat(paymentSchedule: PaymentSchedule.Recurring): Int? {
        val end = paymentSchedule.end
        return if (end is PaymentSchedule.Recurring.Ending.After) end.occurrences else null
    }

    private fun convertEndDate(paymentSchedule: PaymentSchedule.Recurring): LocalDate? {
        val end = paymentSchedule.end
        return if (end is PaymentSchedule.Recurring.Ending.On) end.endDate else null
    }

    private fun convertTransferFrequency(
        frequency: PaymentSchedule.Recurring.TransferFrequency,
    ): Schedule.TransferFrequency {
        return when (frequency) {
            PaymentSchedule.Recurring.TransferFrequency.Daily ->
                Schedule.TransferFrequency.DAILY
            PaymentSchedule.Recurring.TransferFrequency.Weekly ->
                Schedule.TransferFrequency.WEEKLY
            PaymentSchedule.Recurring.TransferFrequency.Biweekly ->
                Schedule.TransferFrequency.BIWEEKLY
            PaymentSchedule.Recurring.TransferFrequency.Monthly ->
                Schedule.TransferFrequency.MONTHLY
            PaymentSchedule.Recurring.TransferFrequency.Quarterly ->
                Schedule.TransferFrequency.QUARTERLY
            PaymentSchedule.Recurring.TransferFrequency.Yearly ->
                Schedule.TransferFrequency.YEARLY
        }
    }

    private fun convertFromAccount(paymentOrder: PaymentOrder): AccountIdentification {
        return AccountIdentification {
            identification = Identification {
                identification = paymentOrder.fromParty.id
                schemeName = SchemeNames.ID
            }
            name = paymentOrder.fromParty.name
        }
    }

    private fun convertCurrency(paymentOrder: PaymentOrder): Currency {
        return Currency {
            amount = "${paymentOrder.amount.value}"
            currencyCode = paymentOrder.amount.currencyCode
        }
    }

    private fun convertToParty(paymentOrder: PaymentOrder): InvolvedParty {
        return InvolvedParty {
            name = paymentOrder.toParty.name
            role = InvolvedPartyRole.CREDITOR
        }
    }
}
