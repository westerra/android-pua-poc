package com.westerra.release

import android.app.Application
import com.backbase.android.client.contactmanagerclient2.api.ContactsApi
import com.backbase.android.client.gen2.accesscontrolclient3.api.UserContextApi
import com.backbase.android.client.gen2.accesscontrolclient3.api.UsersApi
import com.backbase.android.client.gen2.accountstatementsclient2.api.AccountStatementApi
import com.backbase.android.client.gen2.addressautocompleteclient1.api.AddressAutocompleteApi
import com.backbase.android.client.gen2.arrangementclient2.api.ArrangementViewsApi
import com.backbase.android.client.gen2.arrangementclient2.api.ArrangementsApi
import com.backbase.android.client.gen2.arrangementclient2.api.ProductSummaryApi
import com.backbase.android.client.gen2.beneficiaryvalidationclient1.api.BeneficiaryValidationApi
import com.backbase.android.client.gen2.billpayintegratorclient2.api.BillPaySsoApi
import com.backbase.android.client.gen2.budgetsclient3.api.BudgetsApi
import com.backbase.android.client.gen2.cardsclient2.api.CardProductsApi
import com.backbase.android.client.gen2.cardsclient2.api.CardsApi
import com.backbase.android.client.gen2.cardsclient2.api.TravelNoticesApi
import com.backbase.android.client.gen2.cardswalletclient1.api.WalletApi
import com.backbase.android.client.gen2.engagementclient1.api.EngagementMetricsApi
import com.backbase.android.client.gen2.engagementclient1.api.NotificationPreferenceApi
import com.backbase.android.client.gen2.engagementclient1.api.SelectBannerApi
import com.backbase.android.client.gen2.externalaccountaggregatorclient1.api.ExternalAccountAggregationClientApi
import com.backbase.android.client.gen2.financialinstitutionmanagerclient1.api.FinancialInstitutionManagerClientApi
import com.backbase.android.client.gen2.loansclient1.api.LoansApi
import com.backbase.android.client.gen2.messageclient5.api.MessagecenterApi
import com.backbase.android.client.gen2.metricclient1.api.InteractionApi
import com.backbase.android.client.gen2.paymentordera2aclient1.api.A2aClientApi
import com.backbase.android.client.gen2.paymentorderv2client2.api.PaymentOrdersApi as PaymentOrdersV2Api
import com.backbase.android.client.gen2.placemanagerclient2.api.PlacesApi
import com.backbase.android.client.gen2.remotedepositcapturerclient2.api.RemoteDepositCapturerClientApi
import com.backbase.android.client.gen2.rtcclient1.api.AccessApi
import com.backbase.android.client.gen2.rtcclient1.api.ConversationsApi
import com.backbase.android.client.gen2.rtcclient1.api.QueuesApi
import com.backbase.android.client.gen2.savvymoneycreditscorerclient1.api.SavvyMoneyCreditScorerApi
import com.backbase.android.client.gen2.transactioncategorycollectorclient2.api.CategoriesApi
import com.backbase.android.client.gen2.transactionsclient2.api.TransactionClientApi as TransactionV2ClientApi
import com.backbase.android.client.notificationclient2.api.NotificationsApi
import com.backbase.android.client.paymentorderclient2.api.PaymentOrdersApi
import com.backbase.android.client.pocketsclient2.api.PocketTailorClientApi
import com.backbase.android.client.usermanagerclient2.api.IdentityManagementApi
import com.backbase.android.client.usermanagerclient2.api.UserProfileManagementApi
import com.backbase.android.dbs.dataproviders.NetworkDBSDataProvider
import com.backbase.android.design.address.results.AddressSearchResultsProvider
import com.backbase.android.identity.journey.userprofile.UserProfileConfiguration
import com.backbase.android.identity.journey.userprofile.UserProfileJourneyScope
import com.backbase.android.identity.journey.userprofile.address.autocomplete.UserProfileAddressSearchResultsProvider
import com.backbase.android.retail.journey.accounts_and_transactions.AccountsAndTransactionsJourneyScope
import com.backbase.android.retail.journey.accountstatements.AccountStatementsJourneyScope
import com.backbase.android.retail.journey.billPaySso.BillPayIntegratorSsoJourneyScope
import com.backbase.android.retail.journey.cardsmanagement.CardsManagementJourneyScope
import com.backbase.android.retail.journey.contacts.ContactsJourneyScope
import com.backbase.android.retail.journey.credit_score.CreditScoreJourneyScope
import com.backbase.android.retail.journey.financialinsights.FinancialInsightsJourneyScope
import com.backbase.android.retail.journey.payments.PaymentJourneyScope
import com.backbase.android.retail.journey.payments.upcoming.UpcomingPaymentsJourneyScope
import com.backbase.android.retail.journey.places.PlacesJourneyScope
import com.backbase.android.retail.journey.pockets.PocketsJourneyScope
import com.backbase.android.retail.journey.rdc.RdcJourneyScope
import com.backbase.android.retail.journey.rdh.RemoteDepositHistoryJourneyScope
import com.backbase.android.retail.journey.rtc.RTCJourneyScope
import com.backbase.engagementchannels.messages.MessagesJourneyScope
import com.backbase.engagementchannels.notifications.NotificationsJourneyScope
import com.westerra.release.QuickActionQualifiers.NOTIFICATION_CLIENT_2
import com.westerra.release.QuickActionQualifiers.NOTIFICATION_CLIENT_3
import java.net.URI
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.ModuleDeclaration
import org.koin.dsl.module
import com.backbase.android.client.gen2.notificationclient3.api.NotificationsApi as NotificationsV3ClientApi


/**
 * **Note**: Please arrange alphabetically by `scope` or `dependency` name, also maintain definition grouping as per
 * `single`, `factory` and `scope`.
 *
 * @param additionalDependencies [ModuleDeclaration] to provide any additional dependencies.
 * @return Koin [module] containing all the client and related dependencies.
 */
@Suppress("FunctionName")
fun WesterraClientDefinitions(additionalDependencies: ModuleDeclaration = {}) = module {
    WesterraAdditionalDependencies()(this)

    //region Single dependencies
    single {
        val serverUri = URI("${apiRoot()}/access-control")
        UsersApi(
            context = get<Application>(),
            moshi = get(),
            parser = get(),
            serverUri = serverUri,
            provider = get(),
            backbase = get(),
        )
    }

    single {
        val uri = URI("${apiRoot()}/access-control")
        UserContextApi(
            context = get<Application>(),
            moshi = get(),
            parser = get(),
            serverUri = uri,
            provider = get(),
            backbase = get(),
        )
    }

    single(qualifier = NOTIFICATION_CLIENT_2)  {
        val serverUri = URI("${apiRoot()}/notifications-service")
        NotificationsApi(
            context = get<Application>(),
            moshi = get(),
            parser = get(),
            serverUri = serverUri,
            provider = get(),
            backbase = get(),
        )
    }

    single(qualifier = NOTIFICATION_CLIENT_3) {
        val serverUri = URI("${apiRoot()}/notifications-service")
        NotificationsV3ClientApi(
            context = get<Application>(),
            moshi = get(),
            parser = get(),
            serverUri = serverUri,
            provider = get(),
            backbase = get()
        )
    }

    //endregion

    //region Scoped Client Dependencies - please maintain alphabetical order by scope name
    scope<AccountsAndTransactionsJourneyScope> {
        scoped {
            val serverUri = URI("${apiRoot()}/engagement")
            SelectBannerApi(
                context = get<Application>(),
                moshi = get(),
                parser = get(),
                serverUri = serverUri,
                provider = get(),
                backbase = get(),
            )
        }

        scoped {
            val serverUri = URI("${apiRoot()}/engagement")
            EngagementMetricsApi(
                context = get<Application>(),
                moshi = get(),
                parser = get(),
                serverUri = serverUri,
                provider = get(),
                backbase = get()
            )
        }

        scoped {
            val serverUri = URI("${apiRoot()}/metric")
            InteractionApi(
                context = get<Application>(),
                moshi = get(),
                parser = get(),
                serverUri = serverUri,
                provider = get(),
                backbase = get()
            )
        }

        scoped {
            val serverUri = URI("${apiRoot()}/transaction-manager")
            TransactionV2ClientApi(
                context = get<Application>(),
                moshi = get(),
                parser = get(),
                serverUri = serverUri,
                provider = get(),
                backbase = get(),
            )
        }

        scoped {
            val serverUri = URI("${apiRoot()}/arrangement-manager")
            ProductSummaryApi(
                context = get<Application>(),
                moshi = get(),
                parser = get(),
                backbase = get(),
                provider = get(),
                serverUri = serverUri,
            )
        }

        scoped {
            val serverUri = URI("${apiRoot()}/arrangement-manager")
            ArrangementsApi(
                context = get<Application>(),
                moshi = get(),
                parser = get(),
                backbase = get(),
                provider = get(),
                serverUri = serverUri,
            )
        }

        scoped {
            val serverUri = URI("${apiRoot()}/financial-institution-manager")
            FinancialInstitutionManagerClientApi(
                context = get<Application>(),
                moshi = get(),
                parser = get(),
                backbase = get(),
                provider = get(),
                serverUri = serverUri,
            )
        }

        scoped {
            val uri = URI("${apiRoot()}/loan")
            LoansApi(
                context = androidContext(),
                moshi = get(),
                parser = get(),
                backbase = get(),
                serverUri = uri,
            )
        }

        scoped {
            val uri = URI("${apiRoot()}/external-account-aggregator")
            ExternalAccountAggregationClientApi(
                context = androidContext(),
                moshi = get(),
                parser = get(),
                backbase = get(),
                serverUri = uri,
            )
        }

        scoped {
            val uri = URI("${apiRoot()}/arrangement-manager")
            ArrangementViewsApi(
                context = androidContext(),
                moshi = get(),
                parser = get(),
                backbase = get(),
                serverUri = uri,
            )
        }
    }

    scope<AccountStatementsJourneyScope> {
        scoped {
            val serverUri = URI("${apiRoot()}/account-statement")
            AccountStatementApi(
                context = get<Application>(),
                moshi = get(),
                parser = get(),
                serverUri = serverUri,
                provider = get(),
                backbase = get(),
            )
        }
    }

    scope<BillPayIntegratorSsoJourneyScope> {
        scoped {
            BillPaySsoApi(
                context = get<Application>(),
                moshi = get(),
                parser = get(),
                serverUri = URI.create("${apiRoot()}/billpay-integrator"),
                provider = get(),
                backbase = get(),
            )
        }
    }

    scope<CardsManagementJourneyScope> {
        scoped {
            val serverUri = URI("${apiRoot()}/cards-presentation-service")
            CardsApi(
                context = get<Application>(),
                moshi = get(),
                parser = get(),
                serverUri = serverUri,
                backbase = get(),
                provider = NetworkDBSDataProvider(androidContext()),
            )
        }

        scoped {
            val serverUri = URI("${apiRoot()}/user-manager")
            UserProfileManagementApi(
                context = get<Application>(),
                moshi = get(),
                parser = get(),
                serverUri = serverUri,
                backbase = get(),
                provider = NetworkDBSDataProvider(androidContext()),
            )
        }

        scoped {
            val serverUri = URI("${apiRoot()}/cards-presentation-service")
            TravelNoticesApi(
                context = get<Application>(),
                moshi = get(),
                parser = get(),
                serverUri = serverUri,
                backbase = get(),
                provider = NetworkDBSDataProvider(androidContext()),
            )
        }

        scoped {
            WalletApi(
                context = get<Application>(),
                moshi = get(),
                parser = get(),
                serverUri = URI("${apiRoot()}/card-manager"),
                provider = get()
            )
        }

        scoped {
            val serverUri = URI("${apiRoot()}/cards-presentation-service")
            CardProductsApi(
                context = get<Application>(),
                moshi = get(),
                parser = get(),
                serverUri = serverUri,
                provider = get()
            )
        }
    }

    scope<CreditScoreJourneyScope> {
        scoped {
            val serverUri = URI("${apiRoot()}/savvy-money-credit-scorer")
            SavvyMoneyCreditScorerApi(
                context = get<Application>(),
                moshi = get(),
                parser = get(),
                serverUri = serverUri,
                provider = get(),
                backbase = get(),
            )
        }
    }

    scope<FinancialInsightsJourneyScope> {
        scoped {
            val serverUri = URI("${apiRoot()}/income-expense-analyzer")

            com.backbase.android.client.gen2.incomeexpenseanalyzerclient1.api.CategoryTotalsApi(
                context = get<Application>(),
                moshi = get(),
                parser = get(),
                serverUri = serverUri,
                provider = get(),
                backbase = get()
            )
        }

        scoped {
            val serverUri = URI("${apiRoot()}/transaction-category-collector")
            CategoriesApi(
                context = get<Application>(),
                moshi = get(),
                parser = get(),
                serverUri = serverUri,
                provider = get(),
                backbase = get()
            )
        }

        scoped {
            val serverUri = URI("${apiRoot()}/budget-planner")
            BudgetsApi(
                context = get<Application>(),
                moshi = get(),
                parser = get(),
                serverUri = serverUri,
                provider = get(),
                backbase = get()
            )
        }
    }


    scope<ContactsJourneyScope> {
        scoped {
            ContactsApi(
                context = get<Application>(),
                moshi = get(),
                parser = get(),
                serverUri = URI.create("${apiRoot()}/contact-manager"),
                provider = get(),
                backbase = get(),
            )
        }
    }

    scope<MessagesJourneyScope> {
        scoped {
            val serverUri = URI("${apiRoot()}/messages-service")

            MessagecenterApi(
                context = get<Application>(),
                moshi = get(),
                parser = get(),
                serverUri = serverUri,
                provider = get(),
                backbase = get(),
            )
        }
    }

    scope<NotificationsJourneyScope> {
        scoped {
            val serverUri = URI("${apiRoot()}/engagement")
            NotificationPreferenceApi(
                context = get<Application>(),
                moshi = get(),
                parser = get(),
                serverUri = serverUri,
                provider = get(),
                backbase = get(),
            )
        }

//        scoped {
//            val serverUri = URI("${apiRoot()}/action")
//
//            ActionRecipesApi(
//                context = get<Application>(),
//                moshi = get(),
//                parser = get(),
//                serverUri = serverUri,
//                provider = get(),
//                backbase = get(),
//            )
//        }

        scoped {
            val serverUri = URI("${apiRoot()}/arrangement-manager")
            com.backbase.android.client.arrangementclient2.api.ProductSummaryApi(
                context = get<Application>(),
                moshi = get(),
                parser = get(),
                serverUri = serverUri,
                provider = get(),
                backbase = get(),
            )
        }

        scoped {
            val serverUri = URI("${apiRoot()}/arrangement-manager")
            ProductSummaryApi(
                context = get<Application>(),
                moshi = get(),
                parser = get(),
                backbase = get(),
                provider = get(),
                serverUri = serverUri,
            )
        }
    }

    scope<PaymentJourneyScope> {
        scoped {
            val uri = URI("${apiRoot()}/payment-order-service")
            PaymentOrdersV2Api(
                context = get<Application>(),
                moshi = get(),
                parser = get(),
                serverUri = uri,
                backbase = get()
            )
        }

//        scoped {
//            val networkDBSDataProvider = NetworkDBSDataProvider(get<Application>())
//            val serverUri = URI.create("${apiRoot()}/payment-order-service")
//            com.backbase.android.client.gen2.paymentorderclient2.api.PaymentOrdersApi(
//                context = get<Application>(),
//                moshi = get(),
//                parser = get(),
//                serverUri = serverUri,
//                provider = networkDBSDataProvider,
//                backbase = get(),
//            )
//        }

        scoped {
            val networkDBSDataProvider = NetworkDBSDataProvider(get<Application>())
            val serverUri = URI.create("${apiRoot()}/arrangement-manager")
            ProductSummaryApi(
                context = get<Application>(),
                moshi = get(),
                parser = get(),
                serverUri = serverUri,
                provider = networkDBSDataProvider,
                backbase = get(),
            )
        }

        scoped {
            val networkDBSDataProvider = NetworkDBSDataProvider(get<Application>())
            val serverUri = URI.create("${apiRoot()}/payment-order-a2a")
            A2aClientApi(
                context = get<Application>(),
                moshi = get(),
                parser = get(),
                serverUri = serverUri,
                provider = networkDBSDataProvider,
                backbase = get()
            )
        }

        scoped {
            com.backbase.android.client.gen2.contactmanagerclient2.api.ContactsApi(
                context = get<Application>(),
                moshi = get(),
                parser = get(),
                serverUri = URI.create("${apiRoot()}/contact-manager"),
                provider = get(),
                backbase = get(),
            )
        }

        scoped {
            val networkDBSDataProvider = NetworkDBSDataProvider(get<Application>())
            val serverUri = URI.create("${apiRoot()}/arrangement-manager")
            ArrangementsApi(
                context = get<Application>(),
                moshi = get(),
                parser = get(),
                serverUri = serverUri,
                provider = networkDBSDataProvider,
                backbase = get(),
            )
        }

        scoped {
            val serverUri = URI("${apiRoot()}/beneficiary-validation-service")
            BeneficiaryValidationApi(
                context = get<Application>(),
                moshi = get(),
                parser = get(),
                serverUri = serverUri,
                provider = get(),
                backbase = get()
            )
        }
    }

    scope<PocketsJourneyScope> {
        scoped {
            val uri = URI("${apiRoot()}/pocket-tailor")
            PocketTailorClientApi(
                context = get<Application>(),
                moshi = get(),
                parser = get(),
                serverUri = uri,
                backbase = get(),
            )
        }

        scoped {
            val uri = URI("${apiRoot()}/payment-order-service")
            PaymentOrdersV2Api(
                context = get<Application>(),
                moshi = get(),
                parser = get(),
                serverUri = uri,
                backbase = get()
            )
        }
        scoped {
            val serverUri = URI("${apiRoot()}/arrangement-manager")
            ProductSummaryApi(
                context = get<Application>(),
                moshi = get(),
                parser = get(),
                backbase = get(),
                serverUri = serverUri,
            )
        }

        scoped {
            val uri = URI("${apiRoot()}/transaction-manager")
            TransactionV2ClientApi(
                context = get<Application>(),
                moshi = get(),
                parser = get(),
                serverUri = uri,
                backbase = get(),
            )
        }

        scoped {
            val serverUri = URI("${apiRoot()}/arrangement-manager")
            ArrangementsApi(
                context = get<Application>(),
                moshi = get(),
                parser = get(),
                backbase = get(),
                provider = get(),
                serverUri = serverUri,
            )
        }
    }

    scope<RdcJourneyScope> {
        scoped {
            val serverUri = URI("${apiRoot()}/remote-deposit-capturer")
            RemoteDepositCapturerClientApi(
                context = get<Application>(),
                moshi = get(),
                parser = get(),
                serverUri = serverUri,
                provider = get(),
                backbase = get(),
            )
        }

        scoped {
            val serverUri = URI("${apiRoot()}/arrangement-manager")
            ProductSummaryApi(
                context = get<Application>(),
                moshi = get(),
                parser = get(),
                serverUri = serverUri,
                provider = get(),
                backbase = get(),
            )
        }
    }

    scope<UpcomingPaymentsJourneyScope> {
        scoped {
            val serverUri = URI("${apiRoot()}/payment-order-service")
            PaymentOrdersV2Api(
                context = get<Application>(),
                moshi = get(),
                parser = get(),
                serverUri = serverUri,
                provider = get(),
                backbase = get(),
            )
        }
    }

    scope<UserProfileJourneyScope> {
//        scoped {
//            UserProfileAddressSearchResultsProvider(
//                get<UserProfileConfiguration>().addAddressScreen.addressAutocompleteConfiguration,
//                get(),
//                get<Application>(),
//            )
//        } bind AddressSearchResultsProvider::class

        scoped {
            val serverUri = URI("${apiRoot()}/user-manager")
            val networkDBSDataProvider = NetworkDBSDataProvider(get<Application>())

            UserProfileManagementApi(
                context = get<Application>(),
                moshi = get(),
                parser = get(),
                serverUri = serverUri,
                provider = networkDBSDataProvider,
                backbase = get(),
            )
        }

        scoped {
            val serverUri = URI("${apiRoot()}/address-autocomplete")
            val networkDBSDataProvider = NetworkDBSDataProvider(get<Application>())

            AddressAutocompleteApi(
                context = get<Application>(),
                moshi = get(),
                parser = get(),
                serverUri = serverUri,
                provider = networkDBSDataProvider,
                backbase = get(),
            )
        }

        scoped {
            val serverUri = URI("${apiRoot()}/user-manager")
            val networkDBSDataProvider = NetworkDBSDataProvider(get<Application>())

            IdentityManagementApi(
                context = get<Application>(),
                moshi = get(),
                parser = get(),
                serverUri = serverUri,
                provider = networkDBSDataProvider,
                backbase = get(),
            )
        }
    }

    scope<PlacesJourneyScope> {
        scoped {
            PlacesApi(
                context = get<Application>(),
                moshi = get(),
                parser = get(),
                serverUri = URI.create("${apiRoot()}/places-presentation-service"),
                provider = get(),
                backbase = get(),
            )
        }
    }

    scope<RemoteDepositHistoryJourneyScope> {
        scoped {
            val serverUri = URI("${apiRoot()}/remote-deposit-capturer")
            RemoteDepositCapturerClientApi(
                context = get<Application>(),
                moshi = get(),
                parser = get(),
                serverUri = serverUri,
                provider = get(),
                backbase = get(),
            )
        }
    }

    scope<RTCJourneyScope> {
        scoped {
            AccessApi(
                context = get<Application>(),
                moshi = get(),
                parser = get(),
                serverUri = URI.create("${apiRoot()}/rtc"),
                provider = get(),
                backbase = get(),
            )
        }
        scoped {
            ConversationsApi(
                context = get<Application>(),
                moshi = get(),
                parser = get(),
                serverUri = URI.create("${apiRoot()}/rtc"),
                provider = get(),
                backbase = get(),
            )
        }
        scoped {
            QueuesApi(
                context = get<Application>(),
                moshi = get(),
                parser = get(),
                serverUri = URI.create("${apiRoot()}/rtc"),
                provider = get(),
                backbase = get(),
            )
        }
    }

    single {
        val serverUri = URI("${apiRoot()}/engagement")
        SelectBannerApi(
            context = get<Application>(),
            moshi = get(),
            parser = get(),
            serverUri = serverUri,
            provider = get(),
            backbase = get()
        )
    }
    single {
        val serverUri = URI("${apiRoot()}/engagement")
        EngagementMetricsApi(
            context = get<Application>(),
            moshi = get(),
            parser = get(),
            serverUri = serverUri,
            provider = get(),
            backbase = get()
        )
    }

    single {
        val serverUri = URI("${apiRoot()}/metric")
        InteractionApi(
            context = get<Application>(),
            moshi = get(),
            parser = get(),
            serverUri = serverUri,
            provider = get(),
            backbase = get()
        )
    }
    //endregion

    additionalDependencies(this)
}
