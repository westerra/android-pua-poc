package com.westerra.release.more.items

import androidx.core.os.bundleOf
import com.backbase.android.retail.journey.more.OnActionComplete
import com.westerra.release.R

class FindUsCustomMenuItem : BaseMenuItem() {
    override val item = makeMenuItem(
        title = R.string.find_us,
        icon = R.drawable.backbase_ic_place,
        actionBlock = {
            OnActionComplete.NavigateTo(R.id.action_mainScreen_to_findUsCustom, bundleOf())
        },
    )
}
