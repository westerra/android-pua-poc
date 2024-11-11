package com.westerra.release.notifications

import com.backbase.engagementchannels.notifications.AccountsGrouping
import com.backbase.engagementchannels.notifications.NotificationSettingsAPI
import com.backbase.engagementchannels.notifications.NotificationsConfiguration
import com.backbase.engagementchannels.notifications.dto.AccountType
import com.backbase.engagementchannels.notifications.screen.account.AccountNotificationSettingsConfiguration
import com.backbase.engagementchannels.notifications.screen.low_balance.LowBalanceNotificationSettingsConfiguration
import com.backbase.engagementchannels.notifications.screen.new_transaction.NewTransactionNotificationSettingsConfiguration
import com.backbase.engagementchannels.notifications.screen.notification_settings.NotificationSettingsConfiguration
import com.backbase.engagementchannels.notifications.screen.set_low_balance_amount.SetLowBalanceAmountConfiguration
import com.westerra.release.R
import com.westerra.release.extensions.toDeferredBoolean
import com.westerra.release.extensions.toDeferredText

object NotificationsConfig {

    // Some deprecated code exists and is working
    operator fun invoke(): NotificationsConfiguration {
        return NotificationsConfiguration {
            // Need to set useNewNotificationJourney to true even though it's deprecated.
            // Otherwise various strings substitutions don't show up.
            @Suppress("DEPRECATION")
            useNewNotificationJourney = true
            notificationSettingsEnabled = true.toDeferredBoolean()

            // Need to switch to NotificationSettingsAPI.ENGAGEMENTS in next upgrade 2023.03
            notificationSettingsAPI = NotificationSettingsAPI.ACTIONS

            notificationSettingsConfig = NotificationSettingsConfiguration {
                title = R.string.manage_notifications_title.toDeferredText()
            }
            // configuration in "Notification details" screen
            accountNotificationSettingsConfig = AccountNotificationSettingsConfiguration {
                title = R.string.notification_details_title.toDeferredText()
                accountLowBalanceTitle = R.string.balance_title.toDeferredText()
                accountLowBalanceSubtitle =
                    R.string.receive_notifications_current_balance_subtitle.toDeferredText()
                newTransactionTitle = R.string.transactions_title.toDeferredText()
                newTransactionSubtitle =
                    R.string.receive_notifications_transaction_subtitle.toDeferredText()
            }
            // configuration inside "Balance" settings
            lowBalanceNotificationSettingsConfig = LowBalanceNotificationSettingsConfiguration {
                balanceTitle = R.string.balance_title.toDeferredText()
                balanceSubtitle =
                    R.string.receive_notifications_current_balance_subtitle.toDeferredText()
            }
            // configuration while setting low balance amount and saving it
            setLowBalanceAmountConfig = SetLowBalanceAmountConfiguration {
                title = R.string.balance_title.toDeferredText()
                description =
                    R.string.receive_notifications_current_balance_subtitle.toDeferredText()
                saveAmountButton = R.string.save_amount_text.toDeferredText()
            }
            // configuration inside "Transactions" settings
            newTransactionNotificationSettingsConfig =
                NewTransactionNotificationSettingsConfiguration {
                    title = R.string.transactions_title.toDeferredText()
                    turnOffNotificationsSubtitle = R.string.wont_notified_transactions_subtitle
                        .toDeferredText()
                }
            smsDescription = R.string.sent_mobile_sms_description.toDeferredText()
            emailDescription = R.string.sent_email_description.toDeferredText()
            accountsGrouping = AccountsGrouping.ACCOUNT_TYPE
            displayedAccounts = getDisplayedAccounts()
            accountNumberMasked = true
        }
    }

    private fun getDisplayedAccounts(): List<AccountType> = AccountType.values().toList()
}
