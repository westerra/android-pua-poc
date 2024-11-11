package com.westerra.release.payments.usecase

import androidx.core.net.toUri
import com.backbase.android.Backbase
import com.backbase.android.client.contactmanagerclient2.model.ContactGetResponseBodyJsonAdapter
import com.backbase.android.retail.journey.payments.PaymentContactsUseCase
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

class CustomPaymentContactsUseCase(private val moshi: Moshi) : PaymentContactsUseCase {
    companion object {
        private const val CURRENCY_CODE_USD = "USD"

        private const val CONTACTS_URL_ENDPOINT = "/api/contact-manager/client-api/v2/contacts"
        private const val FROM_QUERY_PARAM = "from"
        private const val SIZE_QUERY_PARAM = "size"
    }

    private val edgeDomain: String get() =
        Backbase.getInstance()!!.configuration.experienceConfiguration.serverURL

    override suspend fun getPaymentContacts(
        vararg requestParams: PaymentContactsUseCase.RequestParams,
    ): PaymentContactsUseCase.RetrievePaymentContactsResult<List<PaymentParty>> {
        val contactsUri = edgeDomain.plus(CONTACTS_URL_ENDPOINT).toUri().buildUpon()

        requestParams.forEach { param ->
            when (param) {
                is PaymentContactsUseCase.RequestParams.Page -> {
                    contactsUri.appendQueryParameter(FROM_QUERY_PARAM, "${param.index}")
                }
                is PaymentContactsUseCase.RequestParams.PageSize -> {
                    contactsUri.appendQueryParameter(SIZE_QUERY_PARAM, "${param.size}")
                }
                else -> Unit
            }
        }

        val contactsConnection =
            NetworkConnectorBuilder(contactsUri.build().toString())
                .addRequestMethod(RequestMethods.GET)
                .buildConnection()

        val contactsResponse = contactsConnection.executeAsSuspended()

        if (contactsResponse.isErrorResponse) {
            return PaymentContactsUseCase.RetrievePaymentContactsResult.Failure(
                PaymentContactsUseCase.RetrievePaymentContactsResult.Failure.Cause.Unknown(
                    message = contactsResponse.errorMessage,
                ),
            )
        }

        var paymentPartyList: List<PaymentParty>
        withContext(Dispatchers.IO) {
            paymentPartyList =
                JsonParser.parseString(
                    contactsResponse.stringResponse,
                ).asJsonArray.map {
                    val paymentParty =
                        ContactGetResponseBodyJsonAdapter(moshi).fromJson(it.toString())!!

                    PaymentParty(
                        id = paymentParty.id,
                        name = paymentParty.name,
                        identifications = listOf(
                            IdentificationType.AccountNumber(
                                paymentParty.accounts[0].accountNumber.toString(),
                            ),
                            IdentificationType.EmailAddress(
                                paymentParty.accounts[0].accountNumber.toString(),
                            ),
                            IdentificationType.BBAN(
                                paymentParty.accounts[0].accountNumber.toString(),
                            ),
                        ),
                        paymentPartyType = PaymentPartyType.Contact,
                        currencyCode = CURRENCY_CODE_USD,
                        availableFunds = null,
                    )
                }
        }

        return PaymentContactsUseCase.RetrievePaymentContactsResult.Success(paymentPartyList)
    }
}
