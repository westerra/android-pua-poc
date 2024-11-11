package com.westerra.release.accountandtransactions.accountrowitems

import com.backbase.android.retail.journey.accounts_and_transactions.accounts.AccountRowItem
import com.backbase.android.retail.journey.accounts_and_transactions.accounts.product_summary_dtos.savings_accounts.SavingsAccount
import com.westerra.release.R
import com.westerra.release.accountandtransactions.AccountTransactionsConfig
import com.westerra.release.extensions.maskAccountNumber
import com.westerra.release.extensions.toDeferredTextAppearance

object SavingsAccountRowItem {
    operator fun invoke(dto: SavingsAccount): AccountRowItem {
        return AccountRowItem {
            accountId = dto.id
            title = dto.displayName
            subtitle = dto.BBAN?.maskAccountNumber()
            titleStyle = R.style.AccountRowTitleStyle.toDeferredTextAppearance()
            subtitleStyle = R.style.AccountRowSubTitleStyle.toDeferredTextAppearance()
            rightAccessoryText =
                dto.availableBalance?.let {
                    AccountTransactionsConfig.convertStringToCurrencyFormat(
                        it,
                        dto.currency,
                    )
                }
            rightAccessoryTextStyle =
                R.style.AccountRowRightAccessoryTextStyle.toDeferredTextAppearance()
        }
    }
}
