package com.westerra.release

import android.app.Application
import android.graphics.Color
import android.util.Base64
import com.backbase.android.Backbase
import com.backbase.android.business.journey.workspaces.accesscontrol_client_2.WorkspacesUseCaseImpl
import com.backbase.android.client.gen2.accountstatementsclient2.api.AccountStatementApi
import com.backbase.android.client.gen2.addressautocompleteclient1.api.AddressAutocompleteApi
import com.backbase.android.client.gen2.arrangementclient2.api.ArrangementsApi
import com.backbase.android.client.gen2.arrangementclient2.api.ProductSummaryApi
import com.backbase.android.client.gen2.cardsclient2.api.CardProductsApi
import com.backbase.android.client.gen2.cardsclient2.api.CardsApi
import com.backbase.android.client.gen2.cardsclient2.api.TravelNoticesApi
import com.backbase.android.client.gen2.engagementclient1.api.EngagementMetricsApi
import com.backbase.android.client.gen2.engagementclient1.api.EngagementMetricsApiParams
import com.backbase.android.client.gen2.engagementclient1.api.SelectBannerApi
import com.backbase.android.client.gen2.engagementclient1.api.SelectBannerApiParams
import com.backbase.android.client.gen2.engagementclient1.model.MetricClickPostRequest
import com.backbase.android.client.gen2.engagementclient1.model.SelectedBannerResource
import com.backbase.android.client.gen2.externalaccountaggregatorclient1.api.ExternalAccountAggregationClientApi
import com.backbase.android.client.gen2.financialinstitutionmanagerclient1.api.FinancialInstitutionManagerClientApi
import com.backbase.android.client.gen2.loansclient1.api.LoansApi
import com.backbase.android.client.gen2.paymentordera2aclient1.api.A2aClientApi
import com.backbase.android.client.gen2.savvymoneycreditscorerclient1.api.SavvyMoneyCreditScorerApi
import com.backbase.android.client.gen2.transactionsclient2.api.TransactionClientApi
import com.backbase.android.client.usermanagerclient2.api.UserProfileManagementApi
import com.backbase.android.clients.common.ResponseListener
import com.backbase.android.identity.journey.authentication.identity_auth_client_1.IdentityAuthClient1AuthenticationUseCase
import com.backbase.android.identity.journey.userprofile.autocomplete.usecase.AddressAutocompleteUserProfileUseCase
import com.backbase.android.identity.journey.userprofile.idmanagement.usecase.IdentityManagementUserProfileUseCase
import com.backbase.android.identity.journey.userprofile.usecase.UserManagerUserProfileUseCase
import com.backbase.android.journey.dashboard.bannertile.usecase.BannerUseCaseParams
import com.backbase.android.journey.dashboard.bannertile.usecase.DashboardBannerMetricUseCaseImp
import com.backbase.android.journey.dashboard.bannertile.usecase.DashboardBannerUseCaseImpl
import com.backbase.android.retail.feature_filter.UserEntitlementFeatureFilterUseCase
import com.backbase.android.retail.feature_filter.entitlements.accesscontrol_client_2.AccessControlClient2EntitlementsUseCase
import com.backbase.android.retail.journey.accounts_and_transactions.AccountsAndTransactionsConfiguration
import com.backbase.android.retail.journey.accounts_and_transactions.banner.AccountsBannerEngagementInteractionRequest
import com.backbase.android.retail.journey.accounts_and_transactions.banner.AccountsBannerResponse
import com.backbase.android.retail.journey.accounts_and_transactions.banner.AccountsBannerUseCase
import com.backbase.android.retail.journey.accounts_and_transactions.banner.AccountsDeepRouting
import com.backbase.android.retail.journey.accounts_and_transactions.gen_arrangements_client_2.ArrangementViewsUseCaseImpl
import com.backbase.android.retail.journey.accounts_and_transactions.gen_arrangements_client_2.ArrangementsUseCaseImpl
import com.backbase.android.retail.journey.accounts_and_transactions.gen_arrangements_client_2.ProductSummaryUseCaseImpl
import com.backbase.android.retail.journey.accounts_and_transactions.gen_loans_client_1.LoanDetailsUseCaseImpl
import com.backbase.android.retail.journey.accounts_and_transactions.gen_transaction_client_2.TransactionsUseCaseImpl
import com.backbase.android.retail.journey.accounts_and_transactions.transactions.CallState
import com.backbase.android.retail.journey.accountstatements.gen_accountstatements_client_2.AccountStatementsUseCaseImpl
import com.backbase.android.retail.journey.app.us.UsApplicationConfiguration
import com.backbase.android.retail.journey.app.us.UsUseCaseDefinitions
import com.backbase.android.retail.journey.billPaySso.genBillPayIntegrator.BillPayIntegratorSsoUseCaseImpl
import com.backbase.android.retail.journey.cardsmanagement.SimpleCallState
import com.backbase.android.retail.journey.cardsmanagement.gen_2_cards_client_2_card_products_use_case.CardProductsUseCaseImpl
import com.backbase.android.retail.journey.cardsmanagement.gen_cards_client_2_travel_notice_use_case.TravelNoticeUseCaseImpl
import com.backbase.android.retail.journey.cardsmanagement.gen_cards_client_2_use_case.CardsUseCaseImpl
import com.backbase.android.retail.journey.cardsmanagement.gen_cards_client_2_use_case.ChangePinUseCaseImpl
import com.backbase.android.retail.journey.cardsmanagement.gen_usermanager_client_2_user_use_case.UserUseCaseImpl
import com.backbase.android.retail.journey.cardsmanagement.usecase.cardimages.CardImageRequestParams
import com.backbase.android.retail.journey.cardsmanagement.usecase.cardimages.CardImagesResponse
import com.backbase.android.retail.journey.cardsmanagement.usecase.cardimages.CardImagesUseCase
import com.backbase.android.retail.journey.contacts.contactmanager_client_2.GenContactManagerClient2ContactsUseCase
import com.backbase.android.retail.journey.credit_score.gen_client.GenClientCreditScoreUseCaseImpl
import com.backbase.android.retail.journey.financialinsights.gen_2_budgets_client_3_use_case.BudgetsUseCaseImpl
import com.backbase.android.retail.journey.financialinsights.gen_2_expense_analyzer_client_1_use_case.IncomeExpenseAnalyzerUseCaseImpl
import com.backbase.android.retail.journey.financialinsights.gen_2_transaction_client_2_use_case.TransactionCategoriesUseCaseImpl
import com.backbase.android.retail.journey.gen2_pockets_paymentorder_client_2_payment_service_use_case.PocketsPaymentOrderV2Client2UseCaseImpl
import com.backbase.android.retail.journey.gen_2_arrangements_client_2_arrangements_use_case.PocketsArrangementsUseCaseImpl
import com.backbase.android.retail.journey.gen_external_account_aggregation_client.ExternalAccountAggregationUseCaseImpl
import com.backbase.android.retail.journey.gen_financial_institutions_1_use_case.FinancialInstitutionsUseCaseImpl
import com.backbase.android.retail.journey.gen_financial_institutions_1_use_case.UrlDrawableProvider
import com.backbase.android.retail.journey.gen_pockets_client_2_use_case.PocketsUseCaseImpl
import com.backbase.android.retail.journey.payments.gen2_beneficiary_validation_client_1.BeneficiaryValidationClient1UseCase
import com.backbase.android.retail.journey.payments.gen2_paymentorder_v2_client_2.PaymentOrderV2Client2PaymentServiceUseCase
import com.backbase.android.retail.journey.payments.gen_arrangements_client_2.ArrangementsClient2PaymentAccountsUseCase
import com.backbase.android.retail.journey.payments.gen_contactmanager_client_2.ContactManagerClient2PaymentContactsUseCase
import com.backbase.android.retail.journey.payments.gen_paymentordera2a_client_1.PaymentOrderA2AClient1ServiceUseCase
import com.backbase.android.retail.journey.payments.upcoming.gen2_paymentorder_v2_client_2.PaymentOrderV2Client2UpcomingPaymentsServiceUseCase
import com.backbase.android.retail.journey.places.placemanager_client_2.GenPlaceManagerClient2PlacesUseCase
import com.backbase.android.retail.journey.rdc.gen_remotedepositcapturer_client_2.Rdc2ServiceUseCase
import com.backbase.android.retail.journey.rdc.gen_remotedepositcapturer_client_2.model.DeviceInfo
import com.backbase.android.retail.journey.rdh.gen2_remotedeposithistory_client2.RemoteDepositHistory2ServiceUseCase
import com.backbase.android.retail.journey.rtc.gen_client.GenClientRTCUseCaseImpl
import com.backbase.android.utils.net.response.Response
import com.backbase.deferredresources.DeferredColor
import com.backbase.deferredresources.drawable.DeferredColorDrawable
import com.backbase.engagementchannels.core.util.Result
import com.backbase.engagementchannels.gen_2_arrangements_client_2_accounts_use_case.ArrangementClient2AccountsUseCase
import com.backbase.engagementchannels.gen_2_engagements_client_1_notification_settings_usecase.GenEngagementsClient1NotificationSettingsUseCase
import com.backbase.engagementchannels.gen_2_metric_client_1_messages_metric_use_case.Gen2MetricClient1MessagesMetricUseCaseImpl
import com.backbase.engagementchannels.messages.gen_message_client_5.GenMessageClient5MessagesUseCase
import com.backbase.engagementchannels.notifications.gen2_notification_client_3.Gen2NotificationClient3NotificationsUseCase
import com.backbase.engagementchannels.unreadnotifications.gen_2_notification_client_3.Gen2NotificationClient3UnreadNotificationsUseCase
import com.backbase.engagementchannels.unreadnotifications.gen_notification_client_2.GenNotificationClient2UnreadNotificationsUseCase
import com.backbase.engagementchannels.unreadnotifications.gen_notification_client_2.UnreadNotificationsUseCase
import com.backbase.gen2_rtc_conversation_sdk_use_case.GenRTCConversationSDKUseCaseImpl
import com.bumptech.glide.Glide
import com.westerra.release.QuickActionQualifiers.NOTIFICATION_CLIENT_2
import com.westerra.release.QuickActionQualifiers.NOTIFICATION_CLIENT_3
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Locale
import javax.net.ssl.HttpsURLConnection
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import com.backbase.android.client.gen2.paymentorderv2client2.api.PaymentOrdersApi as PaymentOrdersV2Api
import com.backbase.android.retail.journey.gen_2_pockets_transactions_client_2_use_case.TransactionsUseCaseImpl as PocketsTransactionsUseCaseImpl
import com.backbase.android.retail.journey.rdh.gen2_remotedeposithistory_client2.model.DeviceInfo as RdhDeviceInfo
import com.backbase.engagementchannels.gen_2_engagements_client_1_banners_usecase.GetBannerRequest as GetEngagementBannerRequest
import com.backbase.engagementchannels.unreadnotifications.gen_2_notification_client_3.UnreadNotificationsUseCase as UnreadNotificationsUseCaseV2


/**
 * Default value of [WesterraUseCaseDefinitions] for all the ModelBank applications.
 *
 * - Please name the constructor parameters of the useCase because there are too many definitions
 * of this class and we use service locator pattern to inject dependencies. _/\_
 *
 * - Please maintain alphabetical order by `Definition` name. _/\_
 *
 */

@Suppress("FunctionName")
fun WesterraUseCaseDefinitions(
    overrides: UsUseCaseDefinitions.Builder.() -> Unit = {},
): UsUseCaseDefinitions {
    return DefaultUseCaseDefinitions().apply(overrides).build()
}

@Suppress("FunctionName", "RemoveExplicitTypeArguments")
internal fun DefaultUseCaseDefinitions() = UsUseCaseDefinitions.Builder().apply {
    accountsBannerUseCaseDefinition = {
        if (getOrNull<AccountsAndTransactionsConfiguration>()?.accountsScreen?.isBannersEnabled == true) {
            UsEngagementAccountsBannerUseCase(
                request = GetEngagementBannerRequest {
                    appName = "backbase-retail-prototypes:retail-mobile-android:0"
                    screenName = "index"
                    campaignSpaceName = "bb-campaign-space-android-0"
                    campaignSpaceDimensions = "300x250"
                    appLocale = Locale.getDefault()
                },
                get(),
                get(),
            )
        } else {
            null
        }
    }

    accountStatementsUseCaseDefinition = {
        AccountStatementsUseCaseImpl(
            client = get<AccountStatementApi>(),
        )
    }

    accountsUseCaseDefinition = {
        ProductSummaryUseCaseImpl(
            productSummaryClient = get<ProductSummaryApi>(),
        )
    }

    accountsUnreadNotificationUseCaseDefinition = {
        if (get<UsApplicationConfiguration>().useNewUnreadNotificationUseCase) {
            UsEngagementUnreadNotificationUseCaseV2(
                unreadNotificationsUseCase = get<UnreadNotificationsUseCaseV2>(),
                unreadNotificationsCachedTimeInSeconds = get<UsApplicationConfiguration>().unreadNotificationsCachedTimeInSeconds,
            )
        } else {
            UsEngagementUnreadNotificationUseCase(
                unreadNotificationsUseCase = get<UnreadNotificationsUseCase>(),
                unreadNotificationsCachedTimeInSeconds = get<UsApplicationConfiguration>().unreadNotificationsCachedTimeInSeconds,
            )
        }
    }
    addressAutocompleteUseCaseDefinition = {
        AddressAutocompleteUserProfileUseCase(
            addressAutocompleteApi = get<AddressAutocompleteApi>(),
        )
    }

    arrangementsUseCaseDefinition = {
        ArrangementsUseCaseImpl(
            client = get<ArrangementsApi>(),
        )
    }

    authenticationUseCaseDefinition = {
        IdentityAuthClient1AuthenticationUseCase(
            application = get(),
            authClient = get(),
        )
    }

    billPayIntegratorSsoUseCaseDefinition = {
        BillPayIntegratorSsoUseCaseImpl(
            billPaySsoApi = get(),
        )
    }


    cardsCardImagesUseCaseDefinition = {
        object : CardImagesUseCase {
            override suspend fun fetchCardImage(
                requestParams: CardImageRequestParams,
            ): SimpleCallState.Success<CardImagesResponse> {
                return SimpleCallState.Success(CardImagesResponse {})
            }
        }
    }

    cardsUseCaseDefinition = {
        CardsUseCaseImpl(
            client = get<CardsApi>(),
        )
    }

    cardsUserManagerUseCaseDefinition = {
        UserUseCaseImpl(
            userManagerClient = get(),
        )
    }

    cardsCardImagesUseCaseDefinition = {
        object : CardImagesUseCase {
            override suspend fun fetchCardImage(
                requestParams: CardImageRequestParams,
            ): SimpleCallState.Success<CardImagesResponse> {
                return SimpleCallState.Success(
                    CardImagesResponse {
                        front = DeferredColorDrawable(DeferredColor.Constant(Color.DKGRAY))
                        back = DeferredColorDrawable(DeferredColor.Constant(Color.LTGRAY))
                    },
                )
            }
        }
    }

    cardProductsUseCaseDefinition = {
        CardProductsUseCaseImpl(
            client = get<CardProductsApi>(),
        )
    }

    cardsTravelNoticeUseCaseDefinition = {
        TravelNoticeUseCaseImpl(
            client = get<TravelNoticesApi>(),
        )
    }

    changePinUseCaseDefinition = {
        ChangePinUseCaseImpl(
            client = get<CardsApi>(),
        )
    }

    contactsUseCaseDefinition = {
        GenContactManagerClient2ContactsUseCase(
            contactsApi = get(),
        )
    }

    creditScoreUseCaseDefinition = {
        GenClientCreditScoreUseCaseImpl(creditScorerApi = get<SavvyMoneyCreditScorerApi>())
    }

    entitlementsUseCaseDefinition = {
        AccessControlClient2EntitlementsUseCase(
            usersApi = get(),
        )
    }

    externalAccountAggregationUseCaseDefinition = {
        ExternalAccountAggregationUseCaseImpl(
            client = get<ExternalAccountAggregationClientApi>(),
        )
    }

    externalPaymentAccountsServiceUseCaseDefinition = {
        PaymentOrderA2AClient1ServiceUseCase(
            a2aClientApi = get<A2aClientApi>(),
        )
    }

    featureFilterUserEntitleUseCaseDefinition = {
        UserEntitlementFeatureFilterUseCase(
            entitlementsUseCase = get(),
        )
    }

    financialInsightsBudgetsUseCaseDefinition = {
        BudgetsUseCaseImpl(
            client = get(),
        )
    }

    financialInsightsUseCaseDefinition = {
        IncomeExpenseAnalyzerUseCaseImpl(
            client = get(),
        )
    }

    financialInsightsTransactionCategoriesUseCaseDefinition = {
        TransactionCategoriesUseCaseImpl(
            client = get(),
        )
    }

    financialInstitutionsUseCaseDefinition = {
        FinancialInstitutionsUseCaseImpl(
            // keep the explicit types, otherwise it's an ugly internal cast that may fail
            // in case of a new constructor overload
            client = get<FinancialInstitutionManagerClientApi>(),
            urlDrawableProvider = getOrNull<UrlDrawableProvider>()
                ?: UrlDrawableProvider {
                    Glide.with(get<Application>().applicationContext).load(it).submit().get()
                },
        )
    }

    identityManagementUseCaseDefinition = {
        IdentityManagementUserProfileUseCase(
            identityManagementApi = get(),
        )
    }

    loanDetailsUseCaseDefinition = {
        LoanDetailsUseCaseImpl(
            loansApi = get<LoansApi>(),
        )
    }

    messagesUseCaseDefinition = {
        GenMessageClient5MessagesUseCase(
            messagecenterApi = get(),
        )
    }

    messagesMetricUseCaseDefinition = {
        Gen2MetricClient1MessagesMetricUseCaseImpl(
            interactionApi = get(),
        )
    }

    workspacesUseCaseDefinition = {
        WorkspacesUseCaseImpl(
            userContextApi = get(),
            userRepository = get(),
        )
    }

    notificationAccountsUseCaseDefinition = {
        ArrangementClient2AccountsUseCase(productSummaryApi = get())
    }

    notificationPreferencesUseCaseDefinition = {
        GenEngagementsClient1NotificationSettingsUseCase(preferencesClient = get())
    }

//    notificationPreferencesUseCaseDefinition = {
//        if (getOrNull<NotificationsConfiguration>()?.notificationSettingsAPI ==
//            NotificationSettingsAPI.ENGAGEMENTS
//        ) {
//            GenEngagementsClient1NotificationSettingsUseCase(preferencesClient = get())
//        } else {
//            null
//        }
//    }

//    notificationSettingsUseCaseDefinition = {
//        if (getOrNull<NotificationsConfiguration>()?.notificationSettingsAPI ==
//            NotificationSettingsAPI.ENGAGEMENTS
//        ) {
//            NoActionsAccountsNotificationSettingsUseCase(productSummaryApi = get())
//        } else {
//            GenActionClient2GenArrangementClient2NotificationSettingsUseCase(
//                actionRecipesApi = get(),
//                productSummaryApi = get(),
//            )
//        }
//    }

    notificationsUseCaseDefinition = {
        Gen2NotificationClient3NotificationsUseCase(
            notificationsApi = get(qualifier = NOTIFICATION_CLIENT_3),
        )
    }

    paymentAccountsUseCaseDefinition = {
        ArrangementsClient2PaymentAccountsUseCase(
            productSummaryApiV2 = get(),
            arrangementsApi = get<ArrangementsApi>(),
        )
    }

    paymentRecipientPaymentPartyValidationUseCaseDefinition = {
        BeneficiaryValidationClient1UseCase(
            beneficiaryValidationApi = get(),
        )
    }

    paymentUseCaseDefinition = {
        PaymentOrderV2Client2PaymentServiceUseCase(
            paymentOrdersApi = get<PaymentOrdersV2Api>(),
        )
    }

    paymentContactsUseCaseDefinition = {
        ContactManagerClient2PaymentContactsUseCase(
            contactsApi = get<com.backbase.android.client.gen2.contactmanagerclient2.api.ContactsApi>(),
        )
    }

    placesUseCaseDefinition = {
        GenPlaceManagerClient2PlacesUseCase(
            placesApi = get(),
        )
    }

    pocketsArrangementsUseCaseDefinition = {
        PocketsArrangementsUseCaseImpl(
            client = get(),
            arrangementsApi = get(),
        )
    }

    pocketsPaymentsUseCaseDefinition = {
        PocketsPaymentOrderV2Client2UseCaseImpl(
            paymentOrdersApi = get(),
        )
    }

    pocketsTransactionsUseCaseDefinition = {
        PocketsTransactionsUseCaseImpl(
            client = get(),
        )
    }

    pocketsUseCaseDefinition = {
        PocketsUseCaseImpl(
            client = get(),
        )
    }

    rdcServiceUseCaseDefinition = {
        Rdc2ServiceUseCase(
            deviceInfo = DeviceInfo { appVersion = "1.2.16" },
            rdcClientApiV2 = get(),
            productSummaryApiV2 = get(),
        )
    }

    rdhServiceUseCaseDefinition = {
        RemoteDepositHistory2ServiceUseCase(
            deviceInfo = get<RdhDeviceInfo>(),
            rdcClientApi = get(),
        )
    }

    rtcUseCaseDefinition = {
        GenClientRTCUseCaseImpl(accessApi = get(), conversationsApi = get(), queuesApi = get())
    }

    rtcConversationSDKUseCaseDefinition = {
        GenRTCConversationSDKUseCaseImpl(get())
    }

    transactionsUseCaseDefinition = {
        TransactionsUseCaseImpl(
            client = get<TransactionClientApi>(),
        )
    }

    upcomingPaymentsUseCaseDefinition = {
        PaymentOrderV2Client2UpcomingPaymentsServiceUseCase(
            paymentOrdersApi = get<PaymentOrdersV2Api>(),
        )
    }

    userProfileUseCaseDefinition = {
        UserManagerUserProfileUseCase(
            userProfileManagementApi = get<UserProfileManagementApi>(),
        )
    }

    unreadNotificationsUseCaseDefinition = {
        GenNotificationClient2UnreadNotificationsUseCase(
            notificationsApi = get(qualifier = NOTIFICATION_CLIENT_2),
        )
    }

    unreadNotificationsUseCaseV2Definition = {
        Gen2NotificationClient3UnreadNotificationsUseCase(
            notificationsApi = get(qualifier = NOTIFICATION_CLIENT_3),
        )
    }

    arrangementViewsUseCaseDefinition = {
        ArrangementViewsUseCaseImpl(get())
    }

    dashboardBannerUseCaseDefinition = {
        DashboardBannerUseCaseImpl(
            selectBannerApi = get<SelectBannerApi>(),
            bannerUseCaseParams = BannerUseCaseParams {
                appName = "backbase-retail-prototypes:retail-mobile-android:0"
                screenName = "index"
                campaignSpaceName = "bb-campaign-space-android-0"
                campaignSpaceDimensions = "300x250"
            },
            engagementMetricsApi = get<EngagementMetricsApi>(),
        )
    }

    accountsBannerMetricUseCaseDefinition = {
        UsMetricUseCaseImpl(
            interactionApi = get(),
        )
    }

    dashboardBannerMetricUseCaseDefinition = {
        DashboardBannerMetricUseCaseImp(interactionApi = get())
    }

}

internal class UsEngagementAccountsBannerUseCase(
    private val request: GetEngagementBannerRequest,
    private val selectBannerApi: SelectBannerApi,
    private val metricsBannerApi: EngagementMetricsApi,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : AccountsBannerUseCase {

    override suspend fun getBanner(): AccountsBannerResponse {
        val result: Result<SelectedBannerResource, Response> = suspendCoroutine { continuation ->

            val spaceId = "${request.appName}.${request.screenName}.${request.campaignSpaceName}"
            selectBannerApi.selectBanner(
                SelectBannerApiParams.SelectBanner {
                    this.spaceId = spaceId
                    this.dimensions = request.campaignSpaceDimensions
                    this.locale = request.appLocale.toLanguageTag()
                },
            ).enqueue(
                listener = object : ResponseListener<SelectedBannerResource> {
                    override fun onSuccess(payload: SelectedBannerResource) {
                        try {
                            continuation.resume(Result.OnSuccess(payload))
                        } catch (exception: Exception) {
                            continuation.resume(Result.OnFailure(Response(exception)))
                        }
                    }

                    override fun onError(errorResponse: Response) {
                        continuation.resume(Result.OnFailure(errorResponse))
                    }
                },
            )
        }

        return when (result) {
            is Result.OnSuccess -> AccountsBannerResponse {
                this.banner =
                    String(Base64.decode(result.response.creative.content, Base64.NO_WRAP))
                this.bannerUrl = result.response.bannerUrl
                this.deepRouting = if (result.response.routing != null) AccountsDeepRouting {
                    whereTo = result.response.routing?.whereTo
                    data = result.response.routing?.data
                } else null
                this.destination =
                    result.response.utmConfig?.destination?.run {
                        AccountsBannerResponse.Destination.values().find { it.value == this }
                    } ?: AccountsBannerResponse.Destination.ENGAGEMENT
                this.engagementId = result.response.utmConfig?.engagement
                this.creativeId = result.response.utmConfig?.creative
                this.targetUrl =
                    "${Backbase.getInstance()!!.configuration.experienceConfiguration.serverURL}/api/engagement${result.response.targetUrl}"
                this.result = AccountsBannerUseCase.AccountsBannerResult.SUCCESS
            }

            else -> AccountsBannerResponse {
                this.result = AccountsBannerUseCase.AccountsBannerResult.FAILURE
            }
        }
    }

    override suspend fun captureInteraction(requestParams: AccountsBannerEngagementInteractionRequest): CallState<out Unit> =
        withContext(dispatcher) {
            val response = metricsBannerApi.clickMetric(
                EngagementMetricsApiParams.ClickMetric {
                    metricClickPostRequest = MetricClickPostRequest {
                        this.engagement = requestParams.engagementId
                        this.creative = requestParams.creativeId
                    }
                },
            ).execute()
            return@withContext if (response.isErrorResponse) {
                CallState.Error(response)
            } else {
                if (response.responseCode == HttpsURLConnection.HTTP_ACCEPTED || response.responseCode == HttpsURLConnection.HTTP_NO_CONTENT) {
                    CallState.Success(Unit)
                } else {
                    CallState.Error(response)
                }
            }
        }
}