package com.westerra.release

import android.app.Application
import com.backbase.android.core.utils.BBLogger
import com.backbase.android.retail.journey.app.common.BackbaseSdkConfiguration
import com.backbase.android.retail.journey.app.us.UsApplicationConfiguration
import com.backbase.android.retail.journey.app.us.UsApplicationFeatureFlag
import com.backbase.android.retail.journey.app.us.UsJourneyConfigurations
import com.westerra.release.accountandtransactions.AccountTransactionsConfig
import com.westerra.release.accountstatements.AccountStatementConfiguration
import com.westerra.release.authentication.AuthenticationConfig
import com.westerra.release.bottommenu.BottomMenuConfig
import com.westerra.release.contacts.ContactsConfig
import com.westerra.release.messages.MessagesConfig
import com.westerra.release.more.MoreMenuConfig
import com.westerra.release.movemoney.MoveMoneyHubConfig
import com.westerra.release.notifications.NotificationsConfig
import com.westerra.release.payments.config.InternalA2AConfig
import com.westerra.release.payments.config.UpcomingPaymentsAppConfig
import com.westerra.release.places.PlacesConfig
import com.westerra.release.profile.UserProfileConfig
import com.westerra.release.rdc.RemoteDepositCaptureConfig
import com.westerra.release.remoteconfig.RemoteConfigConfig
import com.westerra.release.selfenrollment.SelfEnrollmentJourneyConfig
import com.westerra.release.workspaces.WorkspacesJourneyConfig

@Suppress("FunctionName") // factory method
internal fun AppConfiguration(application: Application) = UsApplicationConfiguration {
    backbaseSdkConfiguration = BackbaseSdkConfiguration {
        logLevel = if (BuildConfig.DEBUG) {
            BBLogger.LogLevel.DEBUG
        } else {
            BBLogger.LogLevel.WARN
        }
    }

    applicationFeatureFlags = listOf(
        UsApplicationFeatureFlag.WorkspacesJourneyFeatureFlag,
        UsApplicationFeatureFlag.RTCJourneyFeatureFlag,
        UsApplicationFeatureFlag.SelfEnrollmentFeatureFlag,
    )

    unreadNotificationsCachedTimeInSeconds = 5

    journeyConfigurations = UsJourneyConfigurations {
        accountStatementsConfigurationDefinition = {
            AccountStatementConfiguration(context = application.applicationContext)
        }
        workspacesJourneyConfigurationDefinition = { WorkspacesJourneyConfig() }
        accountsAndTransactionsConfigurationDefinition = {
            AccountTransactionsConfig(context = application.applicationContext)
        }
        messagesConfigurationDefinition = { MessagesConfig() }
        moreConfigurationDefinition = { MoreMenuConfig() }
        notificationsConfigurationDefinition = { NotificationsConfig() }
        authenticationConfigurationDefinition = { AuthenticationConfig() }
        moveMoneyConfigurationDefinition = { MoveMoneyHubConfig() }
        paymentJourneyConfigurationDefinition = { InternalA2AConfig() }
        upcomingPaymentsAppConfigurationDefinition = { UpcomingPaymentsAppConfig() }
        rdcConfigurationDefinition = { RemoteDepositCaptureConfig() }
        bottomMenuConfigurationDefinition = { BottomMenuConfig() }
        contactsConfigurationDefinition = { ContactsConfig() }
        placesConfigurationDefinition = { PlacesConfig() }
        userProfileConfigurationDefinition = { UserProfileConfig() }
        selfEnrollmentConfigurationDefinition = { SelfEnrollmentJourneyConfig() }
    }

    remoteConfigConfiguration = RemoteConfigConfig(appName = application.packageName)
}
