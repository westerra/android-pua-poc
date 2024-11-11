package com.westerra.release.splash

import com.backbase.android.retail.journey.app.common.splash.SplashScreenConfiguration
import com.westerra.release.R
import com.westerra.release.constants.Constants.SPLASH_DURATION_SECONDS
import com.westerra.release.extensions.toDeferredDrawable
import java.time.Duration

object SplashScreenConfig {
    operator fun invoke(): SplashScreenConfiguration {
        return SplashScreenConfiguration {
            logo = R.drawable.westerra_logo_stacked.toDeferredDrawable()
            minimumDuration = Duration.ofSeconds(SPLASH_DURATION_SECONDS)
        }
    }
}
