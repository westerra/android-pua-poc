package com.westerra.release.accountandtransactions.quickactions

import com.backbase.android.retail.journey.accounts_and_transactions.transactions.quick_actions.QuickActionButtonConfiguration
import com.westerra.release.R
import com.westerra.release.extensions.toDeferredDrawable
import com.westerra.release.extensions.toDeferredText

object AccountPaymentQuickActionButton {

    operator fun invoke(): QuickActionButtonConfiguration {
        return QuickActionButtonConfiguration {
            title = R.string.make_payment_title.toDeferredText()
            icon = R.drawable.accounts_and_transactions_journey_ic_baseline_attach_money
                .toDeferredDrawable()
            action = AccountPaymentNavigationAction()
        }
    }
}
