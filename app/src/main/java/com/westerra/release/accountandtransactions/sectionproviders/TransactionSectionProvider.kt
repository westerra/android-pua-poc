package com.westerra.release.accountandtransactions.sectionproviders

import com.backbase.android.retail.journey.accounts_and_transactions.transactions.TransactionsConfiguration
import com.backbase.android.retail.journey.accounts_and_transactions.transactions.details.TransactionDetailsGenericTextRow
import com.backbase.android.retail.journey.accounts_and_transactions.transactions.details.TransactionDetailsSection
import com.backbase.android.retail.journey.accounts_and_transactions.transactions.details.TransactionDetailsSectionsProvider
import com.backbase.android.retail.journey.accounts_and_transactions.transactions.details.TransactionDetailsTextSection
import com.backbase.android.retail.journey.accounts_and_transactions.transactions.models.TransactionItem
import com.westerra.release.R
import com.westerra.release.constants.Constants.DEFAULT_EMPTY_STRING
import com.westerra.release.extensions.toDeferredText
import com.westerra.release.extensions.toPreferredDatePattern

class TransactionSectionProvider : TransactionDetailsSectionsProvider {

    companion object {
        const val KEY_ORIGINAL_DESCRIPTION = "originalDescription"
    }

    override fun provide(
        dto: TransactionItem,
        transactionsConfiguration: TransactionsConfiguration,
    ): List<TransactionDetailsSection> {
        return mutableListOf(
            TransactionDetailsTextSection {
                title = DEFAULT_EMPTY_STRING.toDeferredText()
                rows = listOf(
                    TransactionDetailsGenericTextRow {
                        title = R.string.type_title.toDeferredText()
                        value = dto.type.toDeferredText()
                    },
                    TransactionDetailsGenericTextRow {
                        title = R.string.date_created_title.toDeferredText()
                        value = dto.valueDate.toPreferredDatePattern().toDeferredText()
                    },
                    TransactionDetailsGenericTextRow {
                        title = R.string.description_title.toDeferredText()
                        value = dto.description?.toDeferredText()
                    },
                    TransactionDetailsGenericTextRow {
                        title = R.string.statement_description_title.toDeferredText()
                        value = (
                            dto.additions?.get(KEY_ORIGINAL_DESCRIPTION) ?: dto.description
                                ?: ""
                            ).toDeferredText()
                    },
                )
            },
        )
    }
}
