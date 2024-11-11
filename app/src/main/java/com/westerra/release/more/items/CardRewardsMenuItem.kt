package com.westerra.release.more.items

import androidx.core.os.bundleOf
import com.backbase.android.retail.journey.more.OnActionComplete
import com.westerra.release.R
import com.westerra.release.WesterraApplication
import com.westerra.release.constants.Constants.KEY_TOOLBAR_TITLE

class CardRewardsMenuItem : BaseMenuItem() {
    override val item = makeMenuItem(
        title = R.string.card_rewards_title,
        icon = R.drawable.ic_card_rewards,
        actionBlock = {
            OnActionComplete.NavigateTo(
                R.id.action_bottomMenuScreen_to_amplifiSSONavigation,
                bundleOf(
                    KEY_TOOLBAR_TITLE to WesterraApplication.getInstance().getString(
                        R.string.card_rewards_title,
                    ),
                ),
            )
        },
    )
}
