package com.westerra.release.accountandtransactions

import android.content.Context
import android.icu.text.NumberFormat
import android.icu.util.Currency
import com.backbase.android.retail.journey.accounts_and_transactions.AccountsAndTransactionsConfiguration
import com.westerra.release.R
import com.westerra.release.constants.Constants.DEFAULT_ZERO_STRING
import com.westerra.release.extensions.toDeferredText
import java.util.Locale

object AccountTransactionsConfig {

    operator fun invoke(context: Context): AccountsAndTransactionsConfiguration {
        return AccountsAndTransactionsConfiguration {
            val ootbAccountTransactionConfig = this.build()
            accountsScreen = AccountsConfig(context = context)
            transactions = TransactionsConfig(
                ootbAccountTransactionConfig = ootbAccountTransactionConfig,
            )
            notConnectedTitle = R.string.not_connected_title.toDeferredText()
            retryButtonTitle = R.string.try_again.toDeferredText()
        }
    }

    fun convertStringToCurrencyFormat(amountString: String?, currency: String?): String =
        NumberFormat.getCurrencyInstance(Locale.US).run {
            this.currency = Currency.getInstance(currency)
            format(
                (amountString ?: DEFAULT_ZERO_STRING).toDouble(),
            )
        }
}
