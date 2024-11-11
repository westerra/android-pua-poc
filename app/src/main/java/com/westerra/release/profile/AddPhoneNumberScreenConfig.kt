package com.westerra.release.profile

import com.backbase.android.identity.journey.userprofile.phonenumber.PhoneNumberScreenConfiguration
import com.westerra.release.R
import com.westerra.release.extensions.toDeferredBoolean
import com.westerra.release.extensions.toDeferredText

object AddPhoneNumberScreenConfig {
    operator fun invoke(): PhoneNumberScreenConfiguration {
        return PhoneNumberScreenConfiguration {
            isPrimaryEditable = false.toDeferredBoolean()
            isTypeEditable = true.toDeferredBoolean()
            titleText = R.string.add_a_phone_number.toDeferredText()
            confirmText = R.string.add_phone_number.toDeferredText()
        }
    }
}
