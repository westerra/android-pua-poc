package com.westerra.release.bottommenu

import androidx.core.os.bundleOf
import com.backbase.android.retail.journey.app.common.menu.BottomMenuConfiguration
import com.backbase.android.retail.journey.app.common.menu.BottomMenuItem
import com.backbase.android.retail.journey.app.us.DefaultUsAccountsBottomMenuItem
import com.backbase.android.retail.journey.app.us.DefaultUsDashboardBottomMenuItem
import com.backbase.android.retail.journey.app.us.DefaultUsMoreBottomMenuItem
import com.backbase.android.retail.journey.app.us.DefaultUsMoveMoneyBottomMenuItem
import com.backbase.android.retail.journey.app.us.DefaultUsTabbedHeaderBottomMenuConfiguration
import com.backbase.android.retail.journey.app.us.UsApplicationFeatureFlag
import com.backbase.android.retail.journey.app.us.hasFeatureFlag
import com.backbase.android.retail.journey.more.MoreJourney
import com.backbase.android.retail.journey.more.MoreMenuInstanceId
import com.westerra.release.R
import com.westerra.release.constants.Constants.MENU_INSTANCE_ID_DEPOSIT_A_CHECK
import com.westerra.release.constants.Constants.MENU_INSTANCE_ID_MOVE_MONEY
import com.westerra.release.extensions.toDeferredDrawable
import com.westerra.release.extensions.toDeferredText

object BottomMenuConfig {

    operator fun invoke(): BottomMenuConfiguration {

        return if (hasFeatureFlag<UsApplicationFeatureFlag.UseTabbedHeaderFeatureFlag>()) {
            DefaultUsTabbedHeaderBottomMenuConfiguration()
        } else if (hasFeatureFlag<UsApplicationFeatureFlag.DashboardJourneyFeatureFlag>()) {
            BottomMenuConfiguration {
                items = listOf(
                    DefaultUsDashboardBottomMenuItem(),
                    DefaultUsAccountsBottomMenuItem(),
                    DefaultUsMoveMoneyBottomMenuItem {
                        navigationResId = R.navigation.westerra_app_more_tab
                        startDestinationArgs =
                            bundleOf(
                                MoreJourney.INSTANCE_ID to
                                        MoreMenuInstanceId.Custom(name = MENU_INSTANCE_ID_MOVE_MONEY),
                            )
                    },
                    BottomMenuItem {
                        title = R.string.deposit_check_title.toDeferredText()
                        icon = R.drawable.ic_rdc.toDeferredDrawable()
                        navigationResId = R.navigation.westerra_app_more_tab
                        startDestinationArgs =
                            bundleOf(
                                MoreJourney.INSTANCE_ID to
                                        MoreMenuInstanceId.Custom(name = MENU_INSTANCE_ID_DEPOSIT_A_CHECK),
                            )
                    },
                    // Disable cards management until requirement comes
                    //DefaultUsCardsBottomMenuItem(),
                    DefaultUsMoreBottomMenuItem(),
                )
            }
        } else {
            BottomMenuConfiguration {
                items = listOf(
                    DefaultUsAccountsBottomMenuItem(),
                   // DefaultUsPocketsBottomMenuItem(),
                    DefaultUsMoveMoneyBottomMenuItem {
                        navigationResId = R.navigation.westerra_app_more_tab
                        startDestinationArgs =
                            bundleOf(
                                MoreJourney.INSTANCE_ID to
                                        MoreMenuInstanceId.Custom(name = MENU_INSTANCE_ID_MOVE_MONEY),
                            )
                    },
                    BottomMenuItem {
                        title = R.string.deposit_check_title.toDeferredText()
                        icon = R.drawable.ic_rdc.toDeferredDrawable()
                        navigationResId = R.navigation.westerra_app_more_tab
                        startDestinationArgs =
                            bundleOf(
                                MoreJourney.INSTANCE_ID to
                                        MoreMenuInstanceId.Custom(name = MENU_INSTANCE_ID_DEPOSIT_A_CHECK),
                            )
                    },
                    // Disable cards management until requirement comes
                   // DefaultUsCardsBottomMenuItem(),
                    DefaultUsMoreBottomMenuItem(),
                )
            }
        }
    }
}
