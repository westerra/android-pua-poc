package com.westerra.release.more.items

import androidx.core.os.bundleOf
import com.backbase.android.retail.journey.more.OnActionComplete
import com.westerra.release.R

class ChangePasswordMenuItem : BaseMenuItem() {
    override val item = makeMenuItem(
        title = R.string.change_password_title,
        icon = R.drawable.backbase_ic_password,
        actionBlock = {
            OnActionComplete.NavigateTo(R.id.action_global_changePasswordFlow, bundleOf())
        },
    )
}
