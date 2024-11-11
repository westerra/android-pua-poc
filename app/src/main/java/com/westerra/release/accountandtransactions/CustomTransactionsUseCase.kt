package com.westerra.release.accountandtransactions

import androidx.core.net.toUri
import com.backbase.android.client.gen2.transactionsclient2.api.TransactionClientApi
import com.backbase.android.client.transactionclient2.model.CheckImageAvailability
import com.backbase.android.client.transactionclient2.model.CreditDebitIndicator
import com.backbase.android.client.transactionclient2.model.TransactionItemJsonAdapter
import com.backbase.android.retail.journey.accounts_and_transactions.Amount
import com.backbase.android.retail.journey.accounts_and_transactions.gen_transaction_client_2.TransactionsUseCaseImpl
import com.backbase.android.retail.journey.accounts_and_transactions.transactions.CallState
import com.backbase.android.retail.journey.accounts_and_transactions.transactions.State
import com.backbase.android.retail.journey.accounts_and_transactions.transactions.TransactionGetRequestParameters
import com.backbase.android.retail.journey.accounts_and_transactions.transactions.TransactionsUseCase
import com.backbase.android.retail.journey.accounts_and_transactions.transactions.models.BillingStatus
import com.backbase.android.retail.journey.accounts_and_transactions.transactions.models.CheckAvailability
import com.backbase.android.retail.journey.accounts_and_transactions.transactions.models.CreditDebitIndicatorItem
import com.backbase.android.retail.journey.accounts_and_transactions.transactions.models.TransactionCheckImage
import com.backbase.android.retail.journey.accounts_and_transactions.transactions.models.TransactionItem
import com.backbase.android.utils.net.NetworkConnectorBuilder
import com.backbase.android.utils.net.request.RequestMethods
import com.google.gson.JsonParser
import com.squareup.moshi.Moshi
import com.westerra.release.extensions.appendToBaseUrl
import com.westerra.release.extensions.backbase.executeAsSuspended
import com.westerra.release.extensions.backbase.toGeoCoordinates
import java.util.Currency
import kotlin.reflect.full.memberProperties
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CustomTransactionsUseCase(
    private val moshi: Moshi,
    private val transactionClientApi: TransactionClientApi,
) : TransactionsUseCase {
    companion object {
        const val TRANSACTION_MANAGER_URL_ENDPOINT =
            "/api/transaction-manager/client-api/v2/transactions"
        const val TRANSACTION_MANAGER_QUERY_PARAMS = "&direction=DESC"

        private const val STRING_NULL = "null"
        private const val KEY_CREDIT_DEBIT_INDICATOR = "creditDebitIndicator"
        private const val ORDER_BY_BOOKING_DATE = "bookingDate"
        private const val ORDER_BY_EXTERNAL_ID = "externalId"
        private const val KEY_FROM = "from"
        private const val STRING_EMPTY = ""

        private var lastFrom = 0 // separately keep track of "from" counter

        const val KEY_ORIGINAL_DESCRIPTION = "originalDescription"
    }

    private val transactionsUseCaseImpl get() = TransactionsUseCaseImpl(transactionClientApi)

    override suspend fun getTransactions(
        params: TransactionGetRequestParameters,
    ): CallState<out List<TransactionItem>> {
        val transactionManagerUri =
            TRANSACTION_MANAGER_URL_ENDPOINT.appendToBaseUrl().toUri().buildUpon()

        // Get all the memberProperties of "params" variable's class, loop through each variable,
        // get its name and value, append to Uri
        params::class.memberProperties.forEach {
            val propertyValue = it.getter.call(params).toString()

            if (propertyValue != STRING_NULL) {
                when (it.name) {
                    KEY_CREDIT_DEBIT_INDICATOR -> {
                        when (CreditDebitIndicatorItem.valueOf(propertyValue.uppercase())) {
                            CreditDebitIndicatorItem.CREDIT ->
                                transactionManagerUri.appendQueryParameter(
                                    it.name,
                                    CreditDebitIndicator.CRDT.value,
                                )
                            CreditDebitIndicatorItem.DEBIT ->
                                transactionManagerUri.appendQueryParameter(
                                    it.name,
                                    CreditDebitIndicator.DBIT.value,
                                )
                        }
                    }

                    KEY_FROM -> {
                        // WORKAROUND :
                        // check if "from" value is duplicating, if it is duplicating
                        // assign "lastFrom" else assign its original value
                        lastFrom =
                            if (propertyValue.toInt() == lastFrom &&
                                propertyValue.toInt() != 0
                            ) {
                                lastFrom + 1
                            } else {
                                propertyValue.toInt()
                            }
                        transactionManagerUri.appendQueryParameter(it.name, lastFrom.toString())
                    }

                    else -> {
                        // change orderBy parameter from bookingDate to externalId
                        if (propertyValue == ORDER_BY_BOOKING_DATE) {
                            transactionManagerUri.appendQueryParameter(
                                it.name,
                                ORDER_BY_EXTERNAL_ID,
                            )
                        } else {
                            transactionManagerUri.appendQueryParameter(it.name, propertyValue)
                        }
                    }
                }
            }
        }

        val transactionManagerConnection =
            NetworkConnectorBuilder(
                transactionManagerUri.build().toString().plus(TRANSACTION_MANAGER_QUERY_PARAMS),
            )
                .addRequestMethod(RequestMethods.GET)
                .buildConnection()

        val transactionManagerResponse = transactionManagerConnection.executeAsSuspended()

        if (transactionManagerResponse.isErrorResponse) {
            return CallState.Error(errorResponse = transactionManagerResponse)
        }

        var transactionsList: List<TransactionItem>
        withContext(Dispatchers.IO) {
            transactionsList =
                JsonParser.parseString(
                    transactionManagerResponse.stringResponse,
                ).asJsonArray.map { element ->
                    val jsonObject = element.asJsonObject
                    var originalDescription: String? = null
                    if (jsonObject.has(KEY_ORIGINAL_DESCRIPTION)) {
                        originalDescription = jsonObject.get(KEY_ORIGINAL_DESCRIPTION).toString()
                    }
                    TransactionItemJsonAdapter(moshi).fromJson(jsonObject.toString()).run json@{
                        TransactionItem item@{
                            // Mandatory Items
                            this@item.id = id ?: this@json?.id ?: STRING_EMPTY
                            this@item.type = this@json?.type ?: STRING_EMPTY
                            this@item.bookingDate = this@json?.bookingDate
                            this@item.valueDate = this@json?.valueDate
                            this@item.creditDebitIndicator =
                                when (this@json?.creditDebitIndicator) {
                                    CreditDebitIndicator.CRDT -> CreditDebitIndicatorItem.CREDIT
                                    CreditDebitIndicator.DBIT -> CreditDebitIndicatorItem.DEBIT
                                    else -> CreditDebitIndicatorItem.DEBIT
                                }
                            this@item.isIconVisible = true
                            // Mandatory Items End

                            this@item.checkAvailability =
                                when (this@json?.checkImageAvailability) {
                                    CheckImageAvailability.AVAILABLE -> CheckAvailability.AVAILABLE
                                    CheckImageAvailability.UNAVAILABLE ->
                                        CheckAvailability.UNAVAILABLE
                                    else -> CheckAvailability.UNAVAILABLE
                                }
                            this@item.checkSerialNumber = this@json?.checkSerialNumber
                            this@item.arrangementId = this@json?.arrangementId ?: STRING_EMPTY
                            this@item.description = this@json?.description ?: STRING_EMPTY
                            this@item.typeGroup = this@json?.typeGroup
                            this@item.category = this@json?.category
                            this@item.categoryId = this@json?.categoryId
                            this@item.address = this@json?.location?.address
                            this@json?.location?.toGeoCoordinates()?. let { cords ->
                                this@item.geoCoordinates = cords
                            }
                            this@item.merchantId = this@json?.merchant?.id
                            this@item.merchantName = this@json?.merchant?.name
                            this@item.merchantIconUrl = this@json?.merchant?.logo
                            this@item.merchantWebsite = this@json?.merchant?.website
                            if (this@json?.billingStatus != BillingStatus.PENDING.name) {
                                this@item.runningBalance = this@json?.runningBalance
                            } else {
                                // TODO
                            }
                            this@item.transactionAmountCurrency =
                                Amount amount@{
                                    this@amount.value =
                                        this@json?.transactionAmountCurrency?.amount?.toDouble()
                                    this@amount.currency =
                                        Currency.getInstance(
                                            this@json?.transactionAmountCurrency?.currencyCode,
                                        )
                                }
                            if (originalDescription?.isNotEmpty() == true) {
                                this@item.additions =
                                    hashMapOf<String, String>().apply {
                                        put(KEY_ORIGINAL_DESCRIPTION, originalDescription)
                                    }
                            } else {
                                this@json?.additions?.let { addition ->
                                    this@item.additions =
                                        addition.toMutableMap().apply {
                                            put(KEY_ORIGINAL_DESCRIPTION, this@json.description)
                                        }
                                } ?: run {
                                    this@item.additions =
                                        hashMapOf<String, String>().apply {
                                            put(
                                                KEY_ORIGINAL_DESCRIPTION,
                                                this@json?.description ?: STRING_EMPTY,
                                            )
                                        }
                                }
                            }
                        }
                    }
                }
        }

        if (transactionsList.isEmpty()) {
            return CallState.Empty
        }

        return CallState.Success(transactionsList)
    }

    override suspend fun getTransactions(
        from: Int,
        size: Int,
        arrangementId: String,
        query: String,
        state: State?,
    ): CallState<out List<TransactionItem>> {
        // Note :- Using above getTransactions method
        // hence returning default impl to satisfy method return type
        return transactionsUseCaseImpl.getTransactions(from, size, arrangementId, query, state)
    }

    override suspend fun getTransactionCheckImages(
        transactionId: String,
    ): CallState<out List<TransactionCheckImage>> {
        return transactionsUseCaseImpl.getTransactionCheckImages(transactionId)
    }
}
