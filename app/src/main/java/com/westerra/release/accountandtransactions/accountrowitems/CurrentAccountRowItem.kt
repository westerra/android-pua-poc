package com.westerra.release.accountandtransactions.accountrowitems

import com.backbase.android.retail.journey.accounts_and_transactions.accounts.AccountRowItem
import com.backbase.android.retail.journey.accounts_and_transactions.accounts.product_summary_dtos.current_accounts.CurrentAccount
import com.westerra.release.accountandtransactions.AccountTransactionsConfig
import com.westerra.release.constants.Constants
import com.westerra.release.extensions.maskAccountNumber
import com.westerra.release.extensions.toDeferredText
import com.westerra.release.extensions.toDeferredTextAppearance

object CurrentAccountRowItem {
    operator fun invoke(dto: CurrentAccount): AccountRowItem {
        return AccountRowItem {
            accountId = dto.id
            title = dto.displayName
            subtitle = dto.BBAN?.maskAccountNumber()
            titleStyle = com.westerra.release.R.style.AccountRowTitleStyle
                .toDeferredTextAppearance()
            subtitleStyle = com.westerra.release.R.style.AccountRowSubTitleStyle
                .toDeferredTextAppearance()
            rightAccessoryText =
                dto.availableBalance?.let {
                    AccountTransactionsConfig.convertStringToCurrencyFormat(
                        it,
                        dto.currency,
                    )
                }
            rightAccessoryTextStyle =
                com.westerra.release.R.style.AccountRowRightAccessoryTextStyle
                    .toDeferredTextAppearance()
            if (dto.state?.state != Constants.STATE_ACTIVE) {
                rightAccessorySubtitle = com.westerra.release.R.string.account_closed
                    .toDeferredText()
            }
        }
    }
}
