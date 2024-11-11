package com.westerra.release.profile

import com.backbase.android.identity.journey.userprofile.personalinfo.PersonalInformationScreenConfiguration
import com.westerra.release.R
import com.westerra.release.extensions.toDeferredBoolean
import com.westerra.release.extensions.toDeferredText
import com.westerra.release.profile.usecase.UserPostalAddressTypes.Companion.POBOX_KEY
import com.westerra.release.profile.usecase.UserPostalAddressTypes.Companion.RESIDENTIAL_KEY

object PersonalInformationScreenConfig {
    operator fun invoke(): PersonalInformationScreenConfiguration {
        return PersonalInformationScreenConfiguration {
            isEmailAddressAddEnabled = true.toDeferredBoolean()
            isEmailAddressEditEnabled = false.toDeferredBoolean()
            isPhoneNumberAddEnabled = true.toDeferredBoolean()
            isPhoneNumberEditEnabled = true.toDeferredBoolean()
            isPostalAddressAddEnabled = true.toDeferredBoolean()
            isPostalAddressEditEnabled = true.toDeferredBoolean()
            addressTypeMapping =
                mapOf(
                    RESIDENTIAL_KEY to R.string.home_title.toDeferredText(),
                    POBOX_KEY to R.string.mailing_title.toDeferredText(),
                )
        }
    }
}
