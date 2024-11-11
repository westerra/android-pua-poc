package com.westerra.release

import com.backbase.android.identity.journey.authentication.identity_auth_client_1.IdentityAuthClient1AuthenticationUseCase
import com.backbase.android.retail.journey.accounts_and_transactions.banner.AccountsBannerResponse
import com.backbase.android.retail.journey.accounts_and_transactions.banner.AccountsBannerUseCase
import com.backbase.android.retail.journey.app.us.UsUseCaseDefinitions
import com.westerra.release.accountandtransactions.CustomTransactionsUseCase
import com.westerra.release.accountstatements.CustomAccountStatementUseCase
import com.westerra.release.contacts.CustomContactsUseCase
import com.westerra.release.identity.CustomAuthenticationUseCase
import com.westerra.release.payments.usecase.CustomArrangementsPaymentAccountsUseCase
import com.westerra.release.payments.usecase.CustomInternalPaymentUseCase
import com.westerra.release.payments.usecase.CustomPaymentContactsUseCase
import com.westerra.release.payments.usecase.CustomUpcomingPaymentsUseCase
import com.westerra.release.profile.usecase.CustomUserProfileUseCase

fun UsUseCaseDefinitions.Builder.westerraCustomUseCases() {
    accountStatementsUseCaseDefinition = { CustomAccountStatementUseCase(get(), get()) }
    accountsBannerUseCaseDefinition = {
        object : AccountsBannerUseCase {
            override suspend fun getBanner(): AccountsBannerResponse = AccountsBannerResponse {
                result = AccountsBannerUseCase.AccountsBannerResult.FAILURE
            }
        }
    }
    authenticationUseCaseDefinition = {
        CustomAuthenticationUseCase(get<IdentityAuthClient1AuthenticationUseCase>())
    }
    contactsUseCaseDefinition = { CustomContactsUseCase(get()) }
    paymentAccountsUseCaseDefinition = { CustomArrangementsPaymentAccountsUseCase(get()) }
    paymentContactsUseCaseDefinition = { CustomPaymentContactsUseCase(get()) }
    paymentUseCaseDefinition = { CustomInternalPaymentUseCase() }
    transactionsUseCaseDefinition = { CustomTransactionsUseCase(get(), get()) }
    upcomingPaymentsUseCaseDefinition = { CustomUpcomingPaymentsUseCase(get(), get()) }
    userProfileUseCaseDefinition = { CustomUserProfileUseCase(get()) }
}
