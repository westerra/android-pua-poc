package com.westerra.release.workspaces

import com.backbase.android.business.journey.workspaces.config.WorkspaceSelectorScreenConfiguration
import com.backbase.android.business.journey.workspaces.config.WorkspaceSwitcherScreenConfiguration
import com.backbase.android.business.journey.workspaces.config.WorkspacesJourneyConfiguration
import com.westerra.release.R
import com.westerra.release.extensions.toDeferredColor
import com.westerra.release.extensions.toDeferredDrawable

object WorkspacesJourneyConfig {

    operator fun invoke(): WorkspacesJourneyConfiguration {
        return WorkspacesJourneyConfiguration {
            workspacesSelectorScreenConfiguration = WorkspaceSelectorScreenConfiguration {
                background = R.color.authenticationBackgroundColor.toDeferredDrawable()
                loadingProgressBarColor = R.color.textColorPrimary.toDeferredColor()
                errorTitleColorV2 = R.color.textColorPrimary.toDeferredColor()
                errorMessageColorV2 = R.color.textColorPrimary.toDeferredColor()
            }

            workspacesSwitcherScreenConfiguration = WorkspaceSwitcherScreenConfiguration {
                background = R.color.authenticationBackgroundColor.toDeferredDrawable()
                loadingProgressBarColor = R.color.textColorPrimary.toDeferredColor()
                errorTitleColorV2 = R.color.textColorPrimary.toDeferredColor()
                errorMessageColorV2 = R.color.textColorPrimary.toDeferredColor()
            }
        }
    }
}
