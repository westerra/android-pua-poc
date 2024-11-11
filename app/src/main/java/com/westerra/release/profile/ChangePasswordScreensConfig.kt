package com.westerra.release.profile

import com.backbase.android.identity.journey.userprofile.password.ChangePasswordScreenConfiguration
import com.westerra.release.R
import com.westerra.release.extensions.toDeferredDrawable
import com.westerra.release.extensions.toDeferredText

object ChangePasswordScreensConfig {
    operator fun invoke(): ChangePasswordScreenConfiguration {
        return ChangePasswordScreenConfiguration {
            background = R.color.authenticationBackgroundColor.toDeferredDrawable()
            currentPasswordLabelText = R.string.change_password_message.toDeferredText()
            changePasswordRestriction = R.string.change_password_restrictions.toDeferredText()
        }
    }
}
