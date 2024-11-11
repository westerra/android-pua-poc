package com.westerra.release.accountstatements

import androidx.core.net.toUri
import com.backbase.android.client.accountstatementsclient2.api.AccountStatementApi
import com.backbase.android.client.accountstatementsclient2.model.AccountStatementJsonAdapter
import com.backbase.android.retail.journey.accountstatements.AccountStatementsRequestParams
import com.backbase.android.retail.journey.accountstatements.AccountStatementsUseCase
import com.backbase.android.retail.journey.accountstatements.CallState
import com.backbase.android.retail.journey.accountstatements.gen_accountstatements_client_2.AccountStatementsUseCaseImpl
import com.backbase.android.retail.journey.accountstatements.models.AccountStatementIdentificationItem
import com.backbase.android.retail.journey.accountstatements.models.AccountStatementItem
import com.backbase.android.utils.net.NetworkConnectorBuilder
import com.backbase.android.utils.net.request.RequestMethods
import com.google.gson.JsonParser
import com.squareup.moshi.Moshi
import com.westerra.release.extensions.appendToBaseUrl
import com.westerra.release.extensions.backbase.executeAsSuspended
import com.westerra.release.extensions.getCurrentLocalDate
import com.westerra.release.extensions.toYMDString
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CustomAccountStatementUseCase(
    private val moshi: Moshi,
    private val accountStatementApi: AccountStatementApi,
) : AccountStatementsUseCase {

    companion object {
        private const val ACCOUNT_STATEMENT_URL_ENDPOINT =
            "/api/account-statement/client-api/v2/account/statements"
        private const val ACCOUNT_STATEMENT_QUERY_PARAMS = "&orderBy=date&direction=DESC"

        // DEFAULT_DATE_FROM_VALUE was used in 'web' to get statements
        private const val DEFAULT_DATE_FROM_VALUE = "1970-01-01"
        private const val DEFAULT_FROM_VALUE_MULTIPLIER = 10

        private const val ACCOUNT_ID_QUERY_PARAM = "accountId"
        private const val DATE_FROM_QUERY_PARAM = "dateFrom"
        private const val DATE_TO_QUERY_PARAM = "dateTo"
        private const val CATEGORY_QUERY_PARAM = "category"
        private const val FROM_QUERY_PARAM = "from"
        private const val SIZE_QUERY_PARAM = "size"
    }

    private val defaultAccountStatementsUseCase get() = AccountStatementsUseCaseImpl(
        accountStatementApi,
    )

    override suspend fun getAccountStatements(
        accountStatementsRequestParams: AccountStatementsRequestParams,
    ): CallState<out List<AccountStatementItem>> {
        val accountStatementsUri =
            ACCOUNT_STATEMENT_URL_ENDPOINT.appendToBaseUrl().toUri().buildUpon()

        accountStatementsRequestParams.accountId?.let { accountId ->
            accountStatementsUri.appendQueryParameter(ACCOUNT_ID_QUERY_PARAM, accountId)
        }

        accountStatementsUri.appendQueryParameter(
            DATE_FROM_QUERY_PARAM,
            accountStatementsRequestParams.dateFrom?.toYMDString()
                ?: DEFAULT_DATE_FROM_VALUE,
        )

        accountStatementsUri.appendQueryParameter(
            DATE_TO_QUERY_PARAM,
            accountStatementsRequestParams.dateTo?.toYMDString()
                ?: getCurrentLocalDate().toYMDString(),
        )

        accountStatementsRequestParams.categories?.forEach { category ->
            accountStatementsUri.appendQueryParameter(CATEGORY_QUERY_PARAM, category)
        }

        accountStatementsUri.appendQueryParameter(
            FROM_QUERY_PARAM,
            (accountStatementsRequestParams.from * DEFAULT_FROM_VALUE_MULTIPLIER).toString(),
        )
        accountStatementsUri.appendQueryParameter(
            SIZE_QUERY_PARAM,
            accountStatementsRequestParams.size.toString(),
        )

        val accountStatementsConnection = NetworkConnectorBuilder(
            accountStatementsUri.build().toString().plus(ACCOUNT_STATEMENT_QUERY_PARAMS),
        )
            .addRequestMethod(RequestMethods.GET)
            .buildConnection()

        val accountStatementsResponse = accountStatementsConnection.executeAsSuspended()

        if (accountStatementsResponse.isErrorResponse) {
            return CallState.Error(errorResponse = accountStatementsResponse)
        }

        var accountStatementsList: List<AccountStatementItem>
        withContext(Dispatchers.IO) {
            accountStatementsList = JsonParser.parseString(
                accountStatementsResponse.stringResponse,
            ).asJsonArray.map {
                AccountStatementJsonAdapter(moshi).fromJson(it.asJsonObject.toString()).run json@{
                    AccountStatementItem item@{
                        this@item.accountId = this@json?.accountId
                        this@item.date = this@json?.date
                        this@item.description = this@json?.description
                        this@item.category = this@json?.category ?: ""
                        this@item.documents = this@json?.documents?.map {
                                accountStatementDocument ->
                            AccountStatementIdentificationItem {
                                uid = accountStatementDocument.uid
                                contentType = accountStatementDocument.contentType
                                url = accountStatementDocument.url
                                additions?.map { _ -> accountStatementDocument.additions }
                            }
                        } ?: listOf()
                    }
                }
            }
        }

        return CallState.Success(accountStatementsList)
    }

    override suspend fun getCategories(): CallState<out List<String>> {
        return defaultAccountStatementsUseCase.getCategories()
    }

    override suspend fun downloadAccountStatement(uid: String): CallState<out ByteArray> {
        return defaultAccountStatementsUseCase.downloadAccountStatement(uid = uid)
    }
}
