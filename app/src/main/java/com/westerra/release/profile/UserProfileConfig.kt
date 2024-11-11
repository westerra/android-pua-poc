package com.westerra.release.profile

import com.backbase.android.identity.journey.userprofile.UserProfileConfiguration
import com.westerra.release.R
import com.westerra.release.extensions.toDeferredColor

object UserProfileConfig {
    operator fun invoke(): UserProfileConfiguration {
        return UserProfileConfiguration {
            textColor = R.color.textColorPrimary.toDeferredColor()
            maxNumberOfEmailAddresses = 1
            maxNumberOfPhoneNumbers = 3
            maxNumberOfPostalAddresses = 2
            personalInformationScreenConfiguration = PersonalInformationScreenConfig()
            addAddressScreenConfiguration = AddPostalAddressScreenConfig()
            editAddressScreenConfiguration = EditPostalAddressScreenConfig()
            addEmailScreenConfiguration = EmailScreenConfig()
            editEmailScreenConfiguration = EmailScreenConfig()
            addPhoneNumberScreenConfiguration = AddPhoneNumberScreenConfig()
            editPhoneNumberScreenConfiguration = EditPhoneNumberScreenConfig()
            enterCurrentPasswordScreenConfiguration = ChangePasswordScreensConfig()
            enterNewPasswordScreenConfiguration = ChangePasswordScreensConfig()
        }
    }
}
