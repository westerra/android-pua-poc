package com.westerra.release

import android.app.Activity
import androidx.navigation.NavController
import com.backbase.android.retail.journey.payments.AddContactNavigationAction
import com.backbase.android.retail.journey.payments.PaymentCompleteNavigationAction
import com.backbase.android.retail.journey.payments.PaymentJourneyScope
import com.backbase.android.retail.journey.payments.PaymentJourneyType
import com.backbase.android.retail.journey.payments.model.Status
import com.backbase.android.retail.journey.rdc.ExitNavigateAction
import com.backbase.android.retail.journey.rdc.RdcJourneyScope
import com.google.android.material.snackbar.Snackbar
import com.westerra.release.rdc.RDCExitNavigationAction
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

/**
 * Convenience method that is invoked after all the dependencies are initialized in Koin from
 * [WesterraApplication].
 */
internal fun initializeKoinModulesWithDependencies(
    navController: NavController,
    activity: Activity,
) {
    loadKoinModules(
        module = module {
            // when clicked on '+' icon in 'transfer to westerra member' list of contacts screen
            // in move money menu
            scope<PaymentJourneyScope> {
                scoped(
                    qualifier = PaymentJourneyType.P2P,
                    override = true,
                ) {
                    AddContactNavigationAction { _, _, _ ->
                        Snackbar.make(
                            activity.window.decorView.rootView,
                            R.string.add_contacts_payment_journey_message,
                            Snackbar.LENGTH_LONG,
                        ).show()
                    }
                }

                // For Internal Transfer
                scoped(override = true) {
                    getPaymentCompleteNavigationAction(navController = navController)
                }

                // for Contacts Transfer
                scoped(
                    qualifier = PaymentJourneyType.P2P,
                    override = true,
                ) {
                    getPaymentCompleteNavigationAction(navController = navController)
                }
            }

            scope<RdcJourneyScope> {
                // exit navigation from "deposit a check"(RDC) journey
                scoped<ExitNavigateAction>(override = true) {
                    RDCExitNavigationAction(navController = navController)
                }
            }
        },
    )
}

// back navigation mechanism in case of payment failure
// applies for both "transfer to member" and "transfer to westerra member" flows
private fun getPaymentCompleteNavigationAction(
    navController: NavController,
): PaymentCompleteNavigationAction {
    return PaymentCompleteNavigationAction {
            paymentCompleteNavController, _, paymentOrderResponse ->
        val failurePaymentStatus =
            listOf(
                Status.Rejected,
                Status.Cancelled,
                Status.CancellationPending,
                Status.ConfirmationPending,
                Status.ConfirmationDeclined,
            )
        if (failurePaymentStatus.contains(paymentOrderResponse.status)) {
            paymentCompleteNavController.navigateUp()
        } else {
            navController.popBackStack()
        }
    }
}
