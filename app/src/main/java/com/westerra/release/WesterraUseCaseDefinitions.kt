package com.westerra.release

import android.app.Application
import android.graphics.Color
import com.backbase.android.client.contactmanagerclient2.api.ContactsApi
import com.backbase.android.client.gen2.addressautocompleteclient1.api.AddressAutocompleteApi
import com.backbase.android.client.gen2.arrangementclient2.api.ArrangementsApi
import com.backbase.android.client.gen2.arrangementclient2.api.ProductSummaryApi
import com.backbase.android.client.gen2.externalaccountaggregatorclient1.api.ExternalAccountAggregationClientApi
import com.backbase.android.client.gen2.financialinstitutionmanagerclient1.api.FinancialInstitutionManagerClientApi
import com.backbase.android.client.gen2.loansclient1.api.LoansApi
import com.backbase.android.client.gen2.paymentorderclient2.api.PaymentOrdersApi
import com.backbase.android.client.gen2.paymentorderv2client2.api.PaymentOrdersApi as PaymentOrdersV2Api
import com.backbase.android.client.gen2.transactionsclient2.api.TransactionClientApi
import com.backbase.android.client.paymentordera2aclient1.api.A2aClientApi
import com.backbase.android.client.usermanagerclient2.api.UserProfileManagementApi
import com.backbase.android.identity.journey.authentication.identity_auth_client_1.IdentityAuthClient1AuthenticationUseCase
import com.backbase.android.identity.journey.userprofile.autocomplete.usecase.AddressAutocompleteUserProfileUseCase
import com.backbase.android.identity.journey.userprofile.idmanagement.usecase.IdentityManagementUserProfileUseCase
import com.backbase.android.identity.journey.userprofile.usecase.UserManagerUserProfileUseCase
import com.backbase.android.retail.feature_filter.UserEntitlementFeatureFilterUseCase
import com.backbase.android.retail.feature_filter.entitlements.accesscontrol_client_2.AccessControlClient2EntitlementsUseCase
import com.backbase.android.retail.journey.accounts_and_transactions.gen_arrangements_client_2.ArrangementsUseCaseImpl
import com.backbase.android.retail.journey.accounts_and_transactions.gen_arrangements_client_2.ProductSummaryUseCaseImpl
import com.backbase.android.retail.journey.accounts_and_transactions.gen_loans_client_1.LoanDetailsUseCaseImpl
import com.backbase.android.retail.journey.accounts_and_transactions.gen_transaction_client_2.TransactionsUseCaseImpl
import com.backbase.android.retail.journey.accountstatements.gen_accountstatements_client_2.AccountStatementsUseCaseImpl
import com.backbase.android.retail.journey.app.us.UsApplicationConfiguration
import com.backbase.android.retail.journey.app.us.UsUseCaseDefinitions
import com.backbase.android.retail.journey.cardsmanagement.SimpleCallState
import com.backbase.android.retail.journey.cardsmanagement.gen_cards_client_2_travel_notice_use_case.TravelNoticeUseCaseImpl
import com.backbase.android.retail.journey.cardsmanagement.gen_cards_client_2_use_case.CardsUseCaseImpl
import com.backbase.android.retail.journey.cardsmanagement.gen_cards_client_2_use_case.ChangePinUseCaseImpl
import com.backbase.android.retail.journey.cardsmanagement.gen_usermanager_client_2_user_use_case.UserUseCaseImpl
import com.backbase.android.retail.journey.cardsmanagement.usecase.cardimages.CardImageRequestParams
import com.backbase.android.retail.journey.cardsmanagement.usecase.cardimages.CardImagesResponse
import com.backbase.android.retail.journey.cardsmanagement.usecase.cardimages.CardImagesUseCase
import com.backbase.android.retail.journey.contacts.contactmanager_client_2.GenContactManagerClient2ContactsUseCase
import com.backbase.android.retail.journey.financialinsights.gen_2_expense_analyzer_client_1_use_case.IncomeExpenseAnalyzerUseCaseImpl
import com.backbase.android.retail.journey.gen_2_arrangements_client_2_arrangements_use_case.PocketsArrangementsUseCaseImpl
import com.backbase.android.retail.journey.gen_2_pockets_transactions_client_2_use_case.TransactionsUseCaseImpl as PocketsTransactionsUseCaseImpl
import com.backbase.android.retail.journey.gen_external_account_aggregation_client.ExternalAccountAggregationUseCaseImpl
import com.backbase.android.retail.journey.gen_financial_institutions_1_use_case.FinancialInstitutionsUseCaseImpl
import com.backbase.android.retail.journey.gen_financial_institutions_1_use_case.UrlDrawableProvider
import com.backbase.android.retail.journey.gen_paymentorder_client_2_payment_service_use_case.PocketsPaymentUseCaseImpl
import com.backbase.android.retail.journey.gen_pockets_client_2_use_case.PocketsUseCaseImpl
import com.backbase.android.retail.journey.payments.gen_arrangements_client_2.ArrangementsClient2PaymentAccountsUseCase
import com.backbase.android.retail.journey.payments.gen_contactmanager_client_2.ContactManagerClient2PaymentContactsUseCase
import com.backbase.android.retail.journey.payments.gen_paymentorder_client_2.PaymentOrderClient2PaymentServiceUseCase
import com.backbase.android.retail.journey.payments.gen_paymentordera2a_client_1.PaymentOrderA2AClient1ServiceUseCase
import com.backbase.android.retail.journey.payments.upcoming.gen2_paymentorder_v2_client_2.PaymentOrderV2Client2UpcomingPaymentsServiceUseCase
import com.backbase.android.retail.journey.places.placemanager_client_2.GenPlaceManagerClient2PlacesUseCase
import com.backbase.android.retail.journey.rdc.gen_remotedepositcapturer_client_2.Rdc2ServiceUseCase
import com.backbase.android.retail.journey.rdc.gen_remotedepositcapturer_client_2.model.DeviceInfo
import com.backbase.android.retail.journey.rdh.gen2_remotedeposithistory_client2.RemoteDepositHistory2ServiceUseCase
import com.backbase.android.retail.journey.rdh.gen2_remotedeposithistory_client2.model.DeviceInfo as RdhDeviceInfo
import com.backbase.android.retail.journey.rtc.gen_client.GenClientRTCUseCaseImpl
import com.backbase.deferredresources.DeferredColor
import com.backbase.deferredresources.drawable.DeferredColorDrawable
import com.backbase.engagementchannels.gen_2_arrangements_client_2_accounts_use_case.ArrangementClient2AccountsUseCase
import com.backbase.engagementchannels.gen_2_engagements_client_1_notification_settings_usecase.GenEngagementsClient1NotificationSettingsUseCase
import com.backbase.engagementchannels.gen_2_engagements_client_1_notification_settings_usecase.NoActionsAccountsNotificationSettingsUseCase
import com.backbase.engagementchannels.messages.gen_message_client_5.GenMessageClient5MessagesUseCase
import com.backbase.engagementchannels.notifications.NotificationSettingsAPI
import com.backbase.engagementchannels.notifications.NotificationsConfiguration
import com.backbase.engagementchannels.notifications.gen_action_client_2_gen_arrangement_client_2.GenActionClient2GenArrangementClient2NotificationSettingsUseCase
import com.backbase.engagementchannels.notifications.gen_notification_client_2.GenNotificationClient2NotificationsUseCase
import com.backbase.engagementchannels.unreadnotifications.gen_notification_client_2.GenNotificationClient2UnreadNotificationsUseCase
import com.backbase.engagementchannels.unreadnotifications.gen_notification_client_2.UnreadNotificationsUseCase
import com.backbase.gen2_rtc_conversation_sdk_use_case.GenRTCConversationSDKUseCaseImpl
import com.bumptech.glide.Glide
import com.westerra.release.accountandtransactions.AccountsUnreadNotificationUseCase

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
    accountStatementsUseCaseDefinition = {
        AccountStatementsUseCaseImpl(
            client = get(),
        )
    }

    accountsUseCaseDefinition = {
        ProductSummaryUseCaseImpl(
            productSummaryClient = get<ProductSummaryApi>(),
        )
    }

    accountsUnreadNotificationUseCaseDefinition = {
        AccountsUnreadNotificationUseCase(
            unreadNotificationsUseCase = get<UnreadNotificationsUseCase>(),
            unreadNotificationsCachedTimeInSeconds = get<UsApplicationConfiguration>()
                .unreadNotificationsCachedTimeInSeconds,
        )
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
            client = get(),
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

    cardsTravelNoticeUseCaseDefinition = {
        TravelNoticeUseCaseImpl(
            client = get(),
        )
    }

    changePinUseCaseDefinition = {
        ChangePinUseCaseImpl(
            client = get(),
        )
    }

    contactsUseCaseDefinition = {
        GenContactManagerClient2ContactsUseCase(
            contactsApi = get(),
        )
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

    financialInsightsUseCaseDefinition = {
        IncomeExpenseAnalyzerUseCaseImpl(
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

    notificationAccountsUseCaseDefinition = {
        ArrangementClient2AccountsUseCase(productSummaryApi = get())
    }

    notificationPreferencesUseCaseDefinition = {
        if (getOrNull<NotificationsConfiguration>()?.notificationSettingsAPI ==
            NotificationSettingsAPI.ENGAGEMENTS
        ) {
            GenEngagementsClient1NotificationSettingsUseCase(preferencesClient = get())
        } else {
            null
        }
    }

    notificationSettingsUseCaseDefinition = {
        if (getOrNull<NotificationsConfiguration>()?.notificationSettingsAPI ==
            NotificationSettingsAPI.ENGAGEMENTS
        ) {
            NoActionsAccountsNotificationSettingsUseCase(productSummaryApi = get())
        } else {
            GenActionClient2GenArrangementClient2NotificationSettingsUseCase(
                actionRecipesApi = get(),
                productSummaryApi = get(),
            )
        }
    }

    notificationsUseCaseDefinition = {
        GenNotificationClient2NotificationsUseCase(
            notificationsApi = get(),
        )
    }

    paymentAccountsUseCaseDefinition = {
        ArrangementsClient2PaymentAccountsUseCase(
            productSummaryApiV2 = get(),
            arrangementsApi = get<ArrangementsApi>(),
        )
    }

    paymentUseCaseDefinition = {
        PaymentOrderClient2PaymentServiceUseCase(
            paymentOrdersApi = get<PaymentOrdersApi>(),
        )
    }

    paymentContactsUseCaseDefinition = {
        ContactManagerClient2PaymentContactsUseCase(
            contactsApi = get<ContactsApi>(),
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
        )
    }

    pocketsPaymentsUseCaseDefinition = {
        PocketsPaymentUseCaseImpl(
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
            notificationsApi = get(),
        )
    }
}
