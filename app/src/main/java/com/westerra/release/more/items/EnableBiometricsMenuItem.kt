package com.westerra.release.more.items

import androidx.core.os.bundleOf
import com.backbase.android.identity.journey.authentication.AuthenticationJourney
import com.backbase.android.retail.journey.more.OnActionComplete
import com.westerra.release.R

class EnableBiometricsMenuItem : BaseMenuItem() {
    override val item = makeMenuItem(
        title = R.string.enable_biometrics,
        icon = R.drawable.backbase_ic_fingerprint,
        actionBlock = {
            OnActionComplete.NavigateTo(
                R.id.action_mainScreen_to_authenticationJourney,
                bundleOf(
                    AuthenticationJourney.LAUNCH_ACTION_FIDO_ENROLL_BIOMETRIC to true,
                ),
            )
        },
    )
}
