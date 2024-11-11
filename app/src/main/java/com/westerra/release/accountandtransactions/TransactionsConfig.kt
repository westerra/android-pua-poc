package com.westerra.release.accountandtransactions

import com.backbase.android.retail.journey.accounts_and_transactions.AccountsAndTransactionsConfiguration
import com.backbase.android.retail.journey.accounts_and_transactions.transactions.TransactionSummaryUiDataMapper
import com.backbase.android.retail.journey.accounts_and_transactions.transactions.TransactionsConfiguration
import com.backbase.android.retail.journey.accounts_and_transactions.transactions.details.TransactionDetailsScreenConfiguration
import com.backbase.android.retail.journey.accounts_and_transactions.transactions.quick_actions.ProductActionMapper
import com.westerra.release.R
import com.westerra.release.accountandtransactions.quickactions.LoansQuickActionsProvider
import com.westerra.release.accountandtransactions.sectionproviders.TransactionSectionProvider
import com.westerra.release.extensions.toDeferredBoolean
import com.westerra.release.extensions.toDeferredDrawable
import com.westerra.release.extensions.toDeferredText

object TransactionsConfig {

    operator fun invoke(
        ootbAccountTransactionConfig: AccountsAndTransactionsConfiguration,
    ): TransactionsConfiguration {
        return TransactionsConfiguration {
            showPendingTransactionsOnTop = true
            showRunningBalance = true
            transactionDetailsScreen = TransactionDetailsScreenConfiguration {
                sectionsProvider = TransactionSectionProvider()
                summaryUiDataMapper = TransactionSummaryUiDataMapper {
                    amount = {
                        it?.transaction?.transactionAmountCurrency
                    }
                }
                showCheckImage = true.toDeferredBoolean()
            }
            pendingTransactionIcon = R.drawable.ic_pending.toDeferredDrawable()
            pendingTransactionText =
                R.string.accountsAndTransactions_transactions_labels_pending_header.toDeferredText()
            creditTransactionText = R.string.debit_transaction_text.toDeferredText()
            debitTransactionText = R.string.credit_transaction_text.toDeferredText()
            productActionMapper = ProductActionMapper {
                loansQuickActionsProvider = LoansQuickActionsProvider(
                    ootbAccountTransactionConfig = ootbAccountTransactionConfig,
                )
            }
        }
    }
}
