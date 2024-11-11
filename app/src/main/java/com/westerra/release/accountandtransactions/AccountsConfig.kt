package com.westerra.release.accountandtransactions

import android.content.Context
import com.backbase.android.retail.journey.accounts_and_transactions.accounts.AccountGrouping
import com.backbase.android.retail.journey.accounts_and_transactions.accounts.AccountType
import com.backbase.android.retail.journey.accounts_and_transactions.accounts.AccountsScreenConfiguration
import com.backbase.android.retail.journey.accounts_and_transactions.accounts.layout_provider.CreditCardRowItemProvider
import com.backbase.android.retail.journey.accounts_and_transactions.accounts.layout_provider.CurrentAccountRowItemProvider
import com.backbase.android.retail.journey.accounts_and_transactions.accounts.layout_provider.GeneralAccountRowItemProvider
import com.backbase.android.retail.journey.accounts_and_transactions.accounts.layout_provider.LoansRowItemProvider
import com.backbase.android.retail.journey.accounts_and_transactions.accounts.layout_provider.SavingsAccountRowItemProvider
import com.westerra.release.R
import com.westerra.release.accountandtransactions.accountrowitems.CreditCardRowItem
import com.westerra.release.accountandtransactions.accountrowitems.CurrentAccountRowItem
import com.westerra.release.accountandtransactions.accountrowitems.GeneralAccountRowItem
import com.westerra.release.accountandtransactions.accountrowitems.LoansAccountRowItem
import com.westerra.release.accountandtransactions.accountrowitems.SavingsAccountRowItem
import com.westerra.release.accountandtransactions.sectionproviders.CheckingSectionProvider
import com.westerra.release.accountandtransactions.sectionproviders.CreditSectionProvider
import com.westerra.release.accountandtransactions.sectionproviders.GeneralSectionProvider
import com.westerra.release.accountandtransactions.sectionproviders.LoanSectionProvider
import com.westerra.release.accountandtransactions.sectionproviders.SavingsSectionProvider
import com.westerra.release.accountandtransactions.toolbar.ManageAccountMenuItem
import com.westerra.release.accountandtransactions.toolbar.NotificationsMenuItem
import com.westerra.release.extensions.toDeferredDrawable
import com.westerra.release.extensions.toDeferredText

object AccountsConfig {
    operator fun invoke(context: Context): AccountsScreenConfiguration {
        return AccountsScreenConfiguration {
            screenTitle = R.string.my_accounts_title.toDeferredText()
            accountDetailsScreenTitle = R.string.account_details_title.toDeferredText()
            toolbarMenuItems =
                mutableListOf(
                    ManageAccountMenuItem(),
                    NotificationsMenuItem(),
                )
            currentAccountDetailsSectionsProvider = CheckingSectionProvider()
            creditCardDetailsSectionsProvider = CreditSectionProvider()
            savingsAccountDetailsSectionsProvider = SavingsSectionProvider()
            loanDetailsSectionsProvider = LoanSectionProvider(context = context)
            generalAccountDetailsSectionsProvider = GeneralSectionProvider()
            grouping = AccountGrouping.AccountType
            displayedAccounts = AccountType.values().toList()
            currentAccountTitle = R.string.current_account_title.toDeferredText()
            currentAccountIcon = R.drawable.nothing.toDeferredDrawable()
            creditCardTitle = R.string.credit_card_title.toDeferredText()
            creditCardIcon = R.drawable.nothing.toDeferredDrawable()
            debitCardTitle = R.string.debit_card_title.toDeferredText()
            debitCardIcon = R.drawable.nothing.toDeferredDrawable()
            investmentAccountTitle = R.string.investment_account_title.toDeferredText()
            investmentAccountIcon = R.drawable.nothing.toDeferredDrawable()
            loanAccountTitle = R.string.loan_account_title.toDeferredText()
            loanAccountIcon = R.drawable.nothing.toDeferredDrawable()
            savingsAccountTitle = R.string.savings_account_title.toDeferredText()
            savingsAccountIcon = R.drawable.nothing.toDeferredDrawable()
            termDepositTitle = R.string.term_deposit_title.toDeferredText()
            termDepositIcon = R.drawable.nothing.toDeferredDrawable()
            generalAccountTitle = R.string.general_account_title.toDeferredText()
            accountNameTruncated = false // to show full account name

            // This section formats the display of the list of accounts
            currentAccountRowItemProvider =
                CurrentAccountRowItemProvider { dto, _ ->
                    CurrentAccountRowItem(dto = dto)
                }
            creditCardRowItemProvider =
                CreditCardRowItemProvider { dto, _ ->
                    CreditCardRowItem(dto = dto)
                }
            savingsAccountRowItemProvider =
                SavingsAccountRowItemProvider { dto, _ ->
                    SavingsAccountRowItem(dto = dto)
                }
            generalAccountRowItemProvider =
                GeneralAccountRowItemProvider { dto, _ ->
                    GeneralAccountRowItem(dto = dto)
                }
            loansRowItemProvider =
                LoansRowItemProvider { dto, _ ->
                    LoansAccountRowItem(dto = dto)
                }
        }
    }
}
