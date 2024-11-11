package com.westerra.release.accountandtransactions.quickactions

import com.backbase.android.retail.journey.accounts_and_transactions.AccountsAndTransactionsConfiguration
import com.backbase.android.retail.journey.accounts_and_transactions.accounts.product_summary_dtos.loan.Loan
import com.backbase.android.retail.journey.accounts_and_transactions.transactions.quick_actions.ProductQuickActionButtonProvider
import com.backbase.android.retail.journey.accounts_and_transactions.transactions.quick_actions.QuickActionButtonConfiguration
import com.backbase.android.retail.journey.accounts_and_transactions.transactions.quick_actions.QuickActionButtonProviderParams
import com.westerra.release.extensions.backbase.showDMIPortal

object LoansQuickActionsProvider {
    operator fun invoke(
        ootbAccountTransactionConfig: AccountsAndTransactionsConfiguration,
    ): ProductQuickActionButtonProvider<Loan> {
        return ProductQuickActionButtonProvider { loan ->
            val results = mutableListOf<QuickActionButtonConfiguration>()
            val ootbProvider =
                ootbAccountTransactionConfig.transactions.productActionMapper
                    .loansQuickActionsProvider
            results.addAll(
                ootbProvider.provide(
                    QuickActionButtonProviderParams {
                        product = loan
                        configuration = ootbAccountTransactionConfig
                    },
                ),
            )
            if (loan.showDMIPortal()) {
                results.add(AccountPaymentQuickActionButton())
            }
            results
        }
    }
}
