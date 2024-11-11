package com.westerra.release.more

import com.backbase.android.retail.journey.more.MoreConfiguration
import com.backbase.android.retail.journey.more.MoreToolbarMenu
import com.westerra.release.more.sections.ContactMenuSection
import com.westerra.release.more.sections.ManageMenuSection
import com.westerra.release.more.sections.SecurityMenuSection
import com.westerra.release.more.toolbar.NotificationsToolbarMenuItem

object MoreMenuConfig {

    operator fun invoke(): MoreConfiguration {
        return MoreConfiguration {
            moreToolbarMenu = getMoreToolbarMenu()
            sections = listOf(
                ManageMenuSection().section,
                ContactMenuSection().section,
                SecurityMenuSection().section,
            )
        }
    }

    private fun getMoreToolbarMenu(): MoreToolbarMenu {
        return MoreToolbarMenu {
            menuItems = mutableListOf(
                NotificationsToolbarMenuItem().item,
            )
        }
    }
}
