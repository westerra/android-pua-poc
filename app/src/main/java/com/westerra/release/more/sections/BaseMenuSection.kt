package com.westerra.release.more.sections

import androidx.annotation.StringRes
import com.backbase.android.retail.journey.more.MenuItem
import com.backbase.android.retail.journey.more.MenuSection
import com.westerra.release.extensions.toDeferredText

abstract class BaseMenuSection {

    abstract val section: MenuSection

    protected abstract fun getMenuItems(): MutableList<MenuItem>

    protected fun makeMenuSection(@StringRes title: Int): MenuSection {
        return MenuSection(
            title = title.toDeferredText(),
            items = getMenuItems(),
        )
    }
}
