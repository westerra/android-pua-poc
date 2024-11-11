package com.westerra.release.rdc

import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import com.backbase.android.retail.journey.more.MenuItem
import com.backbase.android.retail.journey.more.MenuSection
import com.backbase.android.retail.journey.more.MoreConfiguration
import com.backbase.android.retail.journey.more.OnActionComplete
import com.westerra.release.R
import com.westerra.release.extensions.toDeferredColor
import com.westerra.release.extensions.toDeferredDrawable
import com.westerra.release.extensions.toDeferredText

object RDCMoreConfiguration {

    operator fun invoke(): MoreConfiguration {
        return MoreConfiguration {
            screenTitle = R.string.deposit_check_title.toDeferredText()
            sections = listOf(
                getDepositACheckItemSection(),
            )
        }
    }

    private fun getDepositACheckItemSection(): MenuSection {
        return MenuSection {
            items = mutableListOf(
                MenuItem(
                    title = R.string.deposit_check_title.toDeferredText(),
                    subtitle = R.string.deposit_check_subtitle.toDeferredText(),
                    icon = R.drawable.ic_rdc.toDeferredDrawable {
                        setTint(ContextCompat.getColor(it, R.color.iconForeground))
                    },
                    iconBackgroundColor = R.color.iconBackground.toDeferredColor(),
                ) {
                    OnActionComplete.NavigateTo(R.id.action_mainScreen_to_depositCheck, bundleOf())
                },
            )
        }
    }
}
