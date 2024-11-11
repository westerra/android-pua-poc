package com.westerra.release.more.sections

import com.backbase.android.retail.journey.more.MenuItem
import com.westerra.release.R
import com.westerra.release.more.items.ChangePasscodeMenuItem
import com.westerra.release.more.items.ChangePasswordMenuItem
import com.westerra.release.more.items.DeleteAccountMenuItem
import com.westerra.release.more.items.EnableBiometricsMenuItem
import com.westerra.release.more.items.LogoutMenuItem

class SecurityMenuSection : BaseMenuSection() {

    override val section = makeMenuSection(title = R.string.security_title)

    override fun getMenuItems(): MutableList<MenuItem> {
        val results = mutableListOf<MenuItem>()
        results.add(ChangePasscodeMenuItem().item)
        results.add(EnableBiometricsMenuItem().item)
        results.add(ChangePasswordMenuItem().item)
        results.add(DeleteAccountMenuItem().item)
        results.add(LogoutMenuItem().item)
        return results
    }
}
