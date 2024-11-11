package com.westerra.release.accountandtransactions.quickactions

import android.util.Log
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import com.backbase.android.retail.journey.accounts_and_transactions.accounts.product_summary_dtos.common.ParcelableProduct
import com.backbase.android.retail.journey.accounts_and_transactions.accounts.product_summary_dtos.loan.Loan
import com.backbase.android.retail.journey.accounts_and_transactions.transactions.quick_actions.QuickActionButtonNavigationAction
import com.backbase.android.retail.journey.accounts_and_transactions.transactions.quick_actions.QuickActionButtonNavigationActionParams
import com.westerra.release.R
import com.westerra.release.WesterraApplication
import com.westerra.release.alerts.ErrorAlert
import com.westerra.release.constants.Constants
import com.westerra.release.constants.Constants.KEY_TOOLBAR_TITLE

class AccountPaymentNavigationAction : QuickActionButtonNavigationAction {
    private val tag: String = AccountPaymentNavigationAction::class.java.simpleName
    private val resId =
        R.id.accountsTransactionsJourney_action_transactionsScreen_to_dmiSSONavigation

    override fun navigate(
        navController: NavController,
        params: QuickActionButtonNavigationActionParams,
    ) {
        super.navigate(navController, params)
        val loan = params.product as? Loan ?: return
        val args =
            bundleOf(
                Constants.KEY_INTERNAL_ARRANGEMENT_ID to loan.id,
                Constants.KEY_IS_PAYMENTS to true,
                KEY_TOOLBAR_TITLE to navController.context.getString(R.string.make_payment_title),
            )
        try {
            navController.navigate(
                resId = resId,
                args = args,
            )
        } catch (e: Exception) {
            Log.e(tag, e.message ?: "Unknown error", e)
            WesterraApplication.getInstance().getActivity() ?. let {
                ErrorAlert(activity = it).show()
            }
        }
    }

    @Deprecated("no way to add new params such as NavController without breaking change")
    override fun navigate(args: ParcelableProduct) {
        //
    }
}
