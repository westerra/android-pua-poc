package com.westerra.release.authentication

import com.backbase.android.identity.journey.authentication.otp.OtpConfiguration
import com.backbase.android.identity.journey.authentication.otp.otp_input.OtpInputScreenConfiguration
import com.westerra.release.R
import com.westerra.release.extensions.toDeferredDrawable

object OtpConfig {

    operator fun invoke(): OtpConfiguration {
        return OtpConfiguration {
            otpInputScreenConfiguration = OtpInputScreenConfiguration {
                background = R.color.authenticationBackgroundColor.toDeferredDrawable()
            }
        }
    }
}
