package com.westerra.release.more.sections

import com.backbase.android.retail.journey.more.MenuItem
import com.westerra.release.BuildConfig
import com.westerra.release.R
import com.westerra.release.constants.Constants
import com.westerra.release.more.items.BTPProductsMenuItem
import com.westerra.release.more.items.CardRewardsMenuItem
import com.westerra.release.more.items.ContactsMenuItem
import com.westerra.release.more.items.ManageNotificationsMenuItem
import com.westerra.release.more.items.ProfileMenuItem

class ManageMenuSection : BaseMenuSection() {

    override val section = makeMenuSection(title = R.string.manage_title)

    override fun getMenuItems(): MutableList<MenuItem> {
        val results = mutableListOf<MenuItem>()
        results.add(ProfileMenuItem().item)
        results.add(ManageNotificationsMenuItem().item)
        results.add(ContactsMenuItem().item)
        results.add(CardRewardsMenuItem().item)
        if (BuildConfig.FLAVOR == Constants.BUILD_FLAVOR_DEV) {
            results.add(BTPProductsMenuItem().item)
        }
        return results
    }
}
