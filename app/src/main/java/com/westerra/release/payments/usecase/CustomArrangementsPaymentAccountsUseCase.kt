package com.westerra.release.payments.usecase

import androidx.core.net.toUri
import com.backbase.android.Backbase
import com.backbase.android.client.gen2.arrangementclient2.model.ProductSummaryItem
import com.backbase.android.client.gen2.arrangementclient2.model.ProductSummaryItemJsonAdapter
import com.backbase.android.retail.journey.payments.PaymentAccountsUseCase
import com.backbase.android.retail.journey.payments.model.Balance
import com.backbase.android.retail.journey.payments.model.IdentificationType
import com.backbase.android.retail.journey.payments.model.PaymentParty
import com.backbase.android.retail.journey.payments.model.PaymentPartyType
import com.backbase.android.utils.net.NetworkConnectorBuilder
import com.backbase.android.utils.net.request.RequestMethods
import com.google.gson.JsonParser
import com.squareup.moshi.Moshi
import com.westerra.release.extensions.backbase.executeAsSuspended
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CustomArrangementsPaymentAccountsUseCase(
    private val moshi: Moshi,
) : PaymentAccountsUseCase {

    companion object {
        private const val ARRANGEMENT_MANAGER_URL_ENDPOINT =
            "/api/arrangement-manager/client-api/v2/productsummary/context/arrangements"
        private const val ARRANGEMENT_MANAGER_QUERY_PARAMS =
            "&resourceName=Payments&privilege=create&from=0&size=100&maskIndicator=true"

        private const val QUERY_PARAM_CREDIT_ACCOUNT = "creditAccount"
        private const val QUERY_PARAM_DEBIT_ACCOUNT = "debitAccount"
        private const val QUERY_PARAM_BUSINESS_FUNCTION = "businessFunction"

        private const val BUSINESS_FUNCTION_P2P_TRANSFER = "P2P Transfer"
        private const val BUSINESS_FUNCTION_A2A_TRANSFER = "A2A Transfer"
    }

    private val edgeDomain: String get() =
        Backbase.getInstance()!!.configuration.experienceConfiguration.serverURL

    override suspend fun getPaymentParties(
        vararg requestParams: PaymentAccountsUseCase.RequestParams,
    ): PaymentAccountsUseCase.RetrieveProductsResult {
        val arrangementManagerUri =
            edgeDomain.plus(ARRANGEMENT_MANAGER_URL_ENDPOINT).toUri().buildUpon()

        requestParams.forEach { param ->
            when (param) {
                is PaymentAccountsUseCase.RequestParams.CreditAccount -> {
                    arrangementManagerUri.appendQueryParameter(
                        QUERY_PARAM_CREDIT_ACCOUNT,
                        param.creditAccount.toString(),
                    )
                }
                is PaymentAccountsUseCase.RequestParams.DebitAccount -> {
                    arrangementManagerUri.appendQueryParameter(
                        QUERY_PARAM_DEBIT_ACCOUNT,
                        param.debitAccount.toString(),
                    )
                }
                is PaymentAccountsUseCase.RequestParams.BusinessFunction -> {
                    // overriding the default functionality for p2p transfer business function
                    // append a2a transfer business function in the uri to get all accounts listed,
                    // instead of p2p transfer same as in transfer to member "web"
                    arrangementManagerUri.appendQueryParameter(
                        QUERY_PARAM_BUSINESS_FUNCTION,
                        if (param.businessFunction == BUSINESS_FUNCTION_P2P_TRANSFER) {
                            BUSINESS_FUNCTION_A2A_TRANSFER
                        } else {
                            param.businessFunction
                        },
                    )
                }
                else -> Unit
            }
        }

        val arrangementManagerConnection = NetworkConnectorBuilder(
            arrangementManagerUri.build().toString().plus(ARRANGEMENT_MANAGER_QUERY_PARAMS),
        ).addRequestMethod(
            RequestMethods.GET,
        )
            .buildConnection()

        val arrangementManagerResponse = arrangementManagerConnection.executeAsSuspended()

        if (arrangementManagerResponse.isErrorResponse) {
            return PaymentAccountsUseCase.RetrieveProductsResult.ServerError(
                errorMessage = arrangementManagerResponse.errorMessage,
            )
        }

        val arrangementManagerResult = kotlin.runCatching {
            var productSummaryItems: List<ProductSummaryItem>
            withContext(Dispatchers.IO) {
                productSummaryItems = JsonParser.parseString(
                    arrangementManagerResponse.stringResponse,
                ).asJsonArray.map {
                    ProductSummaryItemJsonAdapter(moshi).fromJson(it.asJsonObject.toString())!!
                }
            }
            productSummaryItems
        }

        return if (arrangementManagerResult.isSuccess) {
            PaymentAccountsUseCase.RetrieveProductsResult.Success(
                arrangementManagerResult.getOrDefault(
                    listOf(),
                ).map {
                    PaymentParty(
                        id = it.id,
                        name = it.displayName ?: (
                            it.userPreferences?.alias ?: (
                                it.bankAlias
                                    ?: it.name
                                )
                            ),
                        availableFunds = Balance(
                            availableBalance = it.availableBalance,
                            bookedBalance = it.bookedBalance,
                            remainingCredit = it.remainingCredit,
                        ),
                        identifications = mutableListOf<IdentificationType>().apply {
                            it.IBAN?.let { iban -> add(IdentificationType.IBAN(iban)) }
                            it.BBAN?.let { bban -> add(IdentificationType.BBAN(bban)) }
                            it.productNumber?.let { productNumber ->
                                add(
                                    IdentificationType.ProductNumber(
                                        productNumber,
                                    ),
                                )
                            }
                            it.number?.let { number -> add(IdentificationType.Number(number)) }
                        }.toList(),
                        paymentPartyType = when (it.productKindName) {
                            "Credit Card" -> PaymentPartyType.CreditCard
                            "Debit Card" -> PaymentPartyType.DebitCard
                            "Savings Account" -> PaymentPartyType.SavingsAccount
                            "Term Deposit" -> PaymentPartyType.TermDeposit
                            "Investment Account" -> PaymentPartyType.InvestmentAccount
                            "Loan" -> PaymentPartyType.Loan
                            "Current Account" -> PaymentPartyType.CurrentAccount
                            else -> PaymentPartyType.Contact
                        },
                        currencyCode = it.currency,
                    )
                }.toList(),
            )
        } else {
            PaymentAccountsUseCase.RetrieveProductsResult.ServerError(
                errorMessage = arrangementManagerResult.exceptionOrNull()?.message,
            )
        }
    }
}
