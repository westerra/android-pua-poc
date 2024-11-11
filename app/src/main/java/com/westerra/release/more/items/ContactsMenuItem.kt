package com.westerra.release.more.items

import androidx.core.os.bundleOf
import com.backbase.android.retail.journey.more.OnActionComplete
import com.westerra.release.R

class ContactsMenuItem : BaseMenuItem() {
    override val item = makeMenuItem(
        title = R.string.manage_contacts_title,
        icon = R.drawable.backbase_ic_contacts,
        actionBlock = {
            OnActionComplete.NavigateTo(R.id.action_moreMenu_to_contactsJourney, bundleOf())
        },
    )
}
