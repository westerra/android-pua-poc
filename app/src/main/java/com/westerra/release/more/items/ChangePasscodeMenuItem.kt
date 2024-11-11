package com.westerra.release.more.items

import androidx.core.os.bundleOf
import com.backbase.android.identity.journey.authentication.AuthenticationJourney
import com.backbase.android.retail.journey.more.OnActionComplete
import com.westerra.release.R

class ChangePasscodeMenuItem : BaseMenuItem() {
    override val item = makeMenuItem(
        title = R.string.change_passcode_title,
        icon = R.drawable.ic_keypad,
        actionBlock = {
            OnActionComplete.NavigateTo(
                R.id.action_mainScreen_to_authenticationJourney,
                bundleOf(
                    AuthenticationJourney.LAUNCH_ACTION_CHANGE_PASSCODE to true,
                ),
            )
        },
    )
}
