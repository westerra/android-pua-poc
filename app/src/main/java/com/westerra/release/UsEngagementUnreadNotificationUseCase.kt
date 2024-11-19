package com.westerra.release

import com.backbase.android.retail.journey.accounts_and_transactions.notification.NotificationUseCase
import com.backbase.android.retail.journey.accounts_and_transactions.notification.UnreadNotificationResponse
import com.backbase.android.retail.journey.accounts_and_transactions.transactions.CallState
import com.backbase.engagementchannels.core.util.Result
import com.backbase.engagementchannels.unreadnotifications.gen_notification_client_2.GetUnreadNotificationsRequest
import com.backbase.engagementchannels.unreadnotifications.gen_notification_client_2.UnreadNotificationCount
import com.backbase.engagementchannels.unreadnotifications.gen_notification_client_2.UnreadNotificationsUseCase

internal class UsEngagementUnreadNotificationUseCase(
    private val unreadNotificationsUseCase: UnreadNotificationsUseCase,
    private val unreadNotificationsCachedTimeInSeconds: Int?
) : NotificationUseCase {
    override suspend fun getUnreadNotificationCount(): CallState<out UnreadNotificationResponse> {
        val result = unreadNotificationsUseCase.getUnreadNotificationCount(GetUnreadNotificationsRequest {
            cachedTimeInSeconds = unreadNotificationsCachedTimeInSeconds
        })

        return when (result) {
            is Result.OnSuccess<UnreadNotificationCount> -> {
                CallState.Success(
                    UnreadNotificationResponse {
                        count = result.response.unread
                        additions = result.response.additions
                    }
                )
            }
            is Result.OnFailure -> {
                CallState.Error(result.errorResponse)
            }
            else -> {
                CallState.Empty
            }
        }
    }
}
