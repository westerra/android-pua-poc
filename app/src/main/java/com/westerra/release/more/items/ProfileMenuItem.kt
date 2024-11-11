package com.westerra.release.more.items

import androidx.core.os.bundleOf
import com.backbase.android.retail.journey.more.OnActionComplete
import com.westerra.release.R

class ProfileMenuItem : BaseMenuItem() {
    override val item = makeMenuItem(
        title = R.string.my_profile_title,
        icon = R.drawable.ic_person,
        actionBlock = {
            OnActionComplete.NavigateTo(R.id.action_mainScreen_to_userProfileJourney, bundleOf())
        },
    )
}
