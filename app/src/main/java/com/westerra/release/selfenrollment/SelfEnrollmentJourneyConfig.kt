package com.westerra.release.selfenrollment

import com.backbase.android.identity.journey.self_enrollment.configuration.SelfEnrollmentJourneyConfiguration
import com.backbase.android.identity.journey.self_enrollment.configuration.SelfEnrollmentScreenConfiguration
import com.westerra.release.BuildConfig
import com.westerra.release.R
import com.westerra.release.constants.Constants
import com.westerra.release.extensions.appendToBaseUrl
import com.westerra.release.extensions.appendToDSBaseUrl
import com.westerra.release.extensions.toDeferredColor
import com.westerra.release.extensions.toDeferredDrawable
import com.westerra.release.extensions.toDeferredText

object SelfEnrollmentJourneyConfig {

    private const val SELF_ENROLLMENT_ENDPOINT = "/retail-app/en/enrollment?source=mobile"

    operator fun invoke(): SelfEnrollmentJourneyConfiguration {
        return SelfEnrollmentJourneyConfiguration {
            urlForEnrollment = if (BuildConfig.FLAVOR == Constants.BUILD_FLAVOR_DEV) {
                SELF_ENROLLMENT_ENDPOINT.appendToBaseUrl().toDeferredText()
            } else {
                SELF_ENROLLMENT_ENDPOINT.appendToDSBaseUrl().toDeferredText()
            }

            background = R.color.authenticationBackgroundColor.toDeferredDrawable()
            textColor = R.color.textColorPrimary.toDeferredColor()

            //TODO- 'mainScreenConfiguration: MainScreenConfiguration' is deprecated. MainScreenConfiguration is deprecated and will be removed in v4.0 at which time SelfEnrollmentScreenConfiguration will be the default screen configuration.
            //TODO - Override string resource to set different text for deprecated views.
//            mainScreenConfiguration = MainScreenConfiguration {
//                icon = R.drawable.westerra_logo_stacked.toDeferredDrawable()
//                welcomeMessageTitleText = R.string.welcome_message.toDeferredText()
//                enrollButtonText = R.string.enroll_digital_banking.toDeferredText()
//            }

            selfEnrollmentScreenConfiguration = SelfEnrollmentScreenConfiguration.Builder().apply{
                icon = R.drawable.westerra_logo_stacked.toDeferredDrawable()

            }.build()
        }
    }
}
