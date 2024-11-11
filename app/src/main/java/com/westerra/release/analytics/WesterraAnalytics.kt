package com.westerra.release.analytics

import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.westerra.release.analytics.AnalyticEventKeys.Companion.BTP_ACCOUNT_CREATION_FAILED
import com.westerra.release.analytics.AnalyticEventKeys.Companion.BTP_FUNDING_INFO_READ
import com.westerra.release.analytics.AnalyticEventKeys.Companion.BTP_FUNDING_INFO_REVIEWED
import com.westerra.release.analytics.AnalyticEventKeys.Companion.BTP_FUND_INITIATED
import com.westerra.release.analytics.AnalyticEventKeys.Companion.BTP_NEW_ACCOUNT_CREATED
import com.westerra.release.analytics.AnalyticEventKeys.Companion.BTP_SELECT_PRODUCT
import com.westerra.release.analytics.AnalyticEventKeys.Companion.BTP_SELECT_PRODUCT_CATEGORY
import com.westerra.release.analytics.AnalyticEventKeys.Companion.BTP_TERMS_ACCEPTED

class WesterraAnalytics : NavController.OnDestinationChangedListener {
    companion object {
        private val TAG = WesterraAnalytics::class.java.simpleName

        private var firebaseAnalytics: FirebaseAnalytics? = null
        private var btpClickCount = 0

        fun initializeAnalytics() {
            if (firebaseAnalytics == null) {
                firebaseAnalytics = Firebase.analytics
            }
        }

        fun recordBtpStartEvent() {
            resetBtpClickCount()
            firebaseAnalytics?.logEvent(
                FirebaseAnalytics.Event.VIEW_PROMOTION,
                bundleOf(
                    FirebaseAnalytics.Param.METHOD to "BTP",
                ),
            )
        }

        fun incrementBtpClickCount() {
            btpClickCount += 1
        }

        fun resetBtpClickCount() {
            btpClickCount = 0
        }

        fun recordScreenView(screenName: String) {
            firebaseAnalytics?.logEvent(
                FirebaseAnalytics.Event.SCREEN_VIEW,
                bundleOf(
                    FirebaseAnalytics.Param.SCREEN_NAME to screenName,
                ),
            )
        }

        fun recordBtpScreenView(screenName: String) {
            firebaseAnalytics?.logEvent(
                FirebaseAnalytics.Event.SCREEN_VIEW,
                bundleOf(
                    FirebaseAnalytics.Param.SCREEN_NAME to screenName,
                    FirebaseAnalytics.Param.VALUE to btpClickCount,
                ),
            )
        }

        fun recordBtpAccountCreatedEvent() {
            firebaseAnalytics?.logEvent(
                FirebaseAnalytics.Event.SIGN_UP,
                bundleOf(
                    FirebaseAnalytics.Param.METHOD to "BTP",
                    FirebaseAnalytics.Param.VALUE to btpClickCount,
                ),
            )
            firebaseAnalytics?.logEvent(
                BTP_NEW_ACCOUNT_CREATED,
                bundleOf(
                    FirebaseAnalytics.Param.VALUE to btpClickCount,
                ),
            )
            resetBtpClickCount()
        }

        fun recordAccountCreationFailedEvent() {
            firebaseAnalytics?.logEvent(
                BTP_ACCOUNT_CREATION_FAILED,
                bundleOf(
                    FirebaseAnalytics.Param.VALUE to btpClickCount,
                ),
            )
        }

        fun recordDeviceLoginEvent() {
            firebaseAnalytics?.logEvent(
                FirebaseAnalytics.Event.LOGIN,
                bundleOf(
                    FirebaseAnalytics.Param.METHOD to "Device",
                ),
            )
        }

        fun recordPasswordLoginEvent() {
            firebaseAnalytics?.logEvent(
                FirebaseAnalytics.Event.LOGIN,
                bundleOf(
                    FirebaseAnalytics.Param.METHOD to "Password",
                ),
            )
        }

        fun recordSelectProductCategoryEvent(category: String) {
            firebaseAnalytics?.logEvent(
                BTP_SELECT_PRODUCT_CATEGORY,
                bundleOf(
                    FirebaseAnalytics.Param.METHOD to category,
                    FirebaseAnalytics.Param.VALUE to btpClickCount,
                ),
            )
        }

        fun recordSelectProductEvent() {
            firebaseAnalytics?.logEvent(
                BTP_SELECT_PRODUCT,
                bundleOf(
                    FirebaseAnalytics.Param.VALUE to btpClickCount,
                ),
            )
        }

        fun recordTermsAcceptedEvent() {
            firebaseAnalytics?.logEvent(
                BTP_TERMS_ACCEPTED,
                bundleOf(
                    FirebaseAnalytics.Param.VALUE to btpClickCount,
                ),
            )
        }

        fun recordFundInitiatedEvent() {
            firebaseAnalytics?.logEvent(
                BTP_FUND_INITIATED,
                bundleOf(
                    FirebaseAnalytics.Param.VALUE to btpClickCount,
                ),
            )
        }

        fun recordFundingInfoReadEvent() {
            firebaseAnalytics?.logEvent(
                BTP_FUNDING_INFO_READ,
                bundleOf(
                    FirebaseAnalytics.Param.VALUE to btpClickCount,
                ),
            )
        }

        fun recordFundingInfoReviewedEvent() {
            firebaseAnalytics?.logEvent(
                BTP_FUNDING_INFO_REVIEWED,
                bundleOf(
                    FirebaseAnalytics.Param.VALUE to btpClickCount,
                ),
            )
        }
    }

    init {
        initializeAnalytics()
    }

    // TODO: Not all fragment changes seem to be detected. For example, from the more menu
    // My profile screen is detected but Manage contacts is not.
    override fun onDestinationChanged(
        controller: NavController,
        destination: NavDestination,
        arguments: Bundle?,
    ) {
        val screenName = destination.label
        if (screenName != null && screenName.isNotBlank()) {
            firebaseAnalytics?.logEvent(
                FirebaseAnalytics.Event.SCREEN_VIEW,
                bundleOf(
                    FirebaseAnalytics.Param.SCREEN_NAME to screenName,
                ),
            )
        }
    }
}
