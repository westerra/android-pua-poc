package com.westerra.release.movemoney

import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import com.backbase.android.retail.journey.more.MenuItem
import com.backbase.android.retail.journey.more.MenuSection
import com.backbase.android.retail.journey.more.MoreConfiguration
import com.backbase.android.retail.journey.more.OnActionComplete
import com.backbase.android.retail.journey.payments.PaymentJourney
import com.backbase.android.retail.journey.payments.PaymentJourneyType
import com.westerra.release.R
import com.westerra.release.WesterraApplication
import com.westerra.release.constants.Constants.KEY_SSO_LINK
import com.westerra.release.constants.Constants.KEY_TOOLBAR_TITLE
import com.westerra.release.extensions.toDeferredColor
import com.westerra.release.extensions.toDeferredDrawable
import com.westerra.release.extensions.toDeferredText

object MoveMoneyHubConfig {

    operator fun invoke(): MoreConfiguration {
        return MoreConfiguration {
            screenTitle = R.string.move_money_title.toDeferredText()
            sections = listOf(
                getMakeATransferSection(),
                getP2PTransferSection(),
                getScheduledTransfersSection(),
                getBillPayDashboardSection(),
                getPaymentActivitySection(),
                getExternalTransfersP2PSection(),
            )
        }
    }

    private fun getMakeATransferSection(): MenuSection {
        return MenuSection {
            items = mutableListOf(
                MenuItem(
                    title = R.string.make_transfer_title.toDeferredText(),
                    icon = R.drawable.ic_make_a_transfer.toDeferredDrawable {
                        setTint(ContextCompat.getColor(it, R.color.iconForeground))
                    },
                    iconBackgroundColor = R.color.iconBackground.toDeferredColor(),
                ) {
                    OnActionComplete.NavigateTo(
                        R.id.action_mainScreen_to_paymentsJourney,
                        bundleOf(),
                    )
                },
            )
        }
    }

    private fun getP2PTransferSection(): MenuSection {
        return MenuSection {
            items = mutableListOf(
                MenuItem(
                    title = R.string.transfer_westerra_member_title.toDeferredText(),
                    icon = R.drawable.ic_make_a_transfer.toDeferredDrawable {
                        setTint(ContextCompat.getColor(it, R.color.iconForeground))
                    },
                    iconBackgroundColor = R.color.iconBackground.toDeferredColor(),
                ) {
                    OnActionComplete.NavigateTo(
                        R.id.paymentJourney,
                        bundleOf(
                            PaymentJourney.PAYMENT_JOURNEY_TYPE to PaymentJourneyType.P2P,
                        ),
                    )
                },
            )
        }
    }

    private fun getScheduledTransfersSection(): MenuSection {
        return MenuSection {
            items = mutableListOf(
                MenuItem(
                    title = R.string.transfer_activity_title.toDeferredText(),
                    icon = R.drawable.ic_pending.toDeferredDrawable {
                        setTint(ContextCompat.getColor(it, R.color.iconForeground))
                    },
                    iconBackgroundColor = R.color.iconBackground.toDeferredColor(),
                ) {
                    OnActionComplete.NavigateTo(
                        R.id.action_mainScreen_to_upcomingPaymentsJourney,
                        bundleOf(),
                    )
                },
            )
        }
    }

    private fun getBillPayDashboardSection(): MenuSection {
        return MenuSection {
            items = mutableListOf(
                MenuItem(
                    title = R.string.bill_pay_title.toDeferredText(),
                    icon = R.drawable.ic_billpay.toDeferredDrawable {
                        setTint(ContextCompat.getColor(it, R.color.iconForeground))
                    },
                    iconBackgroundColor = R.color.iconBackground.toDeferredColor(),
                ) {
                    val bundle = bundleOf(
                        KEY_SSO_LINK to "/pp/sso/eu/ShowDashboard?artifactId=",
                        KEY_TOOLBAR_TITLE to WesterraApplication.getInstance().getString(
                            R.string.bill_pay_title,
                        ),
                    )
                    OnActionComplete.NavigateTo(
                        R.id.action_bottomMenuScreen_to_billPayNavigation,
                        bundle,
                    )
                },
            )
        }
    }

    private fun getPaymentActivitySection(): MenuSection {
        return MenuSection {
            items = mutableListOf(
                MenuItem(
                    title = R.string.payment_activity_title.toDeferredText(),
                    icon = R.drawable.ic_payment_activity.toDeferredDrawable {
                        setTint(ContextCompat.getColor(it, R.color.iconForeground))
                    },
                    iconBackgroundColor = R.color.iconBackground.toDeferredColor(),
                ) {
                    val paymentFragment =
                        bundleOf(
                            KEY_SSO_LINK to "/pp/sso/eu/ViewPaymentHistory?artifactId=",
                            KEY_TOOLBAR_TITLE to WesterraApplication.getInstance().getString(
                                R.string.payment_activity_title,
                            ),
                        )
                    OnActionComplete.NavigateTo(
                        R.id.action_bottomMenuScreen_to_billPayNavigation,
                        paymentFragment,
                    )
                },
            )
        }
    }

    private fun getExternalTransfersP2PSection(): MenuSection {
        return MenuSection {
            items = mutableListOf(
                MenuItem(
                    title = R.string.external_transfer_pay_title.toDeferredText(),
                    icon = R.drawable.ic_monetization.toDeferredDrawable {
                        setTint(ContextCompat.getColor(it, R.color.iconForeground))
                    },
                    iconBackgroundColor = R.color.iconBackground.toDeferredColor(),
                ) {
                    val p2p = bundleOf(
                        KEY_SSO_LINK to "/pp/sso/eu/SendMoneyDashboard?artifactId=",
                        KEY_TOOLBAR_TITLE to WesterraApplication.getInstance().getString(
                            R.string.external_transfer_pay_title,
                        ),
                    )
                    OnActionComplete.NavigateTo(
                        R.id.action_bottomMenuScreen_to_billPayNavigation,
                        p2p,
                    )
                },
            )
        }
    }
}
