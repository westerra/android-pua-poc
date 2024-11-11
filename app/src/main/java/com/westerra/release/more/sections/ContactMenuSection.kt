package com.westerra.release.more.sections

import com.backbase.android.retail.journey.more.MenuItem
import com.westerra.release.BuildConfig
import com.westerra.release.R
import com.westerra.release.constants.Constants
import com.westerra.release.more.items.DisputeTransactionMenuItem
import com.westerra.release.more.items.MessagesMenuItem
import com.westerra.release.more.items.PlacesMenuItem
import com.westerra.release.more.items.ScheduleAppointmentMenuItem

class ContactMenuSection : BaseMenuSection() {

    override val section = makeMenuSection(title = R.string.contact_us_title)

    override fun getMenuItems(): MutableList<MenuItem> {
        val results = mutableListOf(
            MessagesMenuItem().item,
            DisputeTransactionMenuItem().item,
            ScheduleAppointmentMenuItem().item,
        )
        if (BuildConfig.FLAVOR == Constants.BUILD_FLAVOR_DEV) {
            results.add(PlacesMenuItem().item)
            // results.add(FindUsWebMenuItem().item)
            // results.add(FindUsCustomMenuItem().item)
        }
        return results
    }
}
