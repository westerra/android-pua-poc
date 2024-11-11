package com.westerra.release.more.items

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.backbase.android.retail.journey.more.MenuItem
import com.backbase.deferredresources.DeferredDrawable
import com.westerra.release.R
import com.westerra.release.extensions.toDeferredColor
import com.westerra.release.extensions.toDeferredDrawable
import com.westerra.release.extensions.toDeferredText

abstract class BaseMenuItem {
    abstract val item: MenuItem

    protected fun makeMenuItem(
        @StringRes title: Int,
        @DrawableRes icon: Int,
        actionBlock: suspend () -> com.backbase.android.retail.journey.more.OnActionComplete,
    ): MenuItem {
        return MenuItem(
            title = title.toDeferredText(),
            icon = asMoreMenuIcon(icon),
            iconBackgroundColor = R.color.iconBackground.toDeferredColor(),
            actionBlock = actionBlock,
        )
    }

    private fun asMoreMenuIcon(@DrawableRes icon: Int): DeferredDrawable.Resource {
        val menuItemForegroundColor = R.color.iconForeground.toDeferredColor()
        return icon.toDeferredDrawable {
            setTint(menuItemForegroundColor.resolve(it))
        }
    }
}
