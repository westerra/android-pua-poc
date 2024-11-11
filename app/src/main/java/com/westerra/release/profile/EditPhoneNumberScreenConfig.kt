package com.westerra.release.profile

import com.backbase.android.identity.journey.userprofile.phonenumber.PhoneNumberScreenConfiguration
import com.westerra.release.R
import com.westerra.release.extensions.toDeferredBoolean
import com.westerra.release.extensions.toDeferredText

object EditPhoneNumberScreenConfig {
    operator fun invoke(): PhoneNumberScreenConfiguration {
        return PhoneNumberScreenConfiguration {
            isPrimaryEditable = false.toDeferredBoolean()
            isTypeEditable = false.toDeferredBoolean()
            titleText = R.string.change_phone_number.toDeferredText()
            confirmText = R.string.save_changes.toDeferredText()
        }
    }
}
