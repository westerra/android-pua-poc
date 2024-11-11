package com.westerra.release.sso.dmi

import android.util.Log
import androidx.core.os.bundleOf
import com.backbase.android.retail.journey.accounts_and_transactions.account_details.field_editor.ExternalAction
import com.backbase.android.retail.journey.accounts_and_transactions.accounts.product_summary_dtos.loan.Loan
import com.westerra.release.R
import com.westerra.release.WesterraApplication
import com.westerra.release.alerts.ErrorAlert
import com.westerra.release.constants.Constants

object DMISSOExternalAction {
    private val TAG = DMISSOExternalAction::class.java.simpleName

    operator fun invoke(): ExternalAction {
        return ExternalAction { navController, exitArgs ->
            val loanId = (exitArgs.product as? Loan)?.id
            if (loanId?.isEmpty() != false) {
                showErrorAlert()
            } else {
                val args =
                    bundleOf(
                        Constants.KEY_INTERNAL_ARRANGEMENT_ID to loanId,
                        Constants.KEY_IS_PAYMENTS to false,
                    )
                try {
                    navController.navigate(
                        resId = R.id.accountsTransactionsJourney_action_to_dmiSSONavigation,
                        args = args,
                    )
                } catch (e: Exception) {
                    Log.e(TAG, e.message ?: "Unknown error")
                    showErrorAlert()
                }
            }
        }
    }

    private fun showErrorAlert() {
        WesterraApplication.getInstance().getActivity() ?. let {
            ErrorAlert(activity = it).show()
        }
    }
}
